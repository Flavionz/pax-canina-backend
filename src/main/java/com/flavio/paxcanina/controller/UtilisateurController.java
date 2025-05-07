package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dao.UtilisateurDao;
import com.flavio.paxcanina.model.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
@CrossOrigin
public class UtilisateurController {

    private final UtilisateurDao utilisateurDao;

    public UtilisateurController(UtilisateurDao utilisateurDao) {
        this.utilisateurDao = utilisateurDao;
    }

    @GetMapping
    public List<Utilisateur> getAll() {
        return utilisateurDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getById(@PathVariable Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurDao.findById(id);
        return utilisateur.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Utilisateur> create(@RequestBody Utilisateur utilisateur) {
        Utilisateur saved = utilisateurDao.save(utilisateur);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
