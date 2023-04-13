package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HttpSession session;
    @InjectMocks
    private LoginService loginService;

    private User userAuthentificating;
    private String password;
    private UUID userId;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        userAuthentificating = new User();
        userAuthentificating.setId(userId);
        userAuthentificating.setEmail("JUnit@gmail.com");
        userAuthentificating.setPassword("JUnitPa$$word!");
        password = "JUnitPa$$word!";
    }
    @Test
    void authenticateUser() {
        when(userRepository.findByEmail(userAuthentificating.getEmail())).thenReturn(userAuthentificating);
        when(passwordEncoder.matches(password, userAuthentificating.getPassword())).thenReturn(true);
        assertEquals(true, loginService.authenticateUser(session, userAuthentificating.getEmail(), userAuthentificating.getPassword()));
    }
    @Test
    void authenticateUserWrongEmail() {
        when(userRepository.findByEmail(userAuthentificating.getEmail())).thenReturn(null);
        assertEquals(false, loginService.authenticateUser(session, userAuthentificating.getEmail(), userAuthentificating.getPassword()));
    }
    @Test
    void authenticateUserWrongPassword() {
        when(userRepository.findByEmail(userAuthentificating.getEmail())).thenReturn(userAuthentificating);
        when(passwordEncoder.matches(password, userAuthentificating.getPassword())).thenReturn(false);
        assertEquals(false, loginService.authenticateUser(session, userAuthentificating.getEmail(), userAuthentificating.getPassword()));
    }
}