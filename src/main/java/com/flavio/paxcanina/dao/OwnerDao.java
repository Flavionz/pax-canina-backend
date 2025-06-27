package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerDao extends JpaRepository<Owner, Integer> {

    @Query("""
        SELECT o FROM Owner o
        LEFT JOIN FETCH o.dogs d
        LEFT JOIN FETCH d.registrations r
        WHERE o.idUser = :id
    """)
    Optional<Owner> findByIdWithDogsAndRegistrations(@Param("id") Integer id);

    boolean existsByEmail(String email);
}
