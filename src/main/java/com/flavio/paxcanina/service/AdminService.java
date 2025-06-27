package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AdminDao;
import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.dto.AdminProfileDto;
import com.flavio.paxcanina.model.Admin;
import com.flavio.paxcanina.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminService {

    private final AdminDao adminDao;
    private final UserDao userDao;

    public AdminService(AdminDao adminDao, UserDao userDao) {
        this.adminDao = adminDao;
        this.userDao = userDao;
    }

    /** Get all admins */
    public List<Admin> findAllAdmins() {
        return adminDao.findAll();
    }

    /** Promote a user to admin role (returns the Admin entity) */
    @Transactional
    public Admin promoteToAdmin(int userId) {
        User u = userDao.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id " + userId
                ));

        if (!adminDao.existsById(userId)) {
            Admin admin = new Admin();
            admin.setIdUser(u.getIdUser());
            admin.setLastName(u.getLastName());
            admin.setFirstName(u.getFirstName());
            admin.setEmail(u.getEmail());
            admin.setPasswordHash(u.getPasswordHash());
            admin.setPhone(u.getPhone());
            admin.setRegistrationDate(u.getRegistrationDate());
            admin.setAvatarUrl(u.getAvatarUrl());
            admin.setBio(u.getBio());
            admin.setLastLogin(u.getLastLogin());
            return adminDao.save(admin);
        }

        // Already an admin, just return it
        return adminDao.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Inconsistent admin record for user " + userId
                ));
    }

    /** Remove admin privileges (delete Admin record) */
    @Transactional
    public void removeAdmin(int id) {
        Admin adm = adminDao.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Admin not found with id " + id
                ));
        adminDao.delete(adm);
    }

    /** Save or update admin entity */
    public Admin save(Admin admin) {
        return adminDao.save(admin);
    }

    /** Convert an Admin entity to a profile DTO */
    public AdminProfileDto toProfileDto(Admin admin) {
        AdminProfileDto dto = new AdminProfileDto();
        dto.setLastName(admin.getLastName());
        dto.setFirstName(admin.getFirstName());
        dto.setAvatarUrl(admin.getAvatarUrl());
        dto.setBio(admin.getBio());
        dto.setPhone(admin.getPhone());
        dto.setEmail(admin.getEmail());
        return dto;
    }

    /** Update admin profile from DTO */
    @Transactional
    public Admin updateProfile(Admin admin, AdminProfileDto dto) {
        admin.setLastName(dto.getLastName());
        admin.setFirstName(dto.getFirstName());
        admin.setAvatarUrl(dto.getAvatarUrl());
        admin.setBio(dto.getBio());
        admin.setPhone(dto.getPhone());
        return adminDao.save(admin);
    }
}
