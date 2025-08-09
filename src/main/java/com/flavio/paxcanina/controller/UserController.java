package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> list() { return userService.findAll(); }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countUsers() {
        return ResponseEntity.ok(Map.of("count", userService.countUsers()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Integer id) {
        UserDto user = userService.findById(id);
        return (user == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        UserDto created = userService.create(dto);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> update(@PathVariable Integer id, @Valid @RequestBody UserDto dto) {
        UserDto updated = userService.update(id, dto);
        return (updated == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}/full-update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> fullUpdate(@PathVariable Integer id, @Valid @RequestBody UserDto dto) {
        UserDto updated = userService.promoteAndUpdate(id, dto);
        return (updated == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    // --- Admin actions ---

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Integer id) {
        return userService.deactivateUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Integer id) {
        return userService.activateUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/anonymize")
    public ResponseEntity<Void> anonymize(@PathVariable Integer id) {
        return userService.anonymizeUser(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/resend-verification")
    public ResponseEntity<Void> resendVerification(@PathVariable Integer id) {
        return userService.resendVerificationEmail(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
