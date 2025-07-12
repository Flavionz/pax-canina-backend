package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/users
    @GetMapping
    public List<UserDto> list() {
        return userService.findAll();
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Integer id) {
        UserDto user = userService.findById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // POST /api/users
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        try {
            UserDto created = userService.create(dto);
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException e) {
            // Gestione errore email già esistente o altro
            return ResponseEntity.badRequest().body(null);
        }
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Integer id, @RequestBody UserDto dto) {
        UserDto updated = userService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Promote a user to a new role (ADMIN or COACH).
     * Esempio: POST /api/users/5/promote?role=COACH
     */
    @PostMapping("/{id}/promote")
    public ResponseEntity<UserDto> promote(
            @PathVariable Integer id,
            @RequestParam String role // "ADMIN" oppure "COACH"
    ) {
        try {
            UserDto promoted = userService.promoteToRole(id, role);
            if (promoted == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(promoted);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
