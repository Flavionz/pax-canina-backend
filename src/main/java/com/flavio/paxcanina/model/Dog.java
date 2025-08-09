package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dog")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dog")
    @EqualsAndHashCode.Include
    private Integer idDog;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotBlank
    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "chip_number", unique = true)
    private String chipNumber;

    @Column(name = "weight")
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_owner", nullable = false)
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_breed")
    private Breed breed;

    /** Use Set to avoid "bag" and permettere più fetch-join simultanei. */
    @OneToMany(mappedBy = "dog", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Registration> registrations = new LinkedHashSet<>();

    public Dog() {}
}
