package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DogDao extends JpaRepository<Dog, Integer> {
    List<Dog> findByOwnerIdUser(Integer idUser);

}
