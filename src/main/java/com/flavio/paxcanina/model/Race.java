package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "race")
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_race")
    private Integer idRace;

    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    // Relazione opzionale: una razza può essere associata a molti cani
    @OneToMany(mappedBy = "race")
    private List<Chien> chiens;

    public Race() {}
}
