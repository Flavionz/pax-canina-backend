package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "age_group")
public class AgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_age_group")
    private Integer idAgeGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private AgeGroupType name;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    public AgeGroup() {}
}
