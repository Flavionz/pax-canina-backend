package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.model.Breed;
import com.flavio.paxcanina.repository.BreedRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/breeds")
@CrossOrigin(origins = "http://localhost:4200")
public class BreedController {

    private final BreedRepository breedRepository;

    public BreedController(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    @GetMapping
    public List<Breed> getAllBreeds() {
        return breedRepository.findAll();
    }
}
