package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.DogDao;
import com.flavio.paxcanina.dao.OwnerDao;
import com.flavio.paxcanina.dto.DogDto;
import com.flavio.paxcanina.model.Dog;
import com.flavio.paxcanina.model.Owner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DogService providing business logic for managing Dog entities.
 */
@Service
public class DogServiceImpl implements DogService {

    private final DogDao dogDao;
    private final OwnerDao ownerDao;

    public DogServiceImpl(DogDao dogDao, OwnerDao ownerDao) {
        this.dogDao = dogDao;
        this.ownerDao = ownerDao;
    }

    /**
     * Find all dogs belonging to a specific owner.
     */
    @Override
    public List<DogDto> findByOwnerId(Integer ownerId) {
        return dogDao.findByOwnerIdUser(ownerId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }
    @Override
    public Dog findById(Integer id) {
        return dogDao.findById(id).orElse(null);
    }
    /**
     * Create a new dog for a specific owner.
     */
    @Override
    public DogDto createDogForOwner(DogDto dto, Integer ownerId) {
        Owner owner = ownerDao.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        Dog dog = fromDto(dto);
        dog.setOwner(owner);
        Dog saved = dogDao.save(dog);
        return toDto(saved);
    }

    /**
     * Check if a dog is owned by a specific owner.
     */
    @Override
    public boolean isDogOwnedBy(Integer dogId, Integer ownerId) {
        return dogDao.findById(dogId)
                .map(dog -> dog.getOwner().getIdUser().equals(ownerId))
                .orElse(false);
    }

    /**
     * Update an existing dog's details.
     */
    @Override
    public DogDto updateDog(Integer dogId, DogDto dto) {
        Dog existing = dogDao.findById(dogId)
                .orElseThrow(() -> new IllegalArgumentException("Dog not found"));
        // Update only the editable fields
        existing.setName(dto.getName());
        // existing.setBreed(...); // Manage if you have a Breed object
        existing.setBirthDate(dto.getBirthDate());
        existing.setSex(dto.getSex());
        existing.setWeight(dto.getWeight());
        existing.setChipNumber(dto.getChipNumber());
        existing.setPhotoUrl(dto.getPhotoUrl());
        Dog saved = dogDao.save(existing);
        return toDto(saved);
    }

    /**
     * Delete a dog by its ID.
     */
    @Override
    public void deleteDog(Integer dogId) {
        dogDao.deleteById(dogId);
    }

    // --- Mapping methods ---

    private DogDto toDto(Dog d) {
        DogDto dto = new DogDto();
        dto.setIdDog(d.getIdDog());
        dto.setName(d.getName());
        dto.setBreed(d.getBreed() != null ? d.getBreed().getName() : null);
        dto.setBirthDate(d.getBirthDate());
        dto.setSex(d.getSex());                // <-- QUI!
        dto.setWeight(d.getWeight());
        dto.setChipNumber(d.getChipNumber());
        dto.setPhotoUrl(d.getPhotoUrl());
        return dto;
    }


    private Dog fromDto(DogDto dto) {
        Dog d = new Dog();
        d.setName(dto.getName());
        d.setSex(dto.getSex());
        d.setBirthDate(dto.getBirthDate());
        d.setWeight(dto.getWeight());
        d.setChipNumber(dto.getChipNumber());
        d.setPhotoUrl(dto.getPhotoUrl());
        return d;
    }
}
