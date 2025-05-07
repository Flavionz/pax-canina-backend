package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.ProprietaireDao;
import com.flavio.paxcanina.model.Proprietaire;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProprietaireServiceImpl implements ProprietaireService {

    private final ProprietaireDao proprietaireDao;
    private final PasswordEncoder passwordEncoder;

    public ProprietaireServiceImpl(ProprietaireDao proprietaireDao, PasswordEncoder passwordEncoder) {
        this.proprietaireDao = proprietaireDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Proprietaire register(Proprietaire proprietaire) {
        if (proprietaireDao.existsByEmail(proprietaire.getEmail())) {
            throw new RuntimeException("Email déjà enregistrée");
        }
        proprietaire.setPasswordHash(passwordEncoder.encode(proprietaire.getPasswordHash()));
        proprietaire.setDateInscription(LocalDate.now());
        proprietaire.setLastLogin(LocalDateTime.now());
        return proprietaireDao.save(proprietaire);
    }

    @Override
    public List<Proprietaire> findAll() {
        return proprietaireDao.findAll();
    }

    @Override
    public Proprietaire findById(Integer id) {
        return proprietaireDao.findById(id).orElse(null);
    }
}
