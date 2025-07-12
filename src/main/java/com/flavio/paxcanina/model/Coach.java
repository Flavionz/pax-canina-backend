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

    @ManyToMany
    @JoinTable(
            name = "coach_specialization",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_specialization")
    )
    private Set<Specialization> specializations;

    @OneToMany(mappedBy = "coach")
    private Set<Session> sessions;

    public Coach() {}
    public Coach(User u) { super(u); }
}
