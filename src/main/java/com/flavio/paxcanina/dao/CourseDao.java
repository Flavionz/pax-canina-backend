package com.flavio.paxcanina.dao;

import com.flavio.paxcanina.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseDao extends JpaRepository<Course, Integer> {
    // Here you can add custom queries if needed in the future
}
