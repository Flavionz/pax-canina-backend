package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerRegistrationDto {
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
}
