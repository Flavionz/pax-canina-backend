package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.RegistrationDto;
import com.flavio.paxcanina.dto.RegistrationRequestDto;
import com.flavio.paxcanina.exception.BusinessException;
import com.flavio.paxcanina.exception.NotFoundException;
import com.flavio.paxcanina.mapper.RegistrationMapper;
import com.flavio.paxcanina.model.Owner;
import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * RegistrationController
 * ---------------------
 * Thin REST controller delegating domain rules to the service layer:
 * - Ownership, duplicate, capacity, age eligibility are enforced server-side in RegistrationService.
 * - Consistent JSON error payloads via @ExceptionHandler.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Registers an owner's dog to a session.
     * Path keeps your existing route for frontend compatibility:
     *   POST /api/sessions/{sessionId}/registration
     *
     * Body:  { "dogId": number }
     * Auth:  Owner only (enforced here for role, domain rules in service)
     *
     * Returns: RegistrationDto (flat), 201 Created on success.
     */
    @PostMapping("/{sessionId}/registration")
    public ResponseEntity<?> registerDogToSession(
            @PathVariable Integer sessionId,
            @RequestBody RegistrationRequestDto request,
            Authentication authentication
    ) {
        // Extract authenticated user
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        // Only owners can register dogs
        if (!(userDetails.getUser() instanceof Owner owner)) {
            // Security error (not a business rule)
            return ResponseEntity.status(403).body(Map.of(
                    "error", "FORBIDDEN",
                    "message", "Only owners can register dogs"
            ));
        }

        // Delegate the full use case to the service (ownership is re-checked inside)
        Registration reg = registrationService.registerDogToSession(
                owner.getIdUser(),
                request.getDogId(),
                sessionId
        );

        // Map to flat DTO (no cycles)
        RegistrationDto dto = RegistrationMapper.toDto(reg);

        // 201 Created makes sense for a new registration
        return ResponseEntity.status(201).body(dto);
    }

    /* ------------------------------
       Unified error handling
       ------------------------------ */

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusiness(BusinessException ex) {
        // e.g. 422 for domain rule violations, 409 if you ever map conflicts, etc.
        return ResponseEntity.status(ex.getStatus()).body(
                Map.of(
                        "error", ex.getErrorCode(),
                        "message", ex.getMessage(),
                        "details", ex.getDetails()
                )
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(
                Map.of(
                        "error", ex.getErrorCode(),
                        "message", "Resource not found"
                )
        );
    }
}
