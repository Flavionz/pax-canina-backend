package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /api/user
    @GetMapping
    public List<UserDto> list() {
        return userService.findAll();
    }

    // GET /api/user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Integer id) {
        UserDto user = userService.findById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    // POST /api/user (create any user type - specify via 'role' in dto)
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        UserDto created = userService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    // PUT /api/user/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Integer id, @RequestBody UserDto dto) {
        UserDto updated = userService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/user/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PROMOTE USER (owner -> coach/admin etc)
    @PostMapping("/{id}/promote")
    public ResponseEntity<UserDto> promote(
            @PathVariable Integer id,
            @RequestParam String role // ex: "COACH", "ADMIN"
    ) {
        UserDto promoted = userService.promoteToRole(id, role);
        if (promoted == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(promoted);
    }
}
