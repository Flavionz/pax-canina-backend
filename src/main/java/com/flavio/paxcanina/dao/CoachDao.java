package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachDao extends JpaRepository<Coach, Integer> {
    Optional<Coach> findByEmail(String email);
}
