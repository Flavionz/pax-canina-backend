package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.dto.InscriptionDto;
import com.flavio.paxcanina.dto.ProfilProprietaireDto;
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
@RequestMapping("/api/proprietaire")
@CrossOrigin(origins = "http://localhost:4200")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    public ProprietaireController(ProprietaireService proprietaireService) {
        this.proprietaireService = proprietaireService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfilProprietaireDto> getMyProfile(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();

        if (!(utilisateur instanceof Proprietaire proprietaire)) {
            return ResponseEntity.status(403).build();
        }

        Proprietaire loaded = proprietaireService.findByIdWithChiensAndInscriptions(proprietaire.getIdUtilisateur());
        if (loaded == null) return ResponseEntity.notFound().build();

        ProfilProprietaireDto dto = mapProprietaireToDto(loaded);

        return ResponseEntity.ok(dto);
    }

    private ProfilProprietaireDto mapProprietaireToDto(Proprietaire p) {
        ProfilProprietaireDto dto = new ProfilProprietaireDto();
        dto.setId(p.getIdUtilisateur());
        dto.setPrenom(p.getPrenom());
        dto.setNom(p.getNom());
        dto.setEmail(p.getEmail());
        dto.setTelephone(p.getTelephone());
        dto.setAdresse(p.getAdresse());
        dto.setVille(p.getVille());
        dto.setCodePostal(p.getCodePostal());
        dto.setBio(p.getBio());
        dto.setAvatarUrl(p.getAvatarUrl());
        dto.setDateInscription(p.getDateInscription());

        List<DogDto> chiensDto = new ArrayList<>();
        if (p.getChiens() != null) {
            chiensDto = p.getChiens().stream().map(chien -> {
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

        List<Inscription> allInscriptions = (p.getChiens() != null)
                ? p.getChiens().stream()
                .filter(chien -> chien.getInscriptions() != null)
                .flatMap(chien -> chien.getInscriptions().stream())
                .distinct()
                .toList()
                : new ArrayList<>();

        List<InscriptionDto> inscriptionsDto = allInscriptions.stream().map(insc -> {
            InscriptionDto i = new InscriptionDto();
            i.setId(insc.getIdInscription());
            i.setNomSession(insc.getSession() != null ? insc.getSession().getDescription() : null);
            i.setNomCours(insc.getSession() != null && insc.getSession().getCours() != null ? insc.getSession().getCours().getNom() : null);
            i.setNomChien(insc.getChien() != null ? insc.getChien().getNom() : null);
            i.setDateInscription(insc.getDateInscription());
            i.setStatus(insc.getStatus());
            return i;
        }).collect(Collectors.toList());
        dto.setInscriptions(inscriptionsDto);

        return dto;
    }
}
