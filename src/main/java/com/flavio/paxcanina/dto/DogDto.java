package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DogDto {
    private Integer idDog;
    private String name;
    private String breed;
    private LocalDate birthDate;
    private String sex;
    private Double weight;
    private String chipNumber;
    private String photoUrl;
}
