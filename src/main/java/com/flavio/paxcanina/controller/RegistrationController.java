package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.RegistrationRequestDto;
import com.flavio.paxcanina.model.*;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.DogService;
import com.flavio.paxcanina.service.RegistrationService;
import com.flavio.paxcanina.service.SessionService;
import com.flavio.paxcanina.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DogService dogService;
    private final SessionService sessionService;

    public RegistrationController(
            RegistrationService registrationService,
            DogService dogService,
            SessionService sessionService,
            OwnerService ownerService
    ) {
        this.registrationService = registrationService;
        this.dogService = dogService;
        this.sessionService = sessionService;

    }

    // Iscrivi un cane a una sessione
    @PostMapping("/{sessionId}/registration")
    public ResponseEntity<?> registerDogToSession(
            @PathVariable Integer sessionId,
            @RequestBody RegistrationRequestDto request,
            Authentication authentication
    ) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();

        if (!(user instanceof Owner owner)) {
            return ResponseEntity.status(403).body("Only owners can register dogs");
        }

        // Controllo che il cane appartenga all'owner loggato
        Dog dog = dogService.findById(request.getDogId());
        if (dog == null || !dog.getOwner().getIdUser().equals(owner.getIdUser())) {
            return ResponseEntity.status(403).body("Dog does not belong to this owner");
        }

        Session session = sessionService.findEntityById(sessionId);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        // Verifica se la registrazione già esiste (unique constraint id_session+id_dog)
        if (registrationService.existsBySessionAndDog(session, dog)) {
            return ResponseEntity.status(409).body("Dog already registered for this session");
        }

        // Crea la registrazione
        Registration reg = new Registration();
        reg.setDog(dog);
        reg.setSession(session);
        reg.setRegistrationDate(LocalDate.now());
        reg.setStatus("REGISTERED");
        Registration saved = registrationService.save(reg);

        return ResponseEntity.status(201).body(saved);
    }
}
