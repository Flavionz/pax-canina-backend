package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

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

    @Column(name = "duree")
    private String duree;

    // Capienza massima della sessione
    @Column(name = "capacite_max")
    private Integer capaciteMax;

    // Livello della sessione (Enum consigliato)
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau")
    private Niveau niveau;

    // Fascia d'età della sessione
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tranche")
    private TrancheAge trancheAge;

    // Relazione con il corso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cours", nullable = false)
    private Cours cours;

    // Relazione con il coach che dirige la sessione
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coach", nullable = false)
    private Coach coach;

    // (Opzionale) Relazione OneToMany con Inscription
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Inscription> inscriptions;

    public Session() {}
}
