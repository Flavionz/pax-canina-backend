package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionDao extends JpaRepository<Session, Integer> {
    List<Session> findByCourse_IdCourse(Integer idCourse);
    List<Session> findByDate(LocalDate date);
    List<Session> findByDateBetween(LocalDate start, LocalDate end);
}




