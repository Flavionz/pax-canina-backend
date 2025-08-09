package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.dao.ValidationTokenDao;
import com.flavio.paxcanina.dto.ChangePasswordRequestDto;
import com.flavio.paxcanina.dto.ForgotPasswordRequestDto;
import com.flavio.paxcanina.dto.LoginRequest;
import com.flavio.paxcanina.dto.OwnerRegistrationDto;
import com.flavio.paxcanina.dto.ResetPasswordRequestDto;
import com.flavio.paxcanina.model.Owner;
import com.flavio.paxcanina.model.TokenPurpose;
import com.flavio.paxcanina.model.User;
import com.flavio.paxcanina.model.ValidationToken;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.EmailService;
import com.flavio.paxcanina.service.JwtService;
import com.flavio.paxcanina.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    // ======================
    // Dependencies
    // ======================
    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final ValidationTokenDao validationTokenDao;
    private final EmailService emailService;

    // ======================
    // REGISTRATION (Owner)
    // ======================
    @PostMapping("/register/owner")
    public ResponseEntity<?> registerOwner(@RequestBody @Valid OwnerRegistrationDto dto) {
        Owner owner = new Owner();
        owner.setLastName(dto.getLastName());
        owner.setFirstName(dto.getFirstName());
        owner.setEmail(dto.getEmail());
        owner.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        owner.setPhone(dto.getPhone());
        owner.setRegistrationDate(LocalDate.now());
        owner.setAddress(dto.getAddress());
        owner.setCity(dto.getCity());
        owner.setPostalCode(dto.getPostalCode());
        userDao.save(owner);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // =============
    // LOGIN (JWT)
    // =============
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            String role = userDetails.getRole();
            String token = jwtService.generateToken(userDetails, role);
            return ResponseEntity.ok(new JwtResponse(token, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Identifiants invalides"));
        }
    }

    // ======================================
    // EMAIL VERIFICATION (Token-based)
    // ======================================
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        boolean ok = userService.verifyUserEmail(token);
        return ok
                ? ResponseEntity.ok(Map.of("status", "verifie"))
                : ResponseEntity.badRequest().body(Map.of("error", "jeton_invalide_ou_expire"));
    }

    @PostMapping("/resend-verification/{id}")
    public ResponseEntity<?> resendVerification(@PathVariable Integer id) {
        boolean ok = userService.resendVerificationEmail(id);
        return ok
                ? ResponseEntity.ok(Map.of("status", "renvoye"))
                : ResponseEntity.badRequest().body(Map.of("error", "utilisateur_introuvable_ou_deja_verifie"));
    }

    // ======================================
    // PASSWORD RESET (unauthenticated flow)
    // ======================================

    /**
     * Step 1: Ask for a reset link.
     * Security: always return 200 to avoid email enumeration.
     */
    @PostMapping("/password-reset-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid ForgotPasswordRequestDto dto) {
        Optional<User> opt = userDao.findByEmail(dto.getEmail());

        if (opt.isPresent() && opt.get().getAnonymizedAt() == null) {
            User user = opt.get();

            // one-time token (30 min validity)
            String rawToken = UUID.randomUUID().toString();
            ValidationToken vt = ValidationToken.passwordReset(
                    user,
                    rawToken,
                    LocalDateTime.now().plusMinutes(30)
            );
            validationTokenDao.save(vt);

            String resetUrl = "http://localhost:4200/auth/reset-password?token=" + rawToken;
            emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
            log.info("[AuthController] Password reset link sent to {}", user.getEmail());
        }

        // Same UX regardless of account existence
        return ResponseEntity.ok(Map.of(
                "message", "Si un compte existe pour cette adresse, vous recevrez un e-mail avec les instructions."
        ));
    }

    /**
     * Step 2: Submit new password (token).
     */
    @PostMapping("/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequestDto dto) {

        Optional<ValidationToken> opt = validationTokenDao
                .findByTokenAndPurposeAndConsumedAtIsNull(dto.getToken(), TokenPurpose.PASSWORD_RESET);

        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "jeton_invalide_ou_deja_utilise"));
        }

        ValidationToken vt = opt.get();

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Map.of("error", "jeton_expire"));
        }

        User user = vt.getUser();
        if (user.getAnonymizedAt() != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "compte_anonymise"));
        }

        // Change password & audit
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordChangeAt(LocalDateTime.now());
        userDao.save(user);

        // Invalidate token
        vt.setConsumedAt(LocalDateTime.now());
        validationTokenDao.save(vt);

        // Optional: notify user
        try {
            emailService.sendPasswordChangedEmail(user.getEmail());
        } catch (Exception ignored) {}

        log.info("[AuthController] Password successfully reset for {}", user.getEmail());
        return ResponseEntity.ok(Map.of("message", "Mot de passe mis à jour"));
    }

    // ======================================
    // CHANGE PASSWORD (authenticated flow)
    // ======================================

    /**
     * Authenticated password change:
     *  - verifies current password
     *  - forbids reusing the same password
     *  - updates audit fields
     *  - (optional) sends notification email
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal AppUserDetails principal,
                                            @RequestBody @Valid ChangePasswordRequestDto dto) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "non_authentifie"));
        }

        User user = userDao.findById(principal.getIdUser()).orElse(null);
        if (user == null || user.getAnonymizedAt() != null || !user.isActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "compte_inactif_ou_anonymise"));
        }

        // Verify current password
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "mot_de_passe_incorrect"));
        }

        // Prevent reuse of same password
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "nouveau_identique_ancien"));
        }

        // Update password + audit
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setLastPasswordChangeAt(LocalDateTime.now());
        userDao.save(user);

        // Optional: notify by email (best effort)
        try {
            emailService.sendPasswordChangedEmail(user.getEmail());
        } catch (Exception ignored) {}

        log.info("[AuthController] Password changed by user id={}", user.getIdUser());
        return ResponseEntity.ok(Map.of("message", "Mot de passe modifié avec succès"));
    }

    // ===== JWT response DTO (simple and explicit) =====
    @Getter
    public static class JwtResponse {
        public String token;
        public String role;
        public JwtResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }
    }
}
