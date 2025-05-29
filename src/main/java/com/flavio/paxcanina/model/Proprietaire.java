package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

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
    @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chien> chiens;

    // Se vuoi, puoi aggiungere un campo per tenere traccia delle iscrizioni (opzionale, vedi nota sotto)
    // @OneToMany(mappedBy = "proprietaire", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Inscription> inscriptions;

    public Proprietaire() {}
}
