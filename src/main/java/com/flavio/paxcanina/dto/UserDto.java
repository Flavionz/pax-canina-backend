package com.flavio.paxcanina.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;
    private String role;
    private String avatarUrl;
    private String bio;
    private List<String> specializations;  // <-- AGGIUNGILO QUI!
}
