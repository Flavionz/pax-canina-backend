package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.model.Proprietaire;
import com.flavio.paxcanina.service.ProprietaireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proprietaires")
public class ProprietaireController {

    private final ProprietaireService proprietaireService;

    public ProprietaireController(ProprietaireService proprietaireService) {
        this.proprietaireService = proprietaireService;
    }

    @PostMapping("/register")
    public ResponseEntity<Proprietaire> register(@RequestBody Proprietaire proprietaire) {
        try {
            Proprietaire saved = proprietaireService.register(proprietaire);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public List<Proprietaire> getAll() {
        return proprietaireService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietaire> getById(@PathVariable Integer id) {
        Proprietaire proprietaire = proprietaireService.findById(id);
        if (proprietaire == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(proprietaire);
    }
}
