package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProprietaireRegistrationDTO {
    private String nom;
    private String prenom;
    private String email;
    private String password;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostal;
}
