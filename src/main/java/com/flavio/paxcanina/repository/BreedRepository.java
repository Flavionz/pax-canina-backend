package com.flavio.paxcanina.repository;

import com.flavio.paxcanina.model.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Integer> {
    boolean existsByName(String name);
}
