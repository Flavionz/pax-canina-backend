package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "registration", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_session", "id_dog"})
})
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registration")
    private Integer idRegistration;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "status")
    private String status;

    @Column(name = "cancellation_date")
    private LocalDate cancellationDate;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    // Many registrations can reference one dog
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dog", nullable = false)
    private Dog dog;

    // Many registrations can reference one session
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_session", nullable = false)
    private Session session;

    public Registration() {}
}
