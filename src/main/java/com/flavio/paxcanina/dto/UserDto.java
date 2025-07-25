package com.flavio.paxcanina.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * UserDto
 * -----------
 * Data Transfer Object for User entity (base class for Admin, Coach, Owner).
 * This DTO is used to transfer user data between the backend and the frontend.
 *
 * Now includes 'emailVerified' to indicate whether the user's email address has been confirmed.
 * This is important for account activation, security, and GDPR compliance.
 */
@Getter
@Setter
public class UserDto {

    /** Default constructor */
    public UserDto() {}

    /** Unique identifier for the user */
    private Integer id;

    /** Last name of the user */
    private String lastName;

    /** First name of the user */
    private String firstName;

    /** User email address (unique, required) */
    private String email;

    /** User phone number */
    private String phone;

    /** Role: ADMIN / COACH / OWNER */
    private String role;

    /** User avatar URL (optional) */
    private String avatarUrl;

    /** Short biography (optional) */
    private String bio;

    /** Password hash (never sent to the frontend in plaintext) */
    private String passwordHash;

    /**
     * NEW: Email verification status
     * true = verified, false = not verified, null = not set
     * Used for account validation workflows.
     */
    private Boolean emailVerified;

    // --------- OWNER-specific fields ---------

    /** Address (OWNER only) */
    private String address;

    /** City (OWNER only) */
    private String city;

    /** Postal code (OWNER only) */
    private String postalCode;

    // --------- COACH-specific fields ---------

    /** List of specialization IDs (COACH only) */
    private List<Integer> specializations;
}
