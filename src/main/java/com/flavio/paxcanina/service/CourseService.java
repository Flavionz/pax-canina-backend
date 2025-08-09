package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dao.CourseDao;
import com.flavio.paxcanina.dao.SpecializationDao;
import com.flavio.paxcanina.dto.CourseDto;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.model.Course;
import com.flavio.paxcanina.model.Specialization;
import com.flavio.paxcanina.security.AppUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CourseService
 * -------------
 * CRUD for Course + coach-scoped filtering logic.
 * Service keeps mapping responsibilities to avoid leaking JPA entities.
 */
@Service
public class CourseService {

    private final CourseDao courseDao;
    private final SpecializationDao specializationDao;
    private final CoachDao coachDao;

    public CourseService(CourseDao courseDao,
                         SpecializationDao specializationDao,
                         CoachDao coachDao) {
        this.courseDao = courseDao;
        this.specializationDao = specializationDao;
        this.coachDao = coachDao;
    }

    /* ==========================
       Read Operations
       ========================== */

    /** Returns all courses with their specialization ids. */
    @Transactional(readOnly = true)
    public List<CourseDto> findAll() {
        return courseDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /** Returns a course by ID or throws 404. */
    @Transactional(readOnly = true)
    public CourseDto findById(int id) {
        Course c = courseDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found with id " + id
                ));
        return toDto(c);
    }

    /**
     * Returns courses compatible with the current coach's specializations.
     * Keeps authorization/business rules server-side.
     */
    @Transactional(readOnly = true)
    public List<CourseDto> findForCoach(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails details)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "unauthenticated");
        }

        Integer coachId = details.getUser().getIdUser();
        Coach coach = coachDao.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "coach_not_found"));

        // Build the set of specialization IDs owned by the coach
        Set<Integer> coachSpecIds = (coach.getSpecializations() == null)
                ? Set.of()
                : coach.getSpecializations().stream()
                .map(Specialization::getIdSpecialization)
                .collect(Collectors.toSet());

        if (coachSpecIds.isEmpty()) {
            // No specializations -> no eligible courses
            return Collections.emptyList();
        }

        // Filter courses that share at least one specialization with the coach
        return courseDao.findAll().stream()
                .filter(c -> c.getSpecializations() != null &&
                        c.getSpecializations().stream()
                                .map(Specialization::getIdSpecialization)
                                .anyMatch(coachSpecIds::contains))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /* ==========================
       Write Operations (Admin)
       ========================== */

    /** Creates a new course with its associated specializations. */
    @Transactional
    public CourseDto create(CourseDto dto) {
        Course c = toEntity(dto);
        Course saved = courseDao.save(c);
        return toDto(saved);
    }

    /** Updates an existing course and its associated specializations. */
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

        // Replace specializations if a list is provided (null = leave as is)
        if (dto.getSpecializationIds() != null) {
            Set<Specialization> specializations = new HashSet<>();
            for (Integer specId : dto.getSpecializationIds()) {
                Specialization s = specializationDao.findById(specId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Specialization not found with id " + specId));
                specializations.add(s);
            }
            existing.setSpecializations(specializations);
        }

        Course saved = courseDao.save(existing);
        return toDto(saved);
    }

    /** Deletes a course by ID. */
    @Transactional
    public void delete(int id) {
        Course existing = courseDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found with id " + id
                ));
        courseDao.delete(existing);
    }

    /* ==========================
       Mapping helpers
       ========================== */

    /** Maps Course entity to CourseDto (specializations as IDs). */
    private CourseDto toDto(Course c) {
        List<Integer> specializationIds = (c.getSpecializations() == null)
                ? null
                : c.getSpecializations().stream()
                .map(Specialization::getIdSpecialization)
                .collect(Collectors.toList());

        return new CourseDto(
                c.getIdCourse(),
                c.getName(),
                c.getDescription(),
                c.getStatus(),
                c.getImageUrl(),
                specializationIds
        );
    }

    /** Maps CourseDto to Course entity, resolving specialization IDs. */
    private Course toEntity(CourseDto dto) {
        Course c = new Course();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        c.setStatus(dto.getStatus());
        c.setImageUrl(dto.getImageUrl());

        if (dto.getSpecializationIds() != null) {
            Set<Specialization> specializations = new HashSet<>();
            for (Integer specId : dto.getSpecializationIds()) {
                Specialization s = specializationDao.findById(specId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Specialization not found with id " + specId));
                specializations.add(s);
            }
            c.setSpecializations(specializations);
        }

        return c;
    }
}
