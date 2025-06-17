package com.flavio.paxcanina.controller;

import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> list() {
        return adminService.findAllAdmins();
    }

    @PostMapping
    public Admin create(@RequestParam("userId") int userId) {
        return adminService.promoteToAdmin(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        adminService.removeAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
