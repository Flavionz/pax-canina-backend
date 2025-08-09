package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ValidationToken
 * ---------------
 * Reusable token entity used for:
 *   - EMAIL_VERIFY  (email address confirmation)
 *   - PASSWORD_RESET (forgot password flow)
 *
 * Design notes:
 *  - Tokens are "consumed" (timestamped) rather than deleted to preserve auditability.
 *  - Uniqueness is enforced on the token value.
 *  - For stronger security, consider storing a HASH of the token instead of the raw value.
 *  - Timestamps should be handled in UTC at the service layer.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA requires a no-arg constructor; keep it protected
@Entity
@Table(
        name = "validation_token",
        uniqueConstraints = @UniqueConstraint(columnNames = "token"),
        indexes = {
                @Index(name = "idx_vt_user_purpose", columnList = "id_user, purpose"),
                @Index(name = "idx_vt_expiry", columnList = "expiry_date")
        }
)
public class ValidationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Opaque random value. Consider hashing (e.g., SHA-256) as a future hardening step. */
    @NotBlank
    @Column(name = "token", nullable = false, length = 128)
    private String token;

    /** EMAIL_VERIFY or PASSWORD_RESET. Stored as STRING for readability and forward compatibility. */
    @Enumerated(EnumType.STRING)
    @Column(name = "purpose", nullable = false, length = 32)
    private TokenPurpose purpose;

    /** Creation timestamp; set automatically on persist; not updatable. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Hard expiry time after which the token is not valid anymore. */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /** Consumption timestamp (null until first successful use). */
    @Column(name = "consumed_at")
    private LocalDateTime consumedAt;

    /** Owning user (many tokens over time for the same user). */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    /* ----------------------------
       Static factories (recommended)
       ---------------------------- */

    /**
     * Factory: build an EMAIL_VERIFY token.
     */
    public static ValidationToken emailVerify(User user, String rawToken, LocalDateTime expiry) {
        ValidationToken vt = new ValidationToken();
        vt.setUser(user);
        vt.setToken(rawToken);
        vt.setPurpose(TokenPurpose.EMAIL_VERIFY);
        vt.setExpiryDate(expiry);
        return vt;
    }

    /**
     * Factory: build a PASSWORD_RESET token.
     */
    public static ValidationToken passwordReset(User user, String rawToken, LocalDateTime expiry) {
        ValidationToken vt = new ValidationToken();
        vt.setUser(user);
        vt.setToken(rawToken);
        vt.setPurpose(TokenPurpose.PASSWORD_RESET);
        vt.setExpiryDate(expiry);
        return vt;
    }

    /* ----------------------------
       Lifecycle & domain helpers
       ---------------------------- */

    /** Auto-set createdAt if not provided. */
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // handle UTC at service layer
        }
    }

    /** True if the token has already been consumed. */
    @Transient
    public boolean isConsumed() {
        return consumedAt != null;
    }

    /** True if the token is past its expiry at the moment of the call. */
    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    /**
     * A token is usable if:
     *  - not expired
     *  - not already consumed
     *  - for PASSWORD_RESET only: created after the user's last password change
     */
    @Transient
    public boolean isUsable(LocalDateTime lastPasswordChangeAt) {
        if (isConsumed() || isExpired()) return false;
        if (purpose == TokenPurpose.PASSWORD_RESET && lastPasswordChangeAt != null) {
            return createdAt.isAfter(lastPasswordChangeAt);
        }
        return true;
    }

    /** Mark token as consumed (idempotent; safe to call once on success). */
    public void markConsumed() {
        if (this.consumedAt == null) {
            this.consumedAt = LocalDateTime.now(); // handle UTC at service layer
        }
    }
}
