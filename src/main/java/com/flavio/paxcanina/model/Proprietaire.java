package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "proprietaire")
public class Proprietaire extends Utilisateur {

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Chien> chiens = new HashSet<>();

    public Proprietaire() {}
}
