package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@Entity
@DiscriminatorValue("COACH")
public class Coach extends Utilisateur {

    // Specializzazioni del coach (Agility, Obedience, ecc.)
    @ManyToMany
    @JoinTable(
            name = "coach_specialisation",
            joinColumns = @JoinColumn(name = "id_utilisateur"),
            inverseJoinColumns = @JoinColumn(name = "id_specialisation")
    )
    private Set<Specialisation> specialisations;

    // (Opzionale) Tutte le sessioni che il coach dirige
    @OneToMany(mappedBy = "coach")
    private Set<Session> sessions;

    // (Opzionale) Altri attributi specifici del coach
    // private Integer anniEsperienza;
    // private String bio;

    public Coach() {}
}
