package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * OwnerDao
 * --------
 * Repository for Owner with a fetch-graph query that loads:
 *  - dogs
 *  - each dog's breed
 *  - each dog's registrations
 *  - each registration's session
 *
 * Using DISTINCT avoids row-duplication caused by JOIN FETCH on collections.
 */
@Repository
public interface OwnerDao extends JpaRepository<Owner, Integer> {

    @Query("""
           select distinct o
           from Owner o
           left join fetch o.dogs d
           left join fetch d.breed
           left join fetch d.registrations r
           left join fetch r.session s
           where o.idUser = :id
           """)
    Optional<Owner> findByIdWithDogsAndRegistrations(@Param("id") Integer id);


    boolean existsByEmail(String email);
}




