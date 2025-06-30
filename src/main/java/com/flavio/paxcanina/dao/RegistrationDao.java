package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Registration;
import com.flavio.paxcanina.model.Session;
import com.flavio.paxcanina.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationDao extends JpaRepository<Registration, Integer> {
    boolean existsBySessionAndDog(Session session, Dog dog);
}
