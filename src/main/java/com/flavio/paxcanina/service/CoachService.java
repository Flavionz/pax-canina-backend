package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dto.CoachProfileDto;
import com.flavio.paxcanina.dto.SpecializationDto;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.model.Specialization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoachService {

    private final CoachDao coachDao;

    public CoachService(CoachDao coachDao) {
        this.coachDao = coachDao;
    }

    /** Get coach by ID with specializations fully loaded */
    public Optional<Coach> findById(Integer id) {
        return coachDao.findById(id); // thanks to @EntityGraph, specializations are fetched
    }

    /** Map Coach entity to DTO */
    public CoachProfileDto toProfileDto(Coach coach) {
        CoachProfileDto dto = new CoachProfileDto();
        dto.setId(coach.getIdUser());
        dto.setFirstName(coach.getFirstName());
        dto.setLastName(coach.getLastName());
        dto.setEmail(coach.getEmail());
        dto.setPhone(coach.getPhone());
        dto.setBio(coach.getBio());
        dto.setAvatarUrl(coach.getAvatarUrl());
        if (coach.getSpecializations() != null) {
            dto.setSpecializations(
                    coach.getSpecializations().stream()
                            .map(this::toSpecializationDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private SpecializationDto toSpecializationDto(Specialization s) {
        SpecializationDto dto = new SpecializationDto();
        dto.setId(s.getIdSpecialization());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        return dto;
    }

    @Transactional
    public Coach updateProfile(Coach coach, CoachProfileDto dto) {
        coach.setFirstName(dto.getFirstName());
        coach.setLastName(dto.getLastName());
        coach.setPhone(dto.getPhone());
        coach.setBio(dto.getBio());
        coach.setAvatarUrl(dto.getAvatarUrl());
        return coachDao.save(coach);
    }
}
