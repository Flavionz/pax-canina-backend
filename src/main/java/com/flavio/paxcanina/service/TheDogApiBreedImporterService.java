package com.flavio.paxcanina.service;

import com.flavio.paxcanina.model.Breed;
import com.flavio.paxcanina.repository.BreedRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TheDogApiBreedImporterService implements CommandLineRunner {

    private final BreedRepository breedRepository;
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;

    public TheDogApiBreedImporterService(
            BreedRepository breedRepository,
            @Value("${thedogapi.api-key}") String apiKey,
            @Value("${thedogapi.base-url}") String baseUrl
    ) {
        this.breedRepository = breedRepository;
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    @Override
    public void run(String... args) {
        // Scarica le razze solo se il database è vuoto!
        if (breedRepository.count() > 0) return;
        importBreedsFromApi();
    }

    public void importBreedsFromApi() {
        String url = baseUrl + "/breeds";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, List.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> breeds = response.getBody();

            for (Map<String, Object> breedData : breeds) {
                String name = (String) breedData.get("name");
                if (!breedRepository.existsByName(name)) {
                    Breed breed = new Breed();
                    breed.setName(name);
                    breedRepository.save(breed);
                }
            }
            System.out.println("Razze importate da TheDogAPI: " + breeds.size());
        } else {
            System.err.println("Errore nell'importare le razze da TheDogAPI");
        }
    }
}
