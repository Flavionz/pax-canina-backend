// src/main/java/com/flavio/paxcanina/dto/AdminProfileDto.java
package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminProfileDto {
    private String nom;
    private String prenom;
    private String avatarUrl;
    private String bio;
    private String telephone;
    private String email;
}
