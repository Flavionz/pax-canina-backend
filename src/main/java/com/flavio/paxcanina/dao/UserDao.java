package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    // New: find user by email AND emailVerified status (true or false)
    Optional<User> findByEmailAndEmailVerified(String email, boolean emailVerified);
}
