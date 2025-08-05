package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.CoachProfileDto;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * CoachController
 * ---------------
 * Handles all endpoints related to coach self-profile management.
 * Secured for users with ROLE_COACH.
 */
@RestController
@RequestMapping("/api/coach")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('COACH')")
public class CoachController {

    private final CoachService coachService;

    @Autowired
    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    /**
     * Get current coach profile (with specializations).
     * Always fetches from the database to ensure lazy relations are initialized.
     *
     * @param authentication Spring Security context
     * @return CoachProfileDto for the current coach
     */
    @GetMapping("/me")
    public ResponseEntity<CoachProfileDto> getMyProfile(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUser().getIdUser();

        Optional<Coach> coachOpt = coachService.findById(userId);
        if (coachOpt.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        CoachProfileDto dto = coachService.toProfileDto(coachOpt.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * Update current coach profile.
     *
     * @param dto            Profile data to update
     * @param authentication Spring Security context
     * @return Updated CoachProfileDto
     */
    @PutMapping("/me")
    public ResponseEntity<CoachProfileDto> updateMyProfile(
            @RequestBody CoachProfileDto dto,
            Authentication authentication
    ) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUser().getIdUser();

        Optional<Coach> coachOpt = coachService.findById(userId);
        if (coachOpt.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        Coach updated = coachService.updateProfile(coachOpt.get(), dto);
        CoachProfileDto response = coachService.toProfileDto(updated);
        response.setEmail(updated.getEmail());
        return ResponseEntity.ok(response);
    }
}
