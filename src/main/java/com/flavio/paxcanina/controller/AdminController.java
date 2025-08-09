package com.flavio.paxcanina.controller;

import  com.flavio.paxcanina.dto.AdminProfileDto;
import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.security.AppUserDetails;
import com.flavio.paxcanina.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
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

    @GetMapping("/me")
    public AdminProfileDto me(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Admin admin = userDetails.getAdminOrThrow(); // <-- qui
        return adminService.toProfileDto(admin);
    }

    @PutMapping("/me")
    public ResponseEntity<AdminProfileDto> updateMyProfile(
            @RequestBody AdminProfileDto dto,
            Authentication authentication
    ) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        Admin admin = userDetails.getAdminOrThrow(); // <-- qui

        Admin updated = adminService.updateProfile(admin, dto);
        AdminProfileDto response = adminService.toProfileDto(updated);
        response.setEmail(admin.getEmail());
        return ResponseEntity.ok(response);
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
