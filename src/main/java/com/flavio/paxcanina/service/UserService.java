package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dto.ChangePasswordRequestDto;
import com.flavio.paxcanina.dao.*;
import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * UserService
 * -----------
 * Single responsibility:
 *  - CRUD for base User and role sub-entities (Admin/Coach/Owner)
 *  - Email verification flow (token issuance + verification)
 *  - Minimal security hygiene (temp password, password hashing)
 *
 * Design notes:
 *  - We do NOT physically delete users in order to keep referential integrity.
 *    (Soft-deactivation & anonymization are handled at entity level.)
 *  - Validation tokens are reusable for multiple purposes (EMAIL_VERIFY / PASSWORD_RESET).
 *  - Tokens are consumed (timestamped) rather than deleted to preserve auditability.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final AdminDao adminDao;
    private final CoachDao coachDao;
    private final OwnerDao ownerDao;
    private final SpecializationDao specializationDao;
    private final EmailService emailService;
    private final ValidationTokenDao validationTokenDao;
    private final PasswordEncoder passwordEncoder;


    /* ===========================
       Query helpers / basic CRUD
       =========================== */

    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();
        List<UserDto> dtos = new ArrayList<>(users.size());
        for (User user : users) dtos.add(toDto(user));
        return dtos;
    }

    public long countUsers() {
        return userDao.count();
    }

    public UserDto findById(Integer id) {
        return userDao.findById(id).map(this::toDto).orElse(null);
    }

    /**
     * Create a new user (Owner default) with a temporary password.
     * Sends an onboarding email in FR containing:
     *  - the temporary password
     *  - the email verification link (token-based)
     */
    @Transactional
    public UserDto create(UserDto dto) {
        log.info("[UserService] Creating user: {}", dto);

        // 1) Email uniqueness guard
        userDao.findByEmail(dto.getEmail()).ifPresent(u -> {
            throw new IllegalStateException("Email already in use");
        });

        // 2) Instantiate the right subclass; default: Owner
        String role = (dto.getRole() != null ? dto.getRole().toUpperCase(Locale.ROOT) : "OWNER");
        User user = switch (role) {
            case "COACH" -> new Coach();
            case "ADMIN" -> new Admin();
            default      -> new Owner();
        };

        // 3) Populate base fields (hash password with BCrypt/SC)
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());
        user.setRegistrationDate(LocalDate.now());
        user.setEmailVerified(false);

        String tempPassword = generateTemporaryPassword();
        user.setPasswordHash(passwordEncoder.encode(tempPassword));

        // 4) Save base user
        user = userDao.save(user);

        // 5) Save role sub-entity (JOINED inheritance requires row in sub-table)
        if (user instanceof Owner owner) {
            ownerDao.save(owner);
        }
        if (user instanceof Coach coach) {
            if (dto.getSpecializations() != null) {
                Set<Specialization> specs = new HashSet<>();
                for (Integer specId : dto.getSpecializations()) {
                    specializationDao.findById(specId).ifPresent(specs::add);
                }
                coach.setSpecializations(specs);
            }
            coachDao.save(coach);
        }
        if (user instanceof Admin admin) {
            adminDao.save(admin);
        }

        // 6) Issue EMAIL_VERIFY token (60 min validity) and send onboarding email
        ValidationToken token = createEmailValidationToken(user);
        String validationUrl = "http://localhost:4200/auth/validate-email/" + token.getToken();
        emailService.sendAccountCreatedEmail(user.getEmail(), tempPassword, validationUrl);

        log.info("[UserService] User created id={}", user.getIdUser());
        return toDto(user);
    }

    @Transactional
    public UserDto update(Integer id, UserDto dto) {
        Optional<User> opt = userDao.findById(id);
        if (opt.isEmpty()) return null;

        User user = opt.get();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());

        return toDto(userDao.save(user));
    }



    @Transactional
    public void delete(Integer id) {
        // NOTE: Physical delete kept for admin tooling;
        // for production we prefer soft-deactivate/anonymize at entity level.
        userDao.deleteById(id);
    }

    @Transactional
    public void changePassword(Integer userId, ChangePasswordRequestDto dto) {
        User user = userDao.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("user_not_found"));

        if (user.getAnonymizedAt() != null) {
            throw new IllegalStateException("user_anonymized");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("invalid_current_password");
        }

        if (!dto.getNewPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            throw new IllegalArgumentException("weak_password");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("same_password");
        }

        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordChangeAt(LocalDateTime.now());
        userDao.save(user);

        try {
            emailService.sendPasswordChangedEmail(user.getEmail());
        } catch (Exception ignored) {}
    }

    /**
     * Promote/demote a user between roles and update sub-entity data.
     * Keeps base user row and replaces sub-entity row when role changes.
     */
    @Transactional
    public UserDto promoteAndUpdate(Integer id, UserDto dto) {
        log.info("=== [promoteAndUpdate] START === dto={}", dto);

        User user = userDao.findById(id).orElse(null);
        if (user == null) {
            log.warn("[promoteAndUpdate] User not found id={}", id);
            return null;
        }

        String currentRole   = user.getRole();
        String requestedRole = dto.getRole().toUpperCase(Locale.ROOT);
        log.info("[promoteAndUpdate] {} -> {}", currentRole, requestedRole);

        if (!currentRole.equals(requestedRole)) {
            deleteSubEntity(id, currentRole);
            createSubEntity(user, requestedRole, dto);
        } else {
            updateSubEntity(user, currentRole, dto);
        }

        // Update base fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());
        userDao.save(user);

        return userDao.findById(id).map(this::toDto).orElse(null);
    }

    /* ============================
   Account state management (RGPD)
   ============================ */

    /**
     * Soft-deactivate a user: blocks login without breaking FKs.
     * Returns false if user not found.
     */
    @Transactional
    public boolean deactivateUser(Integer id) {
        User user = userDao.findById(id).orElse(null);
        if (user == null) return false;
        if (!user.isActive()) return true; // already inactive
        user.setActive(false);
        userDao.save(user);
        return true;
    }

    /**
     * Reactivate a previously deactivated account.
     * Won't reactivate anonymized users (by policy).
     */
    @Transactional
    public boolean activateUser(Integer id) {
        User user = userDao.findById(id).orElse(null);
        if (user == null) return false;
        if (user.getAnonymizedAt() != null) return false; // cannot re-enable anonymized account
        if (user.isActive()) return true; // already active
        user.setActive(true);
        userDao.save(user);
        return true;
    }

    /**
     * Anonymize a user (RGPD “right to erasure” without breaking history):
     *  - Null/placeholder personal fields
     *  - Null owner address info
     *  - Set anonymizedAt and force deactivation
     *  - (Optional) consume/invalidate outstanding tokens
     *
     * Irreversible by design. Returns false if user not found.
     */
    @Transactional
    public boolean anonymizeUser(Integer id) {
        User user = userDao.findById(id).orElse(null);
        if (user == null) return false;
        if (user.getAnonymizedAt() != null) return true; // already anonymized

        // Mask base identity (keep unique email constraint with a sink domain)
        String sinkEmail = "deleted+" + user.getIdUser() + "+" + UUID.randomUUID() + "@example.invalid";
        user.setFirstName("Anonymized");
        user.setLastName("User");
        user.setEmail(sinkEmail);
        user.setPhone(null);
        user.setAvatarUrl(null);
        user.setBio(null);

        // Deactivate and mark anonymized
        user.setActive(false);
        user.setAnonymizedAt(LocalDateTime.now());

        // OWNER-specific: drop address data
        if (user instanceof Owner owner) {
            owner.setAddress(null);
            owner.setCity(null);
            owner.setPostalCode(null);
            ownerDao.save(owner);
        }

        userDao.save(user);



        return true;
    }

    /**
     * Resend a fresh EMAIL_VERIFY link to a user.
     * Returns false if user not found or anonymized.
     */
    @Transactional
    public boolean resendVerificationEmail(Integer id) {
        User user = userDao.findById(id).orElse(null);
        if (user == null) return false;
        if (user.getAnonymizedAt() != null) return false;

        ValidationToken token = createEmailValidationToken(user);
        String validationUrl = "http://localhost:4200/auth/validate-email/" + token.getToken();
        emailService.sendValidationEmail(user.getEmail(), validationUrl);
        return true;
    }

    /* ===================================
       Token issuance / validation helpers
       =================================== */

    /**
     * Single factory for token creation — avoids duplicated code and ensures
     * we always set a purpose and a validity window.
     */
    private ValidationToken createToken(User user, TokenPurpose purpose, int minutesValid) {
        String token = UUID.randomUUID().toString(); // opaque random value
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(minutesValid);

        // Use the entity's static factories instead of "new"
        ValidationToken vt = switch (purpose) {
            case EMAIL_VERIFY   -> ValidationToken.emailVerify(user, token, expiry);
            case PASSWORD_RESET -> ValidationToken.passwordReset(user, token, expiry);
        };

        return validationTokenDao.save(vt);
    }




    /** Issue an EMAIL_VERIFY token (typical validity: 60 minutes). */
    private ValidationToken createEmailValidationToken(User user) {
        return createToken(user, TokenPurpose.EMAIL_VERIFY, 60);
    }

    /** Issue a PASSWORD_RESET token (typical validity: 30 minutes). */
    private ValidationToken createPasswordResetToken(User user) {
        return createToken(user, TokenPurpose.PASSWORD_RESET, 30);
    }

    /** Repository wrapper — fetch token by its string value. */
    private Optional<ValidationToken> getToken(String token) {
        return validationTokenDao.findByToken(token);
    }

    /**
     * Strong token validation:
     *  - expected purpose matches
     *  - not consumed
     *  - not expired
     */
    public boolean isTokenValid(String token, TokenPurpose expectedPurpose) {
        return getToken(token)
                .filter(t -> t.getPurpose() == expectedPurpose)
                .filter(t -> t.getConsumedAt() == null)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    /**
     * Email verification flow:
     *  - checks token purpose/expiry/consumption
     *  - marks user as verified
     *  - consumes the token (timestamp) for idempotency/audit
     */
    @Transactional
    public boolean verifyUserEmail(String token) {
        Optional<ValidationToken> opt = getToken(token);
        if (opt.isEmpty()) return false;

        ValidationToken vt = opt.get();
        if (vt.getPurpose() != TokenPurpose.EMAIL_VERIFY) return false;
        if (vt.getConsumedAt() != null) return false;
        if (!vt.getExpiryDate().isAfter(LocalDateTime.now())) return false;

        User user = vt.getUser();
        if (user.getAnonymizedAt() != null) return false; // do not verify anonymized accounts

        user.setEmailVerified(true);
        userDao.save(user);

        vt.setConsumedAt(LocalDateTime.now());
        validationTokenDao.save(vt);
        return true;
    }

    /* ============================
       Role sub-entity management
       ============================ */

    private void deleteSubEntity(Integer id, String role) {
        log.info("[deleteSubEntity] role={} id_user={}", role, id);
        switch (role) {
            case "OWNER" -> ownerDao.deleteById(id);
            case "COACH" -> coachDao.deleteById(id);
            case "ADMIN" -> adminDao.deleteById(id);
        }
    }

    private void createSubEntity(User user, String requestedRole, UserDto dto) {
        switch (requestedRole) {
            case "OWNER" -> ownerDao.save(new Owner(user));
            case "COACH" -> {
                Coach coach = new Coach(user);
                if (dto.getSpecializations() != null) {
                    Set<Specialization> specs = new HashSet<>();
                    for (Integer specId : dto.getSpecializations()) {
                        specializationDao.findById(specId).ifPresent(specs::add);
                    }
                    coach.setSpecializations(specs);
                }
                coachDao.save(coach);
            }
            case "ADMIN" -> adminDao.save(new Admin(user));
        }
    }

    private void updateSubEntity(User user, String role, UserDto dto) {
        switch (role) {
            case "OWNER" -> ownerDao.findById(user.getIdUser()).ifPresent(ownerDao::save);
            case "COACH" -> coachDao.findById(user.getIdUser()).ifPresent(coach -> {
                if (dto.getSpecializations() != null) {
                    Set<Specialization> specs = new HashSet<>();
                    for (Integer specId : dto.getSpecializations()) {
                        specializationDao.findById(specId).ifPresent(specs::add);
                    }
                    coach.setSpecializations(specs);
                }
                coachDao.save(coach);
            });
            case "ADMIN" -> adminDao.findById(user.getIdUser()).ifPresent(adminDao::save);
        }
    }

    /* ===================
       Mapping / utilities
       =================== */

    /**
     * Generates a random 12-char alphanumeric password.
     * This value is never persisted in clear text; only the hash is stored.
     */
    private String generateTemporaryPassword() {
        int length = 12;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }



    /** Minimal mapping: entity -> DTO for admin screens/API responses. */
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();

        // Identity & profile
        dto.setId(user.getIdUser());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());

        // Role & account state
        dto.setRole(user.getRole());
        dto.setEmailVerified(user.isEmailVerified());
        dto.setIsActive(user.isActive());

        // Audit / status
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setLastLogin(user.getLastLogin());
        dto.setAnonymizedAt(user.getAnonymizedAt());

        // OWNER-only fields
        if (user instanceof Owner owner) {
            dto.setAddress(owner.getAddress());
            dto.setCity(owner.getCity());
            dto.setPostalCode(owner.getPostalCode());
        }

        // COACH-only: specialization IDs
        if (user instanceof Coach coach && coach.getSpecializations() != null) {
            List<Integer> specIds = new ArrayList<>();
            for (Specialization s : coach.getSpecializations()) {
                specIds.add(s.getIdSpecialization());
            }
            dto.setSpecializations(specIds);
        }

        return dto;
    }
}
