package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.model.Breed;
import com.flavio.paxcanina.repository.BreedRepository;
import org.springframework.data.domain.Sort;
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

    public record BreedLiteDto(Integer idBreed, String name) {}

    @GetMapping("/lite")
    public List<BreedLiteDto> getAllBreedsLite() {
        return breedRepository.findAll(Sort.by("name"))
                .stream()
                .map(b -> new BreedLiteDto(b.getIdBreed(), b.getName()))
                .toList();
    }
}
