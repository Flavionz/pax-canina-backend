package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Proprietaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProprietaireDao extends JpaRepository<Proprietaire, Integer> {

    @Query("""
        SELECT p FROM Proprietaire p
        LEFT JOIN FETCH p.chiens c
        LEFT JOIN FETCH c.inscriptions i
        WHERE p.idUtilisateur = :id
    """)
    Optional<Proprietaire> findByIdWithChiensAndInscriptions(@Param("id") Integer id);

    boolean existsByEmail(String email);
}
