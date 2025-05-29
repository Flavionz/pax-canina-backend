package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cours")
public class Cours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cours")
    private Integer idCours;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "statut")
    private String statut;

    // Un corso può avere molte sessioni
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions;

    // (Opzionale) Relazione con Admin creatore del corso
    // @ManyToOne
    // @JoinColumn(name = "id_admin")
    // private Admin adminCreateur;

    public Cours() {}
}
