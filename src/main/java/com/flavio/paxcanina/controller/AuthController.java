package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.dto.LoginRequest;
import com.flavio.paxcanina.dto.OwnerRegistrationDto;
import com.flavio.paxcanina.model.Owner;
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

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(
            UserDao userDao,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register/owner")
    public ResponseEntity<?> registerOwner(@RequestBody @Valid OwnerRegistrationDto dto) {
        Owner owner = new Owner();
        owner.setLastName(dto.getLastName());
        owner.setFirstName(dto.getFirstName());
        owner.setEmail(dto.getEmail());
        owner.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        owner.setPhone(dto.getPhone());
        owner.setRegistrationDate(LocalDate.now());
        owner.setAddress(dto.getAddress());
        owner.setCity(dto.getCity());
        owner.setPostalCode(dto.getPostalCode());
        userDao.save(owner);
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
            String role = userDetails.getRole();
            String token = jwtService.generateToken(userDetails, role);
            return ResponseEntity.ok().body(new JwtResponse(token, role));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

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
