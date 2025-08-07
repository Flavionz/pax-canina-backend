package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.model.User;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing user's dogs (CRUD for the authenticated owner).
 */
@RestController
@RequestMapping("/api/dogs")
@CrossOrigin(origins = "http://localhost:4200")
public class DogController {

    private final DogService dogService;

    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    /**
     * GET all dogs for the authenticated owner.
     */
    @GetMapping("/me")
    public ResponseEntity<List<DogDto>> getMyDogs(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer ownerId = user.getIdUser();
        List<DogDto> dtos = dogService.findByOwnerId(ownerId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DogDto>> getAllDogs() {
        List<DogDto> dtos = dogService.findAll();
        return ResponseEntity.ok(dtos);
    }


    /**
     * POST a new dog for the authenticated owner.
     */
    @PostMapping("/me")
    public ResponseEntity<DogDto> createDog(@RequestBody DogDto dto, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer ownerId = user.getIdUser();
        DogDto saved = dogService.createDogForOwner(dto, ownerId);
        return ResponseEntity.status(201).body(saved);
    }

    /**
     * PUT update a dog if and only if it belongs to the authenticated owner.
     */
    @PutMapping("/me/{dogId}")
    public ResponseEntity<DogDto> updateDog(@PathVariable Integer dogId, @RequestBody DogDto dto, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer ownerId = user.getIdUser();
        if (!dogService.isDogOwnedBy(dogId, ownerId)) {
            return ResponseEntity.status(403).build();
        }
        DogDto updated = dogService.updateDog(dogId, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE a dog if and only if it belongs to the authenticated owner.
     */
    @DeleteMapping("/me/{dogId}")
    public ResponseEntity<Void> deleteDog(@PathVariable Integer dogId, Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Integer ownerId = user.getIdUser();
        if (!dogService.isDogOwnedBy(dogId, ownerId)) {
            return ResponseEntity.status(403).build();
        }
        dogService.deleteDog(dogId);
        return ResponseEntity.noContent().build();
    }
}
