package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class RegistrationDto {
    private Integer id;
    private String sessionName;
    private String courseName;
    private String dogName;
    private LocalDate registrationDate;
    private String status;
}
