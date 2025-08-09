package com.flavio.paxcanina.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChangePasswordRequestDto {

    // Old password confirmation to prevent silent hijack
    @NotBlank(message = "Le mot de passe actuel est requis.")
    private String currentPassword;

    @NotBlank(message = "Le nouveau mot de passe est requis.")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    private String newPassword;
}
