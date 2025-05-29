package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;


@Getter
@Setter
public class ProfilProprietaireDto {
    private Integer id;
    private String prenom;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String ville;
    private String codePostal;
    private String bio;
    private String avatarUrl;
    private LocalDate dateInscription;
    private List<DogDto> chiens = new ArrayList<>();
    private List<InscriptionDto> inscriptions = new ArrayList<>();
}
