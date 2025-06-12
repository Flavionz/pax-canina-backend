package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursDao extends JpaRepository<Cours, Integer> {}
