package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    @Column(name = "capacite_max")
    private Integer capaciteMax;

    @Column(name = "niveau")
    private String niveau;

    // Un corso può avere molte sessioni
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions;

    // Un corso può essere categorizzato in più tipi (ManyToMany con TypeCours)
    @ManyToMany
    @JoinTable(
            name = "categorise",
            joinColumns = @JoinColumn(name = "id_cours"),
            inverseJoinColumns = @JoinColumn(name = "id_type_cours")
    )
    private List<TypeCours> types;

    // Un corso può corrispondere a più fasce d'età (ManyToMany con TrancheAge)
    @ManyToMany
    @JoinTable(
            name = "correspond",
            joinColumns = @JoinColumn(name = "id_cours"),
            inverseJoinColumns = @JoinColumn(name = "id_tranche")
    )
    private List<TrancheAge> tranchesAge;

    // Relazione con Admin che crea il corso (opzionale, da implementare in futuro)
    // @ManyToOne
    // @JoinTable(
    //     name = "cree_cours",
    //     joinColumns = @JoinColumn(name = "id_cours"),
    //     inverseJoinColumns = @JoinColumn(name = "id_utilisateur")
    // )
    // private Admin adminCreateur;

    public Cours() {}
}
