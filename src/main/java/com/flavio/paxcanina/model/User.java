package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user") // Renamed table from "utilisateur" to "user"
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user") // Renamed from id_utilisateur
    protected Integer idUser;

    @NotBlank
    @Column(name = "last_name", nullable = false) // Renamed from nom
    protected String lastName;

    @NotBlank
    @Column(name = "first_name", nullable = false) // Renamed from prenom
    protected String firstName;

    @NotBlank
    @Email
    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    protected String passwordHash;

    @Column(name = "phone")
    protected String phone;

    @Column(name = "registration_date", nullable = false) // Renamed from date_inscription
    protected LocalDate registrationDate;

    @Column(name = "avatar_url")
    protected String avatarUrl;

    @Column(name = "bio")
    protected String bio;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    // Returns the role based on the subclass type
    @Transient
    public String getRole() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    @Transient
    public String getPassword() {
        return this.passwordHash;
    }

    @Transient
    public void setPassword(String password) {
        this.passwordHash = password;
    }
}
