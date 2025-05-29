package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "type_cours")
public class TypeCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_type_cours")
    private Integer idTypeCours;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;

    // Relazione inversa opzionale (non obbligatoria per funzionare)
    @ManyToMany(mappedBy = "types")
    private List<Cours> cours;

    public TypeCours() {}
}
