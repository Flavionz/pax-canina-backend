package com.flavio.paxcanina.dto;

import com.flavio.paxcanina.model.AgeGroupType;
import lombok.Data;

@Data
public class AgeGroupDto {
    private Integer idAgeGroup;
    private AgeGroupType name;
    private Integer minAge;
    private Integer maxAge;
}
