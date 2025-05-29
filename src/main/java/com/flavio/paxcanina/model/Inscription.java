package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    // Ogni iscrizione riguarda UN cane
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chien", nullable = false)
    private Chien chien;

    // Ogni iscrizione riguarda UNA sessione
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    private Session session;

    // (Opzionale) Proprietario che ha effettuato l'iscrizione
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "id_proprietaire")
    // private Proprietaire proprietaire;

    public Inscription() {}
}
