package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AgeGroupDao;
import com.flavio.paxcanina.dto.AgeGroupDto;
import com.flavio.paxcanina.model.AgeGroup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgeGroupService {
    private final AgeGroupDao ageGroupDao;

    public AgeGroupService(AgeGroupDao ageGroupDao) {
        this.ageGroupDao = ageGroupDao;
    }

    public List<AgeGroupDto> findAll() {
        return ageGroupDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public AgeGroupDto findById(Integer id) {
        AgeGroup ag = ageGroupDao.findById(id)
                .orElseThrow(() -> new RuntimeException("AgeGroup not found"));
        return toDto(ag);
    }

    // Trova la fascia per una certa età in mesi
    public AgeGroupDto findForAgeInMonths(int ageInMonths) {
        List<AgeGroup> allGroups = ageGroupDao.findAll();
        // Ordina le fasce per minAge ASC (per sicurezza)
        allGroups.sort((a, b) -> Integer.compare(a.getMinAge(), b.getMinAge()));
        for (AgeGroup group : allGroups) {
            boolean minOk = ageInMonths >= group.getMinAge();
            boolean maxOk = (group.getMaxAge() == null) || (ageInMonths < group.getMaxAge());
            if (minOk && maxOk) return toDto(group);
        }
        throw new RuntimeException("No age group found for age " + ageInMonths);
    }

    public AgeGroupDto toDto(AgeGroup ag) {
        AgeGroupDto dto = new AgeGroupDto();
        dto.setIdAgeGroup(ag.getIdAgeGroup());
        dto.setName(ag.getName());
        dto.setMinAge(ag.getMinAge());
        dto.setMaxAge(ag.getMaxAge());
        return dto;
    }
}
