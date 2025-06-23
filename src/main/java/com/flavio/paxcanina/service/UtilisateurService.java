package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AdminDao;
import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dao.ProprietaireDao;
import com.flavio.paxcanina.dao.UtilisateurDao;
import com.flavio.paxcanina.dto.UtilisateurDto;
import com.flavio.paxcanina.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired private UtilisateurDao utilisateurDao;
    @Autowired private ProprietaireDao proprietaireDao;
    @Autowired private CoachDao coachDao;
    @Autowired private AdminDao adminDao;
    @Autowired private PasswordEncoder passwordEncoder;

    // Utility di mapping entity <-> dto
    private UtilisateurDto toDto(Utilisateur u) {
        UtilisateurDto dto = new UtilisateurDto();
        dto.setId(u.getIdUtilisateur());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail());
        dto.setTelephone(u.getTelephone());
        dto.setAvatarUrl(u.getAvatarUrl());
        dto.setBio(u.getBio());
        // Ruolo dinamico in base alla classe
        if (u instanceof Admin) dto.setRole("ADMIN");
        else if (u instanceof Coach) dto.setRole("COACH");
        else if (u instanceof Proprietaire) dto.setRole("PROPRIETAIRE");
        else dto.setRole("UTILISATEUR");
        return dto;
    }

    private Utilisateur fromDto(UtilisateurDto dto) {
        // Crea l'istanza giusta a seconda del ruolo (ATTENZIONE: qui puoi personalizzare)
        Utilisateur u;
        switch (dto.getRole()) {
            case "ADMIN" -> u = new Admin();
            case "COACH" -> u = new Coach();
            default -> u = new Proprietaire();
        }
        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setEmail(dto.getEmail());
        u.setTelephone(dto.getTelephone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());
        return u;
    }

    public List<UtilisateurDto> findAll() {
        return utilisateurDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UtilisateurDto findById(Integer id) {
        return utilisateurDao.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public UtilisateurDto create(UtilisateurDto dto) {
        Utilisateur u = fromDto(dto);
        // Puoi generare una password random o accettarla dal DTO (meglio random + mail di attivazione)
        u.setPasswordHash(passwordEncoder.encode("PasswordTemp123")); // Cambia se necessario
        u.setDateInscription(java.time.LocalDate.now());
        utilisateurDao.save(u);
        return toDto(u);
    }

    public UtilisateurDto update(Integer id, UtilisateurDto dto) {
        var opt = utilisateurDao.findById(id);
        if (opt.isEmpty()) return null;
        Utilisateur u = opt.get();
        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setTelephone(dto.getTelephone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());
        utilisateurDao.save(u);
        return toDto(u);
    }

    public void delete(Integer id) {
        utilisateurDao.deleteById(id);
    }

    public UtilisateurDto promoteToRole(Integer id, String role) {
        var opt = utilisateurDao.findById(id);
        if (opt.isEmpty()) return null;
        Utilisateur u = opt.get();

        // "Promozione" = converti entity
        if ("ADMIN".equals(role) && !(u instanceof Admin)) {
            Admin admin = new Admin();
            copyData(u, admin);
            adminDao.save(admin);
            utilisateurDao.delete(u);
            return toDto(admin);
        }
        if ("COACH".equals(role) && !(u instanceof Coach)) {
            Coach coach = new Coach();
            copyData(u, coach);
            coachDao.save(coach);
            utilisateurDao.delete(u);
            return toDto(coach);
        }
        if ("PROPRIETAIRE".equals(role) && !(u instanceof Proprietaire)) {
            Proprietaire p = new Proprietaire();
            copyData(u, p);
            proprietaireDao.save(p);
            utilisateurDao.delete(u);
            return toDto(p);
        }
        return toDto(u); // Già del ruolo giusto
    }

    // Utility per copiare dati tra entità (senza password/email/id!)
    private void copyData(Utilisateur from, Utilisateur to) {
        to.setNom(from.getNom());
        to.setPrenom(from.getPrenom());
        to.setEmail(from.getEmail());
        to.setTelephone(from.getTelephone());
        to.setBio(from.getBio());
        to.setAvatarUrl(from.getAvatarUrl());
        to.setPasswordHash(from.getPasswordHash());
        to.setDateInscription(from.getDateInscription());
    }
}
