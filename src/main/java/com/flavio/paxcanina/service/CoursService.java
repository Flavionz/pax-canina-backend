package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.CoursDao;
import com.flavio.paxcanina.dto.CoursDto;
import com.flavio.paxcanina.model.Cours;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoursService {

    private final CoursDao coursDao;

    public CoursService(CoursDao coursDao) {
        this.coursDao = coursDao;
    }

    public List<CoursDto> findAll() {
        return coursDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CoursDto findById(int id) {
        Cours c = coursDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cours non trouvé avec l’id " + id
                ));
        return toDto(c);
    }

    @Transactional
    public CoursDto create(CoursDto dto) {
        Cours c = toEntity(dto);
        Cours saved = coursDao.save(c);
        return toDto(saved);
    }

    @Transactional
    public CoursDto update(int id, CoursDto dto) {
        Cours existing = coursDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cours non trouvé avec l’id " + id
                ));
        // copia manuale dei campi modificabili
        existing.setNom(dto.getNom());
        existing.setDescription(dto.getDescription());
        existing.setStatut(dto.getStatut());
        existing.setImgUrl(dto.getImgUrl());
        Cours saved = coursDao.save(existing);
        return toDto(saved);
    }

    @Transactional
    public void delete(int id) {
        Cours existing = coursDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cours non trovato con l’id " + id
                ));
        coursDao.delete(existing);
    }

    // ————————————
    // mappers manuali Entity ↔ DTO
    private CoursDto toDto(Cours c) {
        return new CoursDto(
                c.getIdCours(),
                c.getNom(),
                c.getDescription(),
                c.getStatut(),
                c.getImgUrl()
        );
    }

    private Cours toEntity(CoursDto dto) {
        Cours c = new Cours();
        c.setNom(dto.getNom());
        c.setDescription(dto.getDescription());
        c.setStatut(dto.getStatut());
        c.setImgUrl(dto.getImgUrl());
        return c;
    }
}
