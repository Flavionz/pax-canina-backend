package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "breed")
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_breed")
    private Integer idBreed;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    // Optional relation: one breed can be associated with many dogs
    @OneToMany(mappedBy = "breed")
    private List<Dog> dogs;

    public Breed() {}
}
