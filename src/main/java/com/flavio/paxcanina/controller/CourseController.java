package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.CourseDto;
import com.flavio.paxcanina.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CourseController
 * ----------------
 * Public read access to courses, admin-only write.
 * Adds a coach-scoped endpoint that returns only courses matching
 * the current coach's specializations.
 */
@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /* ==========================
       Public read endpoints
       ========================== */

    /** List all courses (with their specialization ids). */
    @GetMapping
    public ResponseEntity<List<CourseDto>> list() {
        return ResponseEntity.ok(courseService.findAll());
    }

    /** Get a single course by id. */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@PathVariable int id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    /**
     * Coach-only convenience endpoint:
     * returns courses compatible with the current coach's specializations.
     * This keeps business rules on the server and simplifies the UI logic.
     */
    @GetMapping("/for-coach/me")
    @PreAuthorize("hasRole('COACH')")
    public ResponseEntity<List<CourseDto>> getForCurrentCoach(Authentication authentication) {
        return ResponseEntity.ok(courseService.findForCoach(authentication));
    }

    /* ==========================
       Admin write endpoints
       ========================== */

    /** Create a new course (admin only). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDto> create(@RequestBody CourseDto dto) {
        CourseDto created = courseService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    /** Update an existing course (admin only). */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseDto> update(@PathVariable int id, @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    /** Delete a course (admin only). */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
