package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DogDto {
    private Integer idDog;
    private String name;
    private String breed;          // Breed name or code, adapt as needed
    private LocalDate birthDate;
    private String sex;            // Consider using Enum in the future
    private Double weight;
    private String chipNumber;
    private String photoUrl;
}
