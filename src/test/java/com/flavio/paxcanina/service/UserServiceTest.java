package com.flavio.paxcanina.service;

import com.flavio.paxcanina.dao.*;
import com.flavio.paxcanina.dto.ChangePasswordRequestDto;
import com.flavio.paxcanina.model.Owner;
import com.flavio.paxcanina.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private final UserDao userDao = mock(UserDao.class);
    private final AdminDao adminDao = mock(AdminDao.class);
    private final CoachDao coachDao = mock(CoachDao.class);
    private final OwnerDao ownerDao = mock(OwnerDao.class);
    private final SpecializationDao specializationDao = mock(SpecializationDao.class);
    private final EmailService emailService = mock(EmailService.class);
    private final ValidationTokenDao validationTokenDao = mock(ValidationTokenDao.class);

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(
                userDao, adminDao, coachDao, ownerDao,
                specializationDao, emailService, validationTokenDao, passwordEncoder
        );
    }

    @Test
    void changePassword_success_encodesWithBCrypt_andUpdatesAudit() {
        User u = new Owner();
        u.setIdUser(42);
        u.setPasswordHash(passwordEncoder.encode("OldPass123"));
        when(userDao.findById(42)).thenReturn(Optional.of(u));

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto();
        dto.setCurrentPassword("OldPass123");
        dto.setNewPassword("NewPass123");

        userService.changePassword(42, dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userDao, atLeastOnce()).save(captor.capture());
        String savedHash = captor.getValue().getPasswordHash();

        assertNotNull(savedHash);
        assertNotEquals("NewPass123", savedHash);
        assertTrue(passwordEncoder.matches("NewPass123", savedHash));
        assertNotNull(captor.getValue().getLastPasswordChangeAt());
    }

    @Test
    void changePassword_fails_onWrongCurrentPassword() {
        User u = new Owner();
        u.setIdUser(77);
        u.setPasswordHash(passwordEncoder.encode("Correct#1"));
        when(userDao.findById(77)).thenReturn(Optional.of(u));

        ChangePasswordRequestDto dto = new ChangePasswordRequestDto();
        dto.setCurrentPassword("Wrong#1");
        dto.setNewPassword("Another#2");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(77, dto)
        );
        assertEquals("invalid_current_password", ex.getMessage());
        verify(userDao, never()).save(any());
    }
}
