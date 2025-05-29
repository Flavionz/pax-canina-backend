package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "specialisation")
public class Specialisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialisation")
    private Integer idSpecialisation;

    @Column(name = "nom", nullable = false, unique = true)
    private String nom; // Es: Agility, Obedience, Difesa, Socializzazione

    @Column(name = "description")
    private String description;

    // (Opzionale) Relazione inversa con Coach
    @ManyToMany(mappedBy = "specialisations")
    private Set<Coach> coachs;

    public Specialisation() {}
}
