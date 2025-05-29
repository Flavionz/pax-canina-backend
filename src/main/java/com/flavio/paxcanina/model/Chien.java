package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "chien")
public class Chien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_chien")
    private Integer idChien;

    @NotBlank
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @NotBlank
    @Column(name = "sexe", nullable = false)
    private String sexe;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "numero_puce")
    private String numeroPuce;

    @Column(name = "poids")
    private Double poids;


    // Un cane ha UN solo proprietario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Proprietaire proprietaire;

    // Un cane ha UNA sola razza
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_race", nullable = false)
    private Race race;

    // Un cane può partecipare a più sessioni (ma solo una volta per sessione)
    @ManyToMany
    @JoinTable(
            name = "participe",
            joinColumns = @JoinColumn(name = "id_chien"),
            inverseJoinColumns = @JoinColumn(name = "id_session")
    )
    private List<Session> sessions;

    @ManyToMany
    @JoinTable(
            name = "participe",
            joinColumns = @JoinColumn(name = "id_chien"),
            inverseJoinColumns = @JoinColumn(name = "id_inscription")
    )
    private List<Inscription> inscriptions;


    public Chien() {}
}
