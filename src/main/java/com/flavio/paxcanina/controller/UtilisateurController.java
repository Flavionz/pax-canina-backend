package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.UtilisateurDto;
import com.flavio.paxcanina.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateur")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // GET /api/utilisateur
    @GetMapping
    public List<UtilisateurDto> list() {
        return utilisateurService.findAll();
    }

    // GET /api/utilisateur/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> get(@PathVariable Integer id) {
        UtilisateurDto utilisateur = utilisateurService.findById(id);
        if (utilisateur == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(utilisateur);
    }

    // POST /api/utilisateur (crea utente di qualsiasi tipo - lo specifichi via 'role' nel dto)
    @PostMapping
    public ResponseEntity<UtilisateurDto> create(@RequestBody UtilisateurDto dto) {
        UtilisateurDto created = utilisateurService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    // PUT /api/utilisateur/{id} (modifica dati)
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> update(@PathVariable Integer id, @RequestBody UtilisateurDto dto) {
        UtilisateurDto updated = utilisateurService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/utilisateur/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        utilisateurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // PROMUOVI UTENTE (proprietaire -> coach/admin ecc)
    @PostMapping("/{id}/promote")
    public ResponseEntity<UtilisateurDto> promote(
            @PathVariable Integer id,
            @RequestParam String role) // es: "COACH", "ADMIN"
    {
        UtilisateurDto promoted = utilisateurService.promoteToRole(id, role);
        if (promoted == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(promoted);
    }
}
