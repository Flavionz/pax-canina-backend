package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @GetMapping
    public List<UserDto> list() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Integer id) {
        UserDto user = userService.findById(id);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        try {
            UserDto created = userService.create(dto);
            return ResponseEntity.status(201).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable Integer id, @RequestBody UserDto dto) {
        UserDto updated = userService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/full-update")
    public ResponseEntity<UserDto> fullUpdate(
            @PathVariable Integer id,
            @RequestBody UserDto dto
    ) {
        try {
            UserDto updated = userService.promoteAndUpdate(id, dto);
            log.info("[UserController] Ritorno fullUpdate: {}", updated);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("[UserController] Errore nel fullUpdate: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
