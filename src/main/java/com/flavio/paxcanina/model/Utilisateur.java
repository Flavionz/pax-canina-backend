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
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_utilisateur", discriminatorType = DiscriminatorType.STRING)
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    protected Integer idUtilisateur;

    @Column(name = "nom")
    protected String nom;

    @Column(name = "prenom")
    protected String prenom;

    @NotBlank
    @Email
    @Column(name = "email")
    protected String email;

    @Column(name = "password_hash")
    protected String passwordHash;

    @Column(name = "telephone")
    protected String telephone;

    @Column(name = "date_inscription")
    protected LocalDate dateInscription;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;
}