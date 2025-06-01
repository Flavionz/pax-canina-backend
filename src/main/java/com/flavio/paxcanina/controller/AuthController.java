package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.UtilisateurDao;
import com.flavio.paxcanina.dto.LoginRequest;
import com.flavio.paxcanina.dto.ProprietaireRegistrationDTO;
import com.flavio.paxcanina.model.Proprietaire;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.JwtService;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UtilisateurDao utilisateurDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(
            UtilisateurDao utilisateurDao,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.utilisateurDao = utilisateurDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // REGISTRA PROPRIETAIRE
    @PostMapping("/register/proprietaire")
    public ResponseEntity<?> registerProprietaire(@RequestBody @Valid ProprietaireRegistrationDTO dto) {
        Proprietaire p = new Proprietaire();
        p.setNom(dto.getNom());
        p.setPrenom(dto.getPrenom());
        p.setEmail(dto.getEmail());
        p.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        p.setTelephone(dto.getTelephone());
        p.setDateInscription(LocalDate.now());
        p.setAdresse(dto.getAdresse());
        p.setVille(dto.getVille());
        p.setCodePostal(dto.getCodePostal());
        utilisateurDao.save(p);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
            String role = userDetails.getRole(); // Fai in modo che sia estratto davvero dalle tabelle!
            String token = jwtService.generateToken(userDetails, role);
            return ResponseEntity.ok().body(new JwtResponse(token, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // DTO per la risposta JWT
    @Getter
    public static class JwtResponse {
        public String token;
        public String role;
        public JwtResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }
    }
}
