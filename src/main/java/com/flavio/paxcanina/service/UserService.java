package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AdminDao;
import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dao.OwnerDao;
import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserDao userDao;
    @Autowired private OwnerDao ownerDao;
    @Autowired private CoachDao coachDao;
    @Autowired private AdminDao adminDao;
    @Autowired private PasswordEncoder passwordEncoder;

    // Utility: map entity to dto
    private UserDto toDto(User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getIdUser());
        dto.setLastName(u.getLastName());
        dto.setFirstName(u.getFirstName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setAvatarUrl(u.getAvatarUrl());
        dto.setBio(u.getBio());
        // Dynamic role based on class type
        if (u instanceof Admin) dto.setRole("ADMIN");
        else if (u instanceof Coach) dto.setRole("COACH");
        else if (u instanceof Owner) dto.setRole("OWNER");
        else dto.setRole("USER");
        return dto;
    }

    private User fromDto(UserDto dto) {
        // Build instance according to role
        User u;
        switch (dto.getRole()) {
            case "ADMIN" -> u = new Admin();
            case "COACH" -> u = new Coach();
            default -> u = new Owner();
        }
        u.setLastName(dto.getLastName());
        u.setFirstName(dto.getFirstName());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());
        return u;
    }

    public List<UserDto> findAll() {
        return userDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Integer id) {
        return userDao.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public UserDto create(UserDto dto) {
        User u = fromDto(dto);
        u.setPasswordHash(passwordEncoder.encode("TempPassword123")); // Set a temp password
        u.setRegistrationDate(java.time.LocalDate.now());
        userDao.save(u);
        return toDto(u);
    }

    public UserDto update(Integer id, UserDto dto) {
        var opt = userDao.findById(id);
        if (opt.isEmpty()) return null;
        User u = opt.get();
        u.setLastName(dto.getLastName());
        u.setFirstName(dto.getFirstName());
        u.setPhone(dto.getPhone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());
        userDao.save(u);
        return toDto(u);
    }

    public void delete(Integer id) {
        userDao.deleteById(id);
    }

    public UserDto promoteToRole(Integer id, String role) {
        var opt = userDao.findById(id);
        if (opt.isEmpty()) return null;
        User u = opt.get();

        // Promote user to a new role (convert entity type)
        if ("ADMIN".equalsIgnoreCase(role) && !(u instanceof Admin)) {
            Admin admin = new Admin();
            copyData(u, admin);
            adminDao.save(admin);
            userDao.delete(u);
            return toDto(admin);
        }
        if ("COACH".equalsIgnoreCase(role) && !(u instanceof Coach)) {
            Coach coach = new Coach();
            copyData(u, coach);
            coachDao.save(coach);
            userDao.delete(u);
            return toDto(coach);
        }
        if ("OWNER".equalsIgnoreCase(role) && !(u instanceof Owner)) {
            Owner owner = new Owner();
            copyData(u, owner);
            ownerDao.save(owner);
            userDao.delete(u);
            return toDto(owner);
        }
        return toDto(u); // Already the right role
    }

    // Utility: Copy data between entities (except password/email/id!)
    private void copyData(User from, User to) {
        to.setLastName(from.getLastName());
        to.setFirstName(from.getFirstName());
        to.setEmail(from.getEmail());
        to.setPhone(from.getPhone());
        to.setBio(from.getBio());
        to.setAvatarUrl(from.getAvatarUrl());
        to.setPasswordHash(from.getPasswordHash());
        to.setRegistrationDate(from.getRegistrationDate());
    }
}
