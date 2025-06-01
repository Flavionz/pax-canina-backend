package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class InscriptionDto {
    private Integer id;
    private String nomSession;
    private String nomCours;
    private String nomChien;
    private LocalDate dateInscription;
    private String status;
}
