package com.flavio.paxcanina.controller;

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

    /**
     * GET /api/admin
     * Restituisce la lista di tutti gli Admin.
     */
    @GetMapping
    public List<Admin> list() {
        return adminService.findAllAdmins();
    }

    /**
     * GET /api/admin/me
     * Restituisce il profilo dell’Admin attualmente autenticato.
     */
    @GetMapping("/me")
    public Admin me(Authentication authentication) {
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getAdmin();
    }

    /**
     * POST /api/admin?userId=123
     * Promuove l’utente con ID=123 a Admin.
     */
    @PostMapping
    public Admin create(@RequestParam("userId") int userId) {
        return adminService.promoteToAdmin(userId);
    }

    /**
     * DELETE /api/admin/{id}
     * Revoca il ruolo Admin all’utente con ID={id}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        adminService.removeAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
