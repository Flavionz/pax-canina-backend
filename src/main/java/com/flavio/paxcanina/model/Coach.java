package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "coach")
public class Coach extends User {

    // The coach's specializations (Agility, Obedience, etc.)
    @ManyToMany
    @JoinTable(
            name = "coach_specialization", // Updated join table name
            joinColumns = @JoinColumn(name = "id_user"), // Updated FK name
            inverseJoinColumns = @JoinColumn(name = "id_specialization") // Updated FK name
    )
    private Set<Specialization> specializations;

    // All sessions managed by this coach
    @OneToMany(mappedBy = "coach")
    private Set<Session> sessions;

    public Coach() {}
}
