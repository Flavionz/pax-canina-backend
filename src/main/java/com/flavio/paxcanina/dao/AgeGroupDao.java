package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgeGroupDao extends JpaRepository<AgeGroup, Integer> {
}
