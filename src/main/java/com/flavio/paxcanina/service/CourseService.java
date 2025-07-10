package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.CourseDao;
import com.flavio.paxcanina.dao.SpecializationDao;
import com.flavio.paxcanina.dto.CourseDto;
import com.flavio.paxcanina.model.Course;
import com.flavio.paxcanina.model.Specialization;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseDao courseDao;
    private final SpecializationDao specializationDao;

    public CourseService(CourseDao courseDao, SpecializationDao specializationDao) {
        this.courseDao = courseDao;
        this.specializationDao = specializationDao;
    }

    /** Returns all courses with their main details and specializations */
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

    /** Creates a new course with its associated specializations */
    @Transactional
    public CourseDto create(CourseDto dto) {
        Course c = toEntity(dto);
        Course saved = courseDao.save(c);
        return toDto(saved);
    }

    /** Updates an existing course and its associated specializations */
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

        // Update specializations if provided
        if (dto.getSpecializationIds() != null) {
            Set<Specialization> specializations = new HashSet<>();
            for (Integer specId : dto.getSpecializationIds()) {
                Specialization s = specializationDao.findById(specId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialization not found with id " + specId));
                specializations.add(s);
            }
            existing.setSpecializations(specializations);
        }

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

    /**
     * Maps Course entity to CourseDto, including associated specializations (as IDs).
     */
    private CourseDto toDto(Course c) {
        List<Integer> specializationIds = null;
        if (c.getSpecializations() != null) {
            specializationIds = c.getSpecializations()
                    .stream()
                    .map(Specialization::getIdSpecialization)
                    .collect(Collectors.toList());
        }
        return new CourseDto(
                c.getIdCourse(),
                c.getName(),
                c.getDescription(),
                c.getStatus(),
                c.getImageUrl(),
                specializationIds
        );
    }

    /**
     * Maps CourseDto to Course entity, resolving associated specializations by their IDs.
     */
    private Course toEntity(CourseDto dto) {
        Course c = new Course();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setImageUrl(dto.getImageUrl());

        // Resolve and associate specializations if provided
        if (dto.getSpecializationIds() != null) {
            Set<Specialization> specializations = new HashSet<>();
            for (Integer specId : dto.getSpecializationIds()) {
                Specialization s = specializationDao.findById(specId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Specialization not found with id " + specId));
                specializations.add(s);
            }
            c.setSpecializations(specializations);
        }
        return c;
    }
}
