package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.*;
import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Service responsible for user management (CRUD),
 * role-specific entity handling (Admin/Coach/Owner),
 * and email validation token management.
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
    private final ValidationTokenDao validationTokenDao; // <- Only DAO, no external service

    /**
     * Retrieve all users as DTOs.
     */
    public List<UserDto> findAll() {
        List<User> users = userDao.findAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(toDto(user));
        }
        return dtos;
    }

    /**
     * Find a user by ID.
     */
    public UserDto findById(Integer id) {
        Optional<User> opt = userDao.findById(id);
        return opt.map(this::toDto).orElse(null);
    }

    /**
     * Create a new user (Admin/Coach/Owner).
     * Automatically sends a validation email with a unique token.
     */
    @Transactional
    public UserDto create(UserDto dto) {
        log.info("[UserService] Creating user: dto={}", dto);

        // 1. Email uniqueness check
        if (userDao.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // 2. Determine user type (default: Owner)
        String role = dto.getRole() != null ? dto.getRole().toUpperCase(Locale.ROOT) : "OWNER";
        User user;
        switch (role) {
            case "COACH" -> user = new Coach();
            case "ADMIN" -> user = new Admin();
            default -> user = new Owner();
        }

        // 3. Populate basic fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());
        user.setPasswordHash(null); // password will be set after validation
        user.setRegistrationDate(java.time.LocalDate.now());
        user.setEmailVerified(false);

        // 4. Save user and related role entity
        user = userDao.save(user);

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

        // 5. Generate and persist the email validation token
        ValidationToken token = createValidationToken(user, 60);
        String validationUrl = "http://localhost:4200/validate-email/" + token.getToken();

        // 6. Send validation email
        emailService.sendValidationEmail(user.getEmail(), validationUrl);

        log.info("[UserService] User created id={}", user.getIdUser());
        return toDto(user);
    }

    /**
     * Helper: Create a validation token (valid X minutes).
     */
    private ValidationToken createValidationToken(User user, int minutesValid) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(minutesValid);
        ValidationToken validationToken = new ValidationToken(token, expiry, user);
        return validationTokenDao.save(validationToken);
    }

    /**
     * Helper: Get a token by its string value.
     */
    private Optional<ValidationToken> getToken(String token) {
        return validationTokenDao.findByToken(token);
    }

    /**
     * Helper: Validate a token (existence and expiry).
     */
    public boolean isTokenValid(String token) {
        Optional<ValidationToken> tokenOpt = getToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().getExpiryDate().isAfter(LocalDateTime.now());
    }

    /**
     * Helper: Mark email as verified and clean up token.
     */
    @Transactional
    public boolean verifyUserEmail(String token) {
        Optional<ValidationToken> tokenOpt = getToken(token);
        if (tokenOpt.isPresent() && tokenOpt.get().getExpiryDate().isAfter(LocalDateTime.now())) {
            User user = tokenOpt.get().getUser();
            user.setEmailVerified(true);
            userDao.save(user);
            validationTokenDao.delete(tokenOpt.get());
            return true;
        }
        return false;
    }

    /**
     * Update base user fields.
     */
    @Transactional
    public UserDto update(Integer id, UserDto dto) {
        Optional<User> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            return null;
        }
        User user = userOpt.get();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());

        user = userDao.save(user);
        return toDto(user);
    }

    /**
     * Delete user by ID.
     */
    @Transactional
    public void delete(Integer id) {
        userDao.deleteById(id);
    }

    /**
     * Promote or update user (with role change support).
     */
    @Transactional
    public UserDto promoteAndUpdate(Integer id, UserDto dto) {
        log.info("=== [promoteAndUpdate] START ===");
        log.info("[promoteAndUpdate] INPUT dto: {}", dto);

        Optional<User> userOpt = userDao.findById(id);
        if (userOpt.isEmpty()) {
            log.warn("[promoteAndUpdate] User not found with id={}", id);
            return null;
        }
        User user = userOpt.get();
        String currentRole = user.getRole();
        String requestedRole = dto.getRole().toUpperCase(Locale.ROOT);

        log.info("[promoteAndUpdate] Current role: {}, Requested role: {}", currentRole, requestedRole);

        if (!currentRole.equals(requestedRole)) {
            log.info("[promoteAndUpdate] Deleting previous sub-entity for id {}", id);
            deleteSubEntity(id, currentRole);
            log.info("[promoteAndUpdate] Creating new sub-entity {}", requestedRole);
            createSubEntity(user, requestedRole, dto);
        } else {
            log.info("[promoteAndUpdate] Role unchanged, updating sub-entity if needed");
            updateSubEntity(user, currentRole, dto);
        }

        // Update base user fields
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setBio(dto.getBio());

        userDao.save(user);

        // Reload updated user and return as DTO
        Optional<User> refreshed = userDao.findById(id);
        return refreshed.map(this::toDto).orElse(null);
    }

    // ======= Private helpers for sub-entity management =======

    private void deleteSubEntity(Integer id, String role) {
        log.info("[deleteSubEntity] Deleting from {} id_user={}", role.toLowerCase(), id);
        switch (role) {
            case "OWNER" -> ownerDao.deleteById(id);
            case "COACH" -> coachDao.deleteById(id);
            case "ADMIN" -> adminDao.deleteById(id);
        }
    }

    private void createSubEntity(User user, String requestedRole, UserDto dto) {
        switch (requestedRole) {
            case "OWNER" -> {
                Owner owner = new Owner(user);
                ownerDao.save(owner);
            }
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
            case "ADMIN" -> {
                Admin admin = new Admin(user);
                adminDao.save(admin);
            }
        }
    }

    private void updateSubEntity(User user, String role, UserDto dto) {
        switch (role) {
            case "OWNER" -> {
                ownerDao.findById(user.getIdUser()).ifPresent(owner -> {
                    ownerDao.save(owner);
                });
            }
            case "COACH" -> {
                coachDao.findById(user.getIdUser()).ifPresent(coach -> {
                    if (dto.getSpecializations() != null) {
                        Set<Specialization> specs = new HashSet<>();
                        for (Integer specId : dto.getSpecializations()) {
                            specializationDao.findById(specId).ifPresent(specs::add);
                        }
                        coach.setSpecializations(specs);
                    }
                    coachDao.save(coach);
                });
            }
        }
    }

    /**
     * Map User entity to UserDto.
     */
    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getIdUser());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        dto.setEmailVerified(user.isEmailVerified());

        if (user instanceof Coach coach) {
            if (coach.getSpecializations() != null) {
                List<Integer> specIds = new ArrayList<>();
                for (Specialization s : coach.getSpecializations()) {
                    specIds.add(s.getIdSpecialization());
                }
                dto.setSpecializations(specIds);
            }
        }


        return dto;
    }
}
