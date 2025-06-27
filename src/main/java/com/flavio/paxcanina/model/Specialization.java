package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialization")
    private Integer idSpecialization;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    // A specialization can be linked to many coaches
    @ManyToMany(mappedBy = "specializations")
    private Set<Coach> coaches;

    // A specialization can be linked to many courses
    @ManyToMany(mappedBy = "specializations")
    private Set<Course> courses;

    public Specialization() {}
}
