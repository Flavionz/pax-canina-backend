package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proprietaire", nullable = false)
    private Proprietaire proprietaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_race", nullable = false)
    private Race race;

    public Chien() {}
}
