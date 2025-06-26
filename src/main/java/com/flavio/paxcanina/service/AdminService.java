package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AdminDao;
import com.flavio.paxcanina.dao.UtilisateurDao;
import com.flavio.paxcanina.dto.AdminProfileDto;
import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.model.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminService {

    private final AdminDao adminDao;
    private final UtilisateurDao utilisateurDao;

    public AdminService(AdminDao adminDao,
                        UtilisateurDao utilisateurDao) {
        this.adminDao = adminDao;
        this.utilisateurDao = utilisateurDao;
    }


    public List<Admin> findAllAdmins() {
        return adminDao.findAll();
    }


    @Transactional
    public Admin promoteToAdmin(int utilisateurId) {
        // 1) carica l’utente
        Utilisateur u = utilisateurDao.findById(utilisateurId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Utilisateur non trouvé avec l’id " + utilisateurId
                ));

        if (!adminDao.existsById(utilisateurId)) {
            Admin admin = new Admin();
            admin.setIdUtilisateur(u.getIdUtilisateur());
            admin.setNom(u.getNom());
            admin.setPrenom(u.getPrenom());
            admin.setEmail(u.getEmail());
            admin.setPasswordHash(u.getPasswordHash());
            admin.setTelephone(u.getTelephone());
            admin.setDateInscription(u.getDateInscription());
            admin.setAvatarUrl(u.getAvatarUrl());
            admin.setBio(u.getBio());
            admin.setLastLogin(u.getLastLogin());
            return adminDao.save(admin);
        }

        return adminDao.findById(utilisateurId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Admin record inconsistente per l’utente " + utilisateurId
                ));
    }


    @Transactional
    public void removeAdmin(int id) {
        Admin adm = adminDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Admin non trouvé avec l’id " + id
                ));
        adminDao.delete(adm);
    }


    public Admin save(Admin admin) {
        return adminDao.save(admin);
    }

    public AdminProfileDto toProfileDto(Admin admin) {
        AdminProfileDto dto = new AdminProfileDto();
        dto.setNom(admin.getNom());
        dto.setPrenom(admin.getPrenom());
        dto.setAvatarUrl(admin.getAvatarUrl());
        dto.setBio(admin.getBio());
        dto.setTelephone(admin.getTelephone());
        dto.setEmail(admin.getEmail());
        return dto;
    }


    @Transactional
    public Admin updateProfile(Admin admin, AdminProfileDto dto) {
        admin.setNom(dto.getNom());
        admin.setPrenom(dto.getPrenom());
        admin.setAvatarUrl(dto.getAvatarUrl());
        admin.setBio(dto.getBio());
        admin.setTelephone(dto.getTelephone());
        return adminDao.save(admin);
    }
}
