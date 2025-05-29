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

    @Column(name = "places_disponibles")
    private Integer placesDisponibles;

    @Column(name = "description")
    private String description;

    @Column(name = "duree")
    private String duree; // oppure Duration se vuoi, ma controlla il mapping con SQL INTERVAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cours", nullable = false)
    private Cours cours;

    // Coach: opzionale, puoi commentare se non hai ancora l'entità Coach
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinTable(
    //     name = "cree",
    //     joinColumns = @JoinColumn(name = "id_session"),
    //     inverseJoinColumns = @JoinColumn(name = "id_utilisateur")
    // )
    // private Coach coach;

    // Relazione molti-a-molti con Inscription tramite CONTIENT
    @ManyToMany
    @JoinTable(
            name = "contient",
            joinColumns = @JoinColumn(name = "id_session"),
            inverseJoinColumns = @JoinColumn(name = "id_inscription")
    )
    private List<Inscription> inscriptions;

    // Relazione molti-a-molti con Chien tramite PARTICIPE (opzionale)
    @ManyToMany
    @JoinTable(
            name = "participe",
            joinColumns = @JoinColumn(name = "id_session"),
            inverseJoinColumns = @JoinColumn(name = "id_chien")
    )
    private List<Chien> chiens;

    public Session() {}
}
