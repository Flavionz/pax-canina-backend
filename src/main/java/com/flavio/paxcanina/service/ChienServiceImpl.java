package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.ChienDao;
import com.flavio.paxcanina.dao.ProprietaireDao;
import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.model.Chien;
import com.flavio.paxcanina.model.Proprietaire;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChienServiceImpl implements ChienService {

    private final ChienDao chienDao;
    private final ProprietaireDao proprietaireDao;

    public ChienServiceImpl(ChienDao chienDao, ProprietaireDao proprietaireDao) {
        this.chienDao = chienDao;
        this.proprietaireDao = proprietaireDao;
    }

    @Override
    public List<DogDto> findByProprietaireId(Integer idProprietaire) {
        return chienDao.findByProprietaireIdUtilisateur(idProprietaire)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DogDto createDogForProprietaire(DogDto dto, Integer idProprietaire) {
        Proprietaire proprietaire = proprietaireDao.findById(idProprietaire)
                .orElseThrow(() -> new IllegalArgumentException("Proprietaire non trovato"));
        Chien chien = fromDto(dto);
        chien.setProprietaire(proprietaire);
        Chien saved = chienDao.save(chien);
        return toDto(saved);
    }

    @Override
    public boolean isDogOwnedBy(Integer dogId, Integer idProprietaire) {
        return chienDao.findById(dogId)
                .map(chien -> chien.getProprietaire().getIdUtilisateur().equals(idProprietaire))
                .orElse(false);
    }

    @Override
    public DogDto updateDog(Integer dogId, DogDto dto) {
        Chien existing = chienDao.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Chien non trovato"));
        // Aggiorna solo i campi modificabili
        existing.setNom(dto.getNom());
        existing.setRace(null); // Gestiscilo se hai oggetto Race
        existing.setDateNaissance(dto.getDateNaissance());
        existing.setSexe(dto.getSexe());
        existing.setPoids(dto.getPoids());
        existing.setNumeroPuce(dto.getNumeroPuce());
        existing.setPhotoUrl(dto.getPhotoUrl());
        Chien saved = chienDao.save(existing);
        return toDto(saved);
    }

    @Override
    public void deleteDog(Integer dogId) {
        chienDao.deleteById(dogId);
    }

    // --- Mapping methods ---
    private DogDto toDto(Chien c) {
        DogDto dto = new DogDto();
        dto.setIdChien(c.getIdChien());
        dto.setNom(c.getNom());
        dto.setRace(c.getRace() != null ? c.getRace().getNom() : null);
        dto.setDateNaissance(c.getDateNaissance());
        dto.setSexe(c.getSexe());
        dto.setPoids(c.getPoids());
        dto.setNumeroPuce(c.getNumeroPuce());
        dto.setPhotoUrl(c.getPhotoUrl());
        return dto;
    }

    private Chien fromDto(DogDto dto) {
        Chien c = new Chien();
        c.setNom(dto.getNom());
        // c.setRace(...) // gestisci se vuoi il collegamento all'entità Race
        c.setDateNaissance(dto.getDateNaissance());
        c.setSexe(dto.getSexe());
        c.setPoids(dto.getPoids());
        c.setNumeroPuce(dto.getNumeroPuce());
        c.setPhotoUrl(dto.getPhotoUrl());
        return c;
    }
}
