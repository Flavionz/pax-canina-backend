package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "inscription")
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscription")
    private Integer idInscription;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @Column(name = "status")
    private String status;

    @Column(name = "date_annulation")
    private LocalDate dateAnnulation;

    @Column(name = "motif_annulation")
    private String motifAnnulation;

    // Una iscrizione può riguardare più cani (tramite PARTICIPE)
    @ManyToMany
    @JoinTable(
            name = "participe",
            joinColumns = @JoinColumn(name = "id_inscription"),
            inverseJoinColumns = @JoinColumn(name = "id_chien")
    )
    private List<Chien> chiens;

    // Una iscrizione può essere legata a più sessioni (tramite CONTIENT)
    @ManyToMany
    @JoinTable(
            name = "contient",
            joinColumns = @JoinColumn(name = "id_inscription"),
            inverseJoinColumns = @JoinColumn(name = "id_session")
    )
    private List<Session> sessions;

    public Inscription() {}
}
