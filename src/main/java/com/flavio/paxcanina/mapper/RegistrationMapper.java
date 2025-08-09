package com.flavio.paxcanina.mapper;

import com.flavio.paxcanina.dto.RegistrationDto;
import com.flavio.paxcanina.model.Registration;

/**
 * RegistrationMapper
 * ------------------
 * Maps Registration entity to RegistrationDto, avoiding entity cycles.
 */
public class RegistrationMapper {
    public static RegistrationDto toDto(Registration reg) {
        RegistrationDto dto = new RegistrationDto();
        dto.setId(reg.getIdRegistration());
        dto.setSessionName(
                reg.getSession() != null ? reg.getSession().getDescription() : null
        );
        dto.setCourseName(
                reg.getSession() != null && reg.getSession().getCourse() != null
                        ? reg.getSession().getCourse().getName() : null
        );
        dto.setDogName(
                reg.getDog() != null ? reg.getDog().getName() : null
        );
        dto.setRegistrationDate(reg.getRegistrationDate());
        dto.setStatus(reg.getStatus());
        return dto;
    }

}
