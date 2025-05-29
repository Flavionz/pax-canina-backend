package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class InscriptionDto {
    private Integer id;
    private String activity;
    private LocalDate date;
    private String status;


}
