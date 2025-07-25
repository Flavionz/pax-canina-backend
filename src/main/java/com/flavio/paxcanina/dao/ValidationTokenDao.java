package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidationTokenDao extends JpaRepository<ValidationToken, Long> {
    Optional<ValidationToken> findByToken(String token);
    void deleteByToken(String token);
}
