package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_session")
    private Integer idSession;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "heure_debut")
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    @Column(name = "description")
    private String description;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @Column(name = "img_url")
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tranche", nullable = false)
    private TrancheAge trancheAge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cours", nullable = false)
    private Cours cours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Coach coach;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscription> inscriptions;

    public Session() {}
}
