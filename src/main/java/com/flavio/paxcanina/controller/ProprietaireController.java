package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.dto.InscriptionDto;
import com.flavio.paxcanina.dto.ProfilProprietaireDto;
import com.flavio.paxcanina.model.Chien;
import com.flavio.paxcanina.model.Inscription;
import com.flavio.paxcanina.model.Proprietaire;
import com.flavio.paxcanina.model.Utilisateur;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.ProprietaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proprietaires")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    public ProprietaireController(ProprietaireService proprietaireService) {
        this.proprietaireService = proprietaireService;
    }

    // ... altri endpoint ...

    @GetMapping("/me")
    public ResponseEntity<ProfilProprietaireDto> getMyProfile(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();

        // Controllo robusto sul ruolo
        String ruolo = utilisateur.getRole();
        if (!(utilisateur instanceof Proprietaire) ||
                !(ruolo.equals("PROPRIETAIRE") || ruolo.equals("ROLE_PROPRIETAIRE"))) {
            return ResponseEntity.status(403).build();
        }

        // Carica il proprietario con cani e iscrizioni (QUERY CUSTOM!)
        Proprietaire proprietaire = proprietaireService.findByIdWithChiensAndInscriptions(utilisateur.getIdUtilisateur());
        if (proprietaire == null) {
            return ResponseEntity.notFound().build();
        }

        // Mapping da Proprietaire a ProfilProprietaireDto
        ProfilProprietaireDto dto = new ProfilProprietaireDto();
        dto.setId(proprietaire.getIdUtilisateur());
        dto.setPrenom(proprietaire.getPrenom());
        dto.setNom(proprietaire.getNom());
        dto.setEmail(proprietaire.getEmail());
        dto.setTelephone(proprietaire.getTelephone());
        dto.setAdresse(proprietaire.getAdresse());
        dto.setVille(proprietaire.getVille());
        dto.setCodePostal(proprietaire.getCodePostal());
        dto.setBio(proprietaire.getBio());
        dto.setAvatarUrl(proprietaire.getAvatarUrl());
        dto.setDateInscription(proprietaire.getDateInscription());

        // Lista cani
        List<DogDto> chiensDto = new ArrayList<>();
        if (proprietaire.getChiens() != null) {
            chiensDto = proprietaire.getChiens().stream().map(chien -> {
                DogDto d = new DogDto();
                d.setIdChien(chien.getIdChien());
                d.setNom(chien.getNom());
                d.setRace(chien.getRace() != null ? chien.getRace().getNom() : null);
                d.setDateNaissance(chien.getDateNaissance());
                d.setSexe(chien.getSexe());
                d.setPoids(chien.getPoids());
                d.setNumeroPuce(chien.getNumeroPuce());
                d.setPhotoUrl(chien.getPhotoUrl());
                return d;
            }).collect(Collectors.toList());
        }
        dto.setChiens(chiensDto);

        // Lista iscrizioni: aggrega tutte le iscrizioni dei cani del proprietario
        List<Inscription> allInscriptions = proprietaire.getChiens() != null
                ? proprietaire.getChiens().stream()
                .filter(chien -> chien.getInscriptions() != null)
                .flatMap(chien -> chien.getInscriptions().stream())
                .distinct()
                .toList()
                : new ArrayList<>();

        List<InscriptionDto> inscriptionsDto = allInscriptions.stream().map(insc -> {
            InscriptionDto i = new InscriptionDto();
            i.setId(insc.getIdInscription());
            i.setActivity(
                    (insc.getSession() != null && insc.getSession().getCours() != null)
                            ? insc.getSession().getCours().getNom()
                            : null
            );
            i.setDate(insc.getDateInscription());
            i.setStatus(insc.getStatus());
            return i;
        }).collect(Collectors.toList());
        dto.setInscriptions(inscriptionsDto);

        return ResponseEntity.ok(dto);
    }
}
