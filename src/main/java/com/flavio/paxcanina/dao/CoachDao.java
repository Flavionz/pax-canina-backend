package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Coach;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoachDao extends JpaRepository<Coach, Integer> {

    @EntityGraph(attributePaths = {"specializations"})
    Optional<Coach> findByEmail(String email);

    @EntityGraph(attributePaths = {"specializations"})
    Optional<Coach> findById(Integer id);
}
