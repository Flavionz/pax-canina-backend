package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.UtilisateurDao;
import com.flavio.paxcanina.dto.LoginRequest;
import com.flavio.paxcanina.model.Utilisateur;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.JwtService; // <-- da implementare o adattare
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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Adatta se serve
public class AuthController {

    private final UtilisateurDao utilisateurDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService; // Da implementare

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

    // REGISTRAZIONE
    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody @Valid Utilisateur utilisateur) {
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        Utilisateur saved = utilisateurDao.save(utilisateur);
        saved.setPassword(null); // Mai restituire la password!
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
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
            String token = jwtService.generateToken(userDetails);
            return ResponseEntity.ok().body(new JwtResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    // DTO per la risposta JWT
    @Getter
    public static class JwtResponse {
        public String token;
        public JwtResponse(String token) { this.token = token; }
    }
}
