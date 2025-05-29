package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

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

    // Relazione inversa opzionale (non obbligatoria per funzionare)
    @ManyToMany(mappedBy = "tranchesAge")
    private List<Cours> cours;

    public TrancheAge() {}
}
