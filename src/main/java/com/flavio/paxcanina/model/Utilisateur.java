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
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    protected Integer idUtilisateur;

    @NotBlank
    @Column(name = "nom", nullable = false)
    protected String nom;

    @NotBlank
    @Column(name = "prenom", nullable = false)
    protected String prenom;

    @NotBlank
    @Email
    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    protected String passwordHash;

    @Column(name = "telephone")
    protected String telephone;

    @Column(name = "date_inscription", nullable = false)
    protected LocalDate dateInscription;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    // Restituisce il ruolo in base al tipo di classe figlia
    @Transient
    public String getRole() {
        return this.getClass().getSimpleName().toUpperCase();
    }

    // Getter e setter per la password (hash)
    @Transient
    public String getPassword() {
        return this.passwordHash;
    }

    @Transient
    public void setPassword(String password) {
        this.passwordHash = password;
    }
}
