package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationDao extends JpaRepository<Specialization, Integer> {
    boolean existsByName(String name);
}
