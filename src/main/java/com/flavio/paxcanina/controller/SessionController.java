package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.SessionDto;
import com.flavio.paxcanina.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * SessionController
 * -----------------
 * REST API controller for managing training sessions (CRUD + queries).
 * Follows RESTful conventions and exposes all endpoints at /api/sessions.
 * - All GET endpoints are public (anyone can consult session data)
 * - Create/Update/Delete require ADMIN or COACH role
 */
@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * GET /api/sessions
     * List all sessions (public)
     */
    @GetMapping
    public ResponseEntity<List<SessionDto>> getAllSessions() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    /**
     * GET /api/sessions/{id}
     * Get a single session by ID (public)
     */
    @GetMapping("/{id}")
    public ResponseEntity<SessionDto> getSession(@PathVariable int id) {
        return ResponseEntity.ok(sessionService.findById(id));
    }

    /**
     * GET /api/sessions/by-course/{courseId}
     * List sessions for a specific course (public)
     */
    @GetMapping("/by-course/{courseId}")
    public ResponseEntity<List<SessionDto>> getSessionsByCourse(@PathVariable Integer courseId) {
        return ResponseEntity.ok(sessionService.findByCourseId(courseId));
    }

    /**
     * GET /api/sessions/by-date/{date}
     * List sessions on a specific date (format: yyyy-MM-dd, public)
     */
    @GetMapping("/by-date/{date}")
    public ResponseEntity<List<SessionDto>> getSessionsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(sessionService.findByDate(localDate));
    }

    /**
     * GET /api/sessions/by-date-range?start=yyyy-MM-dd&end=yyyy-MM-dd
     * List sessions between two dates (public)
     */
    @GetMapping("/by-date-range")
    public ResponseEntity<List<SessionDto>> getSessionsByDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(sessionService.findByDateBetween(startDate, endDate));
    }

    /**
     * POST /api/sessions
     * Create a new session (ADMIN or COACH only)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @PostMapping
    public ResponseEntity<SessionDto> createSession(@RequestBody SessionDto dto) {
        SessionDto created = sessionService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * PUT /api/sessions/{id}
     * Update an existing session (ADMIN or COACH only)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @PutMapping("/{id}")
    public ResponseEntity<SessionDto> updateSession(@PathVariable int id, @RequestBody SessionDto dto) {
        return ResponseEntity.ok(sessionService.update(id, dto));
    }

    /**
     * DELETE /api/sessions/{id}
     * Delete a session by ID (ADMIN or COACH only)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable int id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
