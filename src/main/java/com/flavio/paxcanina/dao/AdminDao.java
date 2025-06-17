package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer> {
}