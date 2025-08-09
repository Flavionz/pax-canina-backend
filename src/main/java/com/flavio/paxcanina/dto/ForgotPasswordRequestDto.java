package com.flavio.paxcanina.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequestDto {

    @NotBlank(message = "L'adresse e-mail est requise")
    @Email(message = "Format de l'adresse e-mail invalide")
    private String email;
}
