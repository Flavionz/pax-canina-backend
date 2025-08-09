package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Owner
 * -----
 * User subtype that owns Dogs.
 * Address fields are optional (nullable).
 * The dogs relation is LAZY and managed via mappedBy="owner".
 */
@Getter @Setter
@Entity
@Table(name = "owner")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Owner extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    /** One owner -> many dogs (lazy by default). Using Set avoids Hibernate "bag" semantics. */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Dog> dogs = new LinkedHashSet<>();

    public Owner() {}
    public Owner(User u) { super(u); }

    /* Convenience methods keep both sides in sync */
    public void addDog(Dog dog) {
        if (dog == null) return;
        dogs.add(dog);
        dog.setOwner(this);
    }

    public void removeDog(Dog dog) {
        if (dog == null) return;
        dogs.remove(dog);
        dog.setOwner(null);
    }
}
