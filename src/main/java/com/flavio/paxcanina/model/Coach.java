package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("COACH")
public class Coach extends Utilisateur {

    // Relazioni future (commentate per ora)
    // @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Session> sessions;

    public Coach() {}
}
