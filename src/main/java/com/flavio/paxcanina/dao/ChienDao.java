package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Chien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChienDao extends JpaRepository<Chien, Integer> {
    List<Chien> findByProprietaireIdUtilisateur(Integer idProprietaire);
}
