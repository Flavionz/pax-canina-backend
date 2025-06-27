package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Getter
@Setter
public class OwnerProfileDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String bio;
    private String avatarUrl;
    private LocalDate registrationDate;
    private List<DogDto> dogs = new ArrayList<>();
    private List<RegistrationDto> registrations = new ArrayList<>();
}
