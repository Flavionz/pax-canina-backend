package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.CoursDto;
import com.flavio.paxcanina.service.CoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@CrossOrigin(origins = "http://localhost:4200")
public class CoursController {

    private final CoursService coursService;

    public CoursController(CoursService coursService) {
        this.coursService = coursService;
    }

    // tutti possono leggere
    @GetMapping
    public ResponseEntity<List<CoursDto>> list() {
        return ResponseEntity.ok(coursService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursDto> get(@PathVariable int id) {
        return ResponseEntity.ok(coursService.findById(id));
    }

    // Solo ADMIN può creare/modificare/cancellare
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CoursDto> create(@RequestBody CoursDto dto) {
        CoursDto created = coursService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CoursDto> update(@PathVariable int id,
                                           @RequestBody CoursDto dto) {
        return ResponseEntity.ok(coursService.update(id, dto));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
