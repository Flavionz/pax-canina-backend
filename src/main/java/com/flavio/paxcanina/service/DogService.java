package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dto.DogDto;
import java.util.List;

/**
 * Service interface for managing Dog entities.
 * Provides CRUD operations and business logic related to dogs and their owners.
 */
public interface DogService {

    /**
     * Find all dogs belonging to a specific owner.
     * @param ownerId the unique identifier of the owner
     * @return list of DogDto representing the owner's dogs
     */
    List<DogDto> findByOwnerId(Integer ownerId);

    /**
     * Create a new dog associated with a specific owner.
     * @param dto the data transfer object containing dog details
     * @param ownerId the unique identifier of the owner
     * @return DogDto of the newly created dog
     */
    DogDto createDogForOwner(DogDto dto, Integer ownerId);

    /**
     * Check if a dog belongs to a given owner.
     * @param dogId the unique identifier of the dog
     * @param ownerId the unique identifier of the owner
     * @return true if the dog belongs to the owner, false otherwise
     */
    boolean isDogOwnedBy(Integer dogId, Integer ownerId);

    /**
     * Update the details of an existing dog.
     * @param dogId the unique identifier of the dog to update
     * @param dto the data transfer object containing updated details
     * @return DogDto of the updated dog
     */
    DogDto updateDog(Integer dogId, DogDto dto);

    /**
     * Delete a dog by its ID.
     * @param dogId the unique identifier of the dog to delete
     */
    void deleteDog(Integer dogId);
}
