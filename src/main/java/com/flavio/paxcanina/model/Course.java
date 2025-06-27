package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_course")
    private Integer idCourse;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "image_url")
    private String imageUrl;

    // A course can have many sessions
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Session> sessions;

    // Many-to-Many with Specialisation (join table course_specialisation)
    @ManyToMany
    @JoinTable(
            name = "course_specialization",
            joinColumns = @JoinColumn(name = "id_course"),
            inverseJoinColumns = @JoinColumn(name = "id_specialization")
    )
    private Set<Specialization> specializations;

    // (Optional) Link to admin who created the course:
    // Add this if your SQL schema has a 'created_by' or similar column.
    // @ManyToOne
    // @JoinColumn(name = "id_admin")
    // private Admin admin;

    public Course() {}
}
