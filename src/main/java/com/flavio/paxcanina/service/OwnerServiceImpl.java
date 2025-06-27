package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.OwnerDao;
import com.flavio.paxcanina.model.Owner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of OwnerService interface.
 */
@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDao ownerDao;
    private final PasswordEncoder passwordEncoder;

    public OwnerServiceImpl(OwnerDao ownerDao, PasswordEncoder passwordEncoder) {
        this.ownerDao = ownerDao;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new owner. Throws exception if email already exists.
     */
    @Override
    public Owner register(Owner owner) {
        if (ownerDao.existsByEmail(owner.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        owner.setPasswordHash(passwordEncoder.encode(owner.getPasswordHash()));
        owner.setRegistrationDate(LocalDate.now());
        owner.setLastLogin(LocalDateTime.now());
        return ownerDao.save(owner);
    }

    /**
     * Get all owners.
     */
    @Override
    public List<Owner> findAll() {
        return ownerDao.findAll();
    }

    /**
     * Get an owner by ID.
     */
    @Override
    public Owner findById(Integer id) {
        return ownerDao.findById(id).orElse(null);
    }

    /**
     * Get an owner by ID including dogs and registrations.
     */
    @Override
    public Owner findByIdWithDogsAndRegistrations(Integer id) {
        return ownerDao.findByIdWithDogsAndRegistrations(id).orElse(null);
    }

    /**
     * Update owner info (does NOT update password).
     */
    @Override
    public Owner update(Integer id, Owner owner) {
        Owner existing = ownerDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
        existing.setLastName(owner.getLastName());
        existing.setFirstName(owner.getFirstName());
        existing.setEmail(owner.getEmail());
        existing.setPhone(owner.getPhone());
        existing.setAddress(owner.getAddress());
        existing.setCity(owner.getCity());
        existing.setPostalCode(owner.getPostalCode());
        existing.setBio(owner.getBio());
        existing.setAvatarUrl(owner.getAvatarUrl());
        // Do NOT update password here!
        return ownerDao.save(existing);
    }

    /**
     * Delete an owner.
     */
    @Override
    public void delete(Integer id) {
        ownerDao.deleteById(id);
    }
}
