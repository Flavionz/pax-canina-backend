package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.SessionDao;
import com.flavio.paxcanina.dao.CourseDao;
import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dao.AgeGroupDao;
import com.flavio.paxcanina.dto.SessionDto;
import com.flavio.paxcanina.model.Level;
import com.flavio.paxcanina.model.Session;
import com.flavio.paxcanina.model.Course;
import com.flavio.paxcanina.model.Coach;
import com.flavio.paxcanina.model.AgeGroup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    private final SessionDao sessionDao;
    private final CourseDao courseDao;
    private final CoachDao coachDao;
    private final AgeGroupDao ageGroupDao;

    public SessionService(
            SessionDao sessionDao,
            CourseDao courseDao,
            CoachDao coachDao,
            AgeGroupDao ageGroupDao
    ) {
        this.sessionDao = sessionDao;
        this.courseDao = courseDao;
        this.coachDao = coachDao;
        this.ageGroupDao = ageGroupDao;
    }

    public List<SessionDto> findAll() {
        return sessionDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public SessionDto findById(int id) {
        Session s = sessionDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        return toDto(s);
    }

    public List<SessionDto> findByCourseId(Integer courseId) {
        return sessionDao.findByCourse_IdCourse(courseId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<SessionDto> findByDate(java.time.LocalDate date) {
        return sessionDao.findByDate(date).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<SessionDto> findByDateBetween(java.time.LocalDate start, java.time.LocalDate end) {
        return sessionDao.findByDateBetween(start, end).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public SessionDto create(SessionDto dto) {
        Session s = toEntity(dto);
        Session saved = sessionDao.save(s);
        return toDto(saved);
    }

    @Transactional
    public SessionDto update(int id, SessionDto dto) {
        Session existing = sessionDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));

        // Update simple fields
        existing.setDate(dto.getDate());
        existing.setLevel(dto.getLevel() != null ? Level.valueOf(dto.getLevel()) : null);
        existing.setStartTime(dto.getStartTime() != null ? LocalTime.parse(dto.getStartTime()) : null);
        existing.setEndTime(dto.getEndTime() != null ? LocalTime.parse(dto.getEndTime()) : null);
        existing.setMaxCapacity(dto.getMaxCapacity());
        existing.setDescription(dto.getDescription());
        existing.setLocation(dto.getLocation());
        existing.setImageUrl(dto.getImageUrl());

        // Update relations (if values provided)
        if (dto.getCourseId() != null) {
            Course course = courseDao.findById(dto.getCourseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
            existing.setCourse(course);
        }
        if (dto.getCoachId() != null) {
            Coach coach = coachDao.findById(dto.getCoachId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));
            existing.setCoach(coach);
        }
        if (dto.getAgeGroupId() != null) {
            AgeGroup ageGroup = ageGroupDao.findById(dto.getAgeGroupId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Age group not found"));
            existing.setAgeGroup(ageGroup);
        }

        Session saved = sessionDao.save(existing);
        return toDto(saved);
    }
    public Session findEntityById(Integer id) {
        return sessionDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
    }

    @Transactional
    public void delete(int id) {
        Session existing = sessionDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        sessionDao.delete(existing);
    }

    // --- Mapping methods ---

    private SessionDto toDto(Session s) {
        SessionDto dto = new SessionDto();
        dto.setIdSession(s.getIdSession());
        dto.setDate(s.getDate());
        dto.setLevel(s.getLevel() != null ? s.getLevel().name() : null);
        dto.setStartTime(s.getStartTime() != null ? s.getStartTime().toString() : null);
        dto.setEndTime(s.getEndTime() != null ? s.getEndTime().toString() : null);
        dto.setMaxCapacity(s.getMaxCapacity());
        dto.setDescription(s.getDescription());
        dto.setLocation(s.getLocation());
        dto.setImageUrl(s.getImageUrl());

        // Course
        if (s.getCourse() != null) {
            dto.setCourseId(s.getCourse().getIdCourse());
            dto.setCourseName(s.getCourse().getName());
        }
        // Coach
        if (s.getCoach() != null) {
            dto.setCoachId(s.getCoach().getIdUser());
            dto.setCoachLastName(s.getCoach().getLastName());
            dto.setCoachFirstName(s.getCoach().getFirstName());
        }
        // Age group
        if (s.getAgeGroup() != null) {
            dto.setAgeGroupId(s.getAgeGroup().getIdAgeGroup());
            dto.setAgeGroupName(s.getAgeGroup().getName());
            dto.setMinAge(s.getAgeGroup().getMinAge());
            dto.setMaxAge(s.getAgeGroup().getMaxAge());
        }
        // Registrations & Status
        if (s.getRegistrations() != null) {
            dto.setRegistrationsCount(s.getRegistrations().size());
            dto.setStatus(
                    s.getMaxCapacity() != null && s.getRegistrations().size() >= s.getMaxCapacity()
                            ? "full" : "available"
            );
        }
        return dto;
    }

    private Session toEntity(SessionDto dto) {
        Session s = new Session();
        s.setIdSession(dto.getIdSession());
        s.setDate(dto.getDate());
        s.setLevel(dto.getLevel() != null ? Level.valueOf(dto.getLevel()) : null);
        s.setStartTime(dto.getStartTime() != null ? LocalTime.parse(dto.getStartTime()) : null);
        s.setEndTime(dto.getEndTime() != null ? LocalTime.parse(dto.getEndTime()) : null);
        s.setMaxCapacity(dto.getMaxCapacity());
        s.setDescription(dto.getDescription());
        s.setLocation(dto.getLocation());
        s.setImageUrl(dto.getImageUrl());
        // Set Course
        if (dto.getCourseId() != null) {
            Course course = courseDao.findById(dto.getCourseId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));
            s.setCourse(course);
        }
        // Set Coach
        if (dto.getCoachId() != null) {
            Coach coach = coachDao.findById(dto.getCoachId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach not found"));
            s.setCoach(coach);
        }
        // Set Age Group
        if (dto.getAgeGroupId() != null) {
            AgeGroup ageGroup = ageGroupDao.findById(dto.getAgeGroupId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Age group not found"));
            s.setAgeGroup(ageGroup);
        }
        return s;
    }
}
