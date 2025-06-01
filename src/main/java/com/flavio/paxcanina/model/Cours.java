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

    @Column(name = "img_url")
    private String imgUrl;


    // Un corso può avere molte sessioni
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions;

    // ManyToMany con Specialisation (tabella di join cours_specialisation)
    @ManyToMany
    @JoinTable(
            name = "cours_specialisation",
            joinColumns = @JoinColumn(name = "id_cours"),
            inverseJoinColumns = @JoinColumn(name = "id_specialisation")
    )
    private Set<Specialisation> specialisations;

    // (Opzionale) Relazione con Admin creatore del corso:
    // Solo se aggiungi id_admin nello schema.sql!

    public Cours() {}
}
