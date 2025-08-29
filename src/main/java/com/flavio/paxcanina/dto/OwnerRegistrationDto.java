package com.flavio.paxcanina.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerRegistrationDto {
    @NotBlank private String lastName;
    @NotBlank private String firstName;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank @Pattern(regexp="^\\+?[0-9 .-]{7,15}$") private String phone;

    private String address;
    private String city;

    @Pattern(regexp="^[0-9]{5}$")
    private String postalCode;
}







