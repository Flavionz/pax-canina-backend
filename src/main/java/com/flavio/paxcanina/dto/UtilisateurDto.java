package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UtilisateurDto {
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String role;     // "ADMIN" | "COACH" | "PROPRIETAIRE"
    private String avatarUrl;
    private String bio;
}
