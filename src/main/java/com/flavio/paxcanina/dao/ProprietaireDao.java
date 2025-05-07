package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietaireDao extends JpaRepository<Proprietaire, Integer> {
    boolean existsByEmail(String email);
    Proprietaire findByEmail(String email);
}
