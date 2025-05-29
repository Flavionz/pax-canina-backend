package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DogDto {
    private Integer idChien;
    private String nom;
    private String race;
    private LocalDate dateNaissance;
    private String sexe;
    private Double poids;
    private String numeroPuce;
    private String photoUrl;


}
