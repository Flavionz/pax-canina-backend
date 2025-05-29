package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tranche_age")
public class TrancheAge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tranche")
    private Integer idTranche;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "age_min")
    private Integer ageMin;

    @Column(name = "age_max")
    private Integer ageMax;

    // (Opzionale) Relazione inversa con Session
    // @OneToMany(mappedBy = "trancheAge")
    // private List<Session> sessions;

    public TrancheAge() {}
}
