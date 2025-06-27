package com.flavio.paxcanina.service;

import com.flavio.paxcanina.model.Owner;
import java.util.List;

/**
 * Service interface for Owner-related operations.
 */
public interface OwnerService {

    /**
     * Register a new Owner.
     * @param owner Owner entity to register
     * @return the registered Owner entity
     */
    Owner register(Owner owner);

    /**
     * Get all owners.
     */
    List<Owner> findAll();

    /**
     * Get an owner by ID.
     */
    Owner findById(Integer id);

    /**
     * Get an owner by ID, including their dogs and registrations.
     */
    Owner findByIdWithDogsAndRegistrations(Integer id);

    /**
     * Update owner data.
     */
    Owner update(Integer id, Owner owner);

    /**
     * Delete an owner.
     */
    void delete(Integer id);
}
