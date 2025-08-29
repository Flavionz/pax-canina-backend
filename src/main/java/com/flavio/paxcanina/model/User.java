package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    protected Integer idUser;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    protected String lastName;

    @NotBlank
    @Column(name = "first_name", nullable = false)
    protected String firstName;

    @NotBlank @Email
    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    protected String passwordHash;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "registration_date", nullable = false)
    protected LocalDate registrationDate;

    @Column(name = "avatar_url")
    protected String avatarUrl;

    @Column(name = "bio")
    protected String bio;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    @Column(name = "email_verified", nullable = false)
    protected boolean emailVerified = false;

    @Column(name = "is_active", nullable = false)
    protected boolean active = true;

    @Column(name = "anonymized_at")
    protected LocalDateTime anonymizedAt;

    @Column(name = "last_password_change_at")
    protected LocalDateTime lastPasswordChangeAt;

    public User(User u) {
        this.idUser = u.idUser;
        this.lastName = u.lastName;
        this.firstName = u.firstName;
        this.email = u.email;
        this.passwordHash = u.passwordHash;
        this.phone = u.phone;
        this.registrationDate = u.registrationDate;
        this.avatarUrl = u.avatarUrl;
        this.bio = u.bio;
        this.lastLogin = u.lastLogin;
        this.emailVerified = u.emailVerified;
        this.active = u.active;
        this.anonymizedAt = u.anonymizedAt;
        this.lastPasswordChangeAt = u.lastPasswordChangeAt;
    }

    @Transient
    public String getRole() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    // Convenience methods (optional but handy)
    public boolean isAnonymized()        { return anonymizedAt != null; }
    public void deactivate()             { this.active = false; }
    public void anonymize()              { this.active = false; this.anonymizedAt = LocalDateTime.now(); }
    public void markPasswordChanged()    { this.lastPasswordChangeAt = LocalDateTime.now(); }
}
