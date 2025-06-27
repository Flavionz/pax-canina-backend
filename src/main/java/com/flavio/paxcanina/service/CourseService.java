package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.CourseDao;
import com.flavio.paxcanina.dto.CourseDto;
import com.flavio.paxcanina.model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseDao courseDao;

    public CourseService(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    /** Returns all courses (for admin/coach/owner) */
    public List<CourseDto> findAll() {
        return courseDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Returns a course by ID or throws 404 */
    public CourseDto findById(int id) {
        Course c = courseDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found with id " + id
                ));
        return toDto(c);
    }

    /** Creates a new course */
    @Transactional
    public CourseDto create(CourseDto dto) {
        Course c = toEntity(dto);
        Course saved = courseDao.save(c);
        return toDto(saved);
    }

    /** Updates an existing course */
    @Transactional
    public CourseDto update(int id, CourseDto dto) {
        Course existing = courseDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found with id " + id
                ));
        // Copy modifiable fields
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setImageUrl(dto.getImageUrl());
        Course saved = courseDao.save(existing);
        return toDto(saved);
    }

    /** Deletes a course by ID */
    @Transactional
    public void delete(int id) {
        Course existing = courseDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found with id " + id
                ));
        courseDao.delete(existing);
    }

    // ————————
    // Manual mappers: Entity <-> DTO
    private CourseDto toDto(Course c) {
        return new CourseDto(
                c.getIdCourse(),
                c.getName(),
                c.getDescription(),
                c.getStatus(),
                c.getImageUrl()
        );
    }

    private Course toEntity(CourseDto dto) {
        Course c = new Course();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setImageUrl(dto.getImageUrl());
        return c;
    }
}
