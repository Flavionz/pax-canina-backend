package com.flavio.paxcanina.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UserDto
 * -------
 * Safe DTO for admin dashboard & API.
 * - No passwordHash exposure
 * - Exposes account state flags (isActive, emailVerified, anonymizedAt)
 */
@Getter @Setter
public class UserDto {

    private Integer id;

    // Identity
    private String lastName;
    private String firstName;
    private String email;
    private String phone;

    // Role: ADMIN / COACH / OWNER
    private String role;

    // Profile
    private String avatarUrl;
    private String bio;

    // Account state
    private Boolean emailVerified;
    private Boolean isActive;

    // Read-only audit/status
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate registrationDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastLogin;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime anonymizedAt;

    // OWNER-only
    private String address;
    private String city;
    private String postalCode;

    // COACH-only
    private List<Integer> specializations;
}
