package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.SpecializationDao;
import com.flavio.paxcanina.dto.SpecializationDto;
import com.flavio.paxcanina.model.Specialization;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationDao specializationDao;

    public SpecializationService(SpecializationDao specializationDao) {
        this.specializationDao = specializationDao;
    }

    public List<SpecializationDto> findAll() {
        return specializationDao.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public SpecializationDto findById(Integer id) {
        Specialization s = specializationDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialization not found"));
        return toDto(s);
    }

    @Transactional
    public SpecializationDto create(SpecializationDto dto) {
        if (specializationDao.existsByName(dto.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Specialization already exists with this name");
        }
        Specialization entity = toEntity(dto);
        return toDto(specializationDao.save(entity));
    }

    @Transactional
    public SpecializationDto update(Integer id, SpecializationDto dto) {
        Specialization existing = specializationDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialization not found"));
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        return toDto(specializationDao.save(existing));
    }

    @Transactional
    public void delete(Integer id) {
        if (!specializationDao.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialization not found");
        }
        specializationDao.deleteById(id);
    }

    // --- Mapping methods ---
    private SpecializationDto toDto(Specialization s) {
        SpecializationDto dto = new SpecializationDto();
        dto.setId(s.getIdSpecialization());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());
        return dto;
    }

    private Specialization toEntity(SpecializationDto dto) {
        Specialization s = new Specialization();
        s.setIdSpecialization(dto.getId());
        s.setName(dto.getName());
        s.setDescription(dto.getDescription());
        return s;
    }
}
