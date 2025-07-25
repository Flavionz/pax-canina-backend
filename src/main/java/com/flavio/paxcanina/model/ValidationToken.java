package com.flavio.paxcanina.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class ValidationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ValidationToken() {}

    public ValidationToken(String token, LocalDateTime expiryDate, User user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public void setToken(String token) { this.token = token; }

    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public void setUser(User user) { this.user = user; }
}
