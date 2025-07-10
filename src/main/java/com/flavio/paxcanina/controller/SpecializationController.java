package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.SpecializationDto;
import com.flavio.paxcanina.service.SpecializationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialization")
@CrossOrigin(origins = "http://localhost:4200")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping
    public List<SpecializationDto> list() {
        return specializationService.findAll();
    }

    @GetMapping("/{id}")
    public SpecializationDto get(@PathVariable Integer id) {
        return specializationService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SpecializationDto> create(@RequestBody SpecializationDto s) {
        return ResponseEntity.status(201).body(specializationService.create(s));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SpecializationDto update(@PathVariable Integer id, @RequestBody SpecializationDto s) {
        return specializationService.update(id, s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        specializationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
