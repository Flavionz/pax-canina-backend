package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.dto.CourseDto;
import com.flavio.paxcanina.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Everyone can read courses
    @GetMapping
    public ResponseEntity<List<CourseDto>> list() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> get(@PathVariable int id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    // Only ADMIN can create/update/delete courses
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CourseDto> create(@RequestBody CourseDto dto) {
        CourseDto created = courseService.create(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> update(@PathVariable int id,
                                            @RequestBody CourseDto dto) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
