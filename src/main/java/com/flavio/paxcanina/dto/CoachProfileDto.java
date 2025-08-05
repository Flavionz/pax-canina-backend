package com.flavio.paxcanina.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CoachProfileDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;
    private String avatarUrl;
    private List<SpecializationDto> specializations;
}
