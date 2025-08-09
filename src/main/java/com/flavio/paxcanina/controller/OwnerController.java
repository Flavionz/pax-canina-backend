package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.dto.RegistrationDto;
import com.flavio.paxcanina.dto.OwnerProfileDto;
import com.flavio.paxcanina.mapper.RegistrationMapper;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Owner;
import com.flavio.paxcanina.model.User;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OwnerController
 * ---------------
 * Exposes endpoints for managing owner profile and personal dogs.
 * Uses DTOs and mappers to avoid cyclic entity serialization and ensure a clean API.
 */

@RestController
@RequestMapping("/api/owner")
@CrossOrigin(origins = "http://localhost:4200")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * GET /api/owner/me
     * Returns profile info of the currently authenticated owner.
     * Uses DTOs and RegistrationMapper to avoid cyclic references.
     */
    @GetMapping("/me")
    public ResponseEntity<OwnerProfileDto> getMyProfile(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!(user instanceof Owner owner)) {
            return ResponseEntity.status(403).build();
        }

        Owner loaded = ownerService.findByIdWithDogsAndRegistrations(owner.getIdUser());
        if (loaded == null) return ResponseEntity.notFound().build();

        OwnerProfileDto dto = mapOwnerToDto(loaded);

        return ResponseEntity.ok(dto);
    }

    /**
     * PUT /api/owner/me
     * Updates the profile info of the currently authenticated owner.
     */
    @PutMapping("/me")
    public ResponseEntity<OwnerProfileDto> updateMyProfile(
            @RequestBody OwnerProfileDto dto,
            Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!(user instanceof Owner owner)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Owner updated = ownerService.update(owner.getIdUser(), mapDtoToOwner(dto));
            OwnerProfileDto updatedDto = mapOwnerToDto(updated);
            return ResponseEntity.ok(updatedDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/owner/me
     * Deletes the profile of the currently authenticated owner.
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!(user instanceof Owner owner)) {
            return ResponseEntity.status(403).build();
        }
        ownerService.delete(owner.getIdUser());
        return ResponseEntity.noContent().build();
    }

    // --- DTO Mapping Helpers ---

    /**
     * Maps an OwnerProfileDto to an Owner entity (for updates).
     */
    private Owner mapDtoToOwner(OwnerProfileDto dto) {
        Owner o = new Owner();
        o.setIdUser(dto.getId());
        o.setLastName(dto.getLastName());
        o.setFirstName(dto.getFirstName());
        o.setEmail(dto.getEmail());
        o.setPhone(dto.getPhone());
        o.setAddress(dto.getAddress());
        o.setCity(dto.getCity());
        o.setPostalCode(dto.getPostalCode());
        o.setBio(dto.getBio());
        o.setAvatarUrl(dto.getAvatarUrl());
        return o;
    }

    /**
     * Maps an Owner entity to OwnerProfileDto, including dogs and registrations.
     * Uses RegistrationMapper to avoid code duplication and cyclic JSON issues.
     */
    private OwnerProfileDto mapOwnerToDto(Owner o) {
        OwnerProfileDto dto = new OwnerProfileDto();
        dto.setId(o.getIdUser());
        dto.setFirstName(o.getFirstName());
        dto.setLastName(o.getLastName());
        dto.setEmail(o.getEmail());
        dto.setPhone(o.getPhone());
        dto.setAddress(o.getAddress());
        dto.setCity(o.getCity());
        dto.setPostalCode(o.getPostalCode());
        dto.setBio(o.getBio());
        dto.setAvatarUrl(o.getAvatarUrl());
        dto.setRegistrationDate(o.getRegistrationDate());

        // Dogs
        List<DogDto> dogsDto = new ArrayList<>();
        if (o.getDogs() != null) {
            dogsDto = o.getDogs().stream().map(dog -> {
                DogDto d = new DogDto();
                d.setIdDog(dog.getIdDog());
                d.setName(dog.getName());
                d.setBreed(dog.getBreed() != null ? dog.getBreed().getName() : null);
                d.setBirthDate(dog.getBirthDate());
                d.setSex(dog.getSex());
                d.setWeight(dog.getWeight());
                d.setChipNumber(dog.getChipNumber());
                d.setPhotoUrl(dog.getPhotoUrl());
                return d;
            }).collect(Collectors.toList());
        }
        dto.setDogs(dogsDto);

        // Registrations
        List<Registration> allRegistrations = (o.getDogs() != null)
                ? o.getDogs().stream()
                .filter(dog -> dog.getRegistrations() != null)
                .flatMap(dog -> dog.getRegistrations().stream())
                .distinct()
                .toList()
                : new ArrayList<>();

        List<RegistrationDto> registrationsDto = allRegistrations.stream()
                .map(RegistrationMapper::toDto) // Use mapper instead of duplicating logic!
                .collect(Collectors.toList());
        dto.setRegistrations(registrationsDto);

        dto.setRole("OWNER");

        return dto;
    }
}
