package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.model.Utilisateur;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.ChienService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chien")
@CrossOrigin(origins = "http://localhost:4200")
public class ChienController {

    private final ChienService chienService;

    public ChienController(ChienService chienService) {
        this.chienService = chienService;
    }

    // GET tutti i cani dell’utente loggato
    @GetMapping("/me")
    public ResponseEntity<List<DogDto>> getMyDogs(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        Integer proprietaireId = utilisateur.getIdUtilisateur();
        List<DogDto> dtos = chienService.findByProprietaireId(proprietaireId);
        return ResponseEntity.ok(dtos);
    }

    // POST nuovo cane
    @PostMapping("/me")
    public ResponseEntity<DogDto> createDog(@RequestBody DogDto dto, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        Integer proprietaireId = utilisateur.getIdUtilisateur();
        DogDto saved = chienService.createDogForProprietaire(dto, proprietaireId);
        return ResponseEntity.status(201).body(saved);
    }

    // PUT modifica cane solo se di proprietà
    @PutMapping("/me/{dogId}")
    public ResponseEntity<DogDto> updateDog(@PathVariable Integer dogId, @RequestBody DogDto dto, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        Integer proprietaireId = utilisateur.getIdUtilisateur();
        if (!chienService.isDogOwnedBy(dogId, proprietaireId)) {
            return ResponseEntity.status(403).build();
        }
        DogDto updated = chienService.updateDog(dogId, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE cane solo se di proprietà
    @DeleteMapping("/me/{dogId}")
    public ResponseEntity<Void> deleteDog(@PathVariable Integer dogId, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = userDetails.getUtilisateur();
        Integer proprietaireId = utilisateur.getIdUtilisateur();
        if (!chienService.isDogOwnedBy(dogId, proprietaireId)) {
            return ResponseEntity.status(403).build();
        }
        chienService.deleteDog(dogId);
        return ResponseEntity.noContent().build();
    }
}
