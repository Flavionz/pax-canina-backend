package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("PROPRIETAIRE")
public class Proprietaire extends Utilisateur {

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    // Un proprietario può avere molti cani
    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Chien> chiens = new HashSet<>();

    // (Opzionale) Se vuoi tracciare tutte le iscrizioni fatte dal proprietario
    // @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Inscription> inscriptions;

    public Proprietaire() {}
}
