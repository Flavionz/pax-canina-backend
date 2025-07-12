package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.AdminDao;
import com.flavio.paxcanina.dao.CoachDao;
import com.flavio.paxcanina.dao.OwnerDao;
import com.flavio.paxcanina.dao.UserDao;
import com.flavio.paxcanina.dao.SpecializationDao;
import com.flavio.paxcanina.dto.UserDto;
import com.flavio.paxcanina.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired private UserDao userDao;
    @Autowired private OwnerDao ownerDao;
    @Autowired private CoachDao coachDao;
    @Autowired private AdminDao adminDao;
    @Autowired private SpecializationDao specializationDao;
    @Autowired private PasswordEncoder passwordEncoder;

    // Convert Entity -> DTO
    private UserDto toDto(com.flavio.paxcanina.model.User u) {
        UserDto dto = new UserDto();
        dto.setId(u.getIdUser());
        dto.setLastName(u.getLastName());
        dto.setFirstName(u.getFirstName());
        dto.setEmail(u.getEmail());
        dto.setPhone(u.getPhone());
        dto.setAvatarUrl(u.getAvatarUrl());
        dto.setBio(u.getBio());
        if (adminDao.existsById(u.getIdUser())) dto.setRole("ADMIN");
        else if (coachDao.existsById(u.getIdUser())) {
            dto.setRole("COACH");
            Coach coach = coachDao.findById(u.getIdUser()).orElse(null);
            if (coach != null && coach.getSpecializations() != null) {
                List<String> specs = coach.getSpecializations()
                        .stream()
                        .map(Specialization::getName)
                        .collect(Collectors.toList());
                dto.setSpecializations(specs);
            }
        }
        else if (ownerDao.existsById(u.getIdUser())) dto.setRole("OWNER");
        else dto.setRole("USER");
        return dto;
    }

    // Convert DTO -> Entity (Owner by default)
    private com.flavio.paxcanina.model.User fromDto(UserDto dto) {
        com.flavio.paxcanina.model.User u = new Owner();
        u.setLastName(dto.getLastName());
        u.setFirstName(dto.getFirstName());
        u.setEmail(dto.getEmail());
        u.setPhone(dto.getPhone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());
        return u;
    }

    // List all users
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

    // Crea nuovo utente (Admin, Coach, Owner)
    public UserDto create(UserDto dto) {
        if (userDao.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        com.flavio.paxcanina.model.User baseUser = fromDto(dto);
        baseUser.setPasswordHash(passwordEncoder.encode("TempPassword123"));
        baseUser.setRegistrationDate(java.time.LocalDate.now());
        userDao.save(baseUser);

        switch (dto.getRole()) {
            case "ADMIN" -> adminDao.save(new Admin(baseUser));
            case "COACH" -> {
                Coach coach = new Coach(baseUser);
                if (dto.getSpecializations() != null && !dto.getSpecializations().isEmpty()) {
                    Set<Specialization> specializations = dto.getSpecializations().stream()
                            .map(name -> specializationDao.findByName(name)
                                    .orElseThrow(() -> new RuntimeException("Specialization not found: " + name)))
                            .collect(Collectors.toSet());
                    coach.setSpecializations(specializations);
                }
                coachDao.save(coach);
            }
            default -> ownerDao.save(new Owner(baseUser));
        }
        return toDto(baseUser);
    }

    public UserDto update(Integer id, UserDto dto) {
        Optional<com.flavio.paxcanina.model.User> opt = userDao.findById(id);
        if (opt.isEmpty()) return null;
        com.flavio.paxcanina.model.User u = opt.get();

        u.setLastName(dto.getLastName());
        u.setFirstName(dto.getFirstName());
        u.setPhone(dto.getPhone());
        u.setAvatarUrl(dto.getAvatarUrl());
        u.setBio(dto.getBio());

        // Aggiorna specializzazioni se coach
        if (u instanceof Coach coach) {
            if (dto.getSpecializations() != null) {
                Set<Specialization> specializations = dto.getSpecializations().stream()
                        .map(name -> specializationDao.findByName(name)
                                .orElseThrow(() -> new RuntimeException("Specialization not found: " + name)))
                        .collect(Collectors.toSet());
                coach.setSpecializations(specializations);
            } else {
                coach.setSpecializations(Collections.emptySet());
            }
        }

        userDao.save(u);
        return toDto(u);
    }

    public void delete(Integer id) {
        adminDao.findById(id).ifPresent(adminDao::delete);
        coachDao.findById(id).ifPresent(coachDao::delete);
        ownerDao.findById(id).ifPresent(ownerDao::delete);
        userDao.deleteById(id);
    }

    public UserDto promoteToRole(Integer id, String role) {
        Optional<com.flavio.paxcanina.model.User> opt = userDao.findById(id);
        if (opt.isEmpty()) return null;
        com.flavio.paxcanina.model.User u = opt.get();

        if ("ADMIN".equalsIgnoreCase(role) && !adminDao.existsById(u.getIdUser())) {
            adminDao.save(new Admin(u));
        }
        if ("COACH".equalsIgnoreCase(role) && !coachDao.existsById(u.getIdUser())) {
            Coach coach = new Coach(u);
            coach.setSpecializations(Collections.emptySet());
            coachDao.save(coach);
        }
        return toDto(u);
    }
}
