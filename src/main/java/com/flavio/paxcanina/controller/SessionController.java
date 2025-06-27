package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.SessionDto;
import com.flavio.paxcanina.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/session")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public ResponseEntity<List<SessionDto>> list() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDto> get(@PathVariable int id) {
        return ResponseEntity.ok(sessionService.findById(id));
    }

    @GetMapping("/by-course/{idCours}")
    public ResponseEntity<List<SessionDto>> byCourse(@PathVariable Integer idCours) {
        return ResponseEntity.ok(sessionService.findByCourseId(idCours));
    }

    @GetMapping("/by-date/{date}")
    public ResponseEntity<List<SessionDto>> byDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(sessionService.findByDate(localDate));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<SessionDto>> byDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(sessionService.findByDateBetween(startDate, endDate));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @PostMapping
    public ResponseEntity<SessionDto> create(@RequestBody SessionDto dto) {
        SessionDto created = sessionService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @PutMapping("/{id}")
    public ResponseEntity<SessionDto> update(@PathVariable int id, @RequestBody SessionDto dto) {
        return ResponseEntity.ok(sessionService.update(id, dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'COACH')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
