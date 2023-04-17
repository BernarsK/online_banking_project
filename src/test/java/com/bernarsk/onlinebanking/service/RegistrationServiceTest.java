package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.RegistrationException;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    UserRepository userRepository;

    @Mock
    AccountRepository accountRepository;

    @Mock
    EmailService emailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        // Arrange
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String name = "John";
        String surname = "Doe";
        UUID userId = UUID.randomUUID();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        registrationService.saveUser(email, password, name, surname);

        // Assert
        // No exception should be thrown
    }

    @Test
    void testSaveUserThrowsExceptionWhenPasswordTooShort() {
        // Arrange
        String email = "junit@gmail.com";
        String password = "weak";
        String name = "John";
        String surname = "Doe";

        // Act and Assert
        RegistrationException exception = Assertions.assertThrows(RegistrationException.class, () -> {
            registrationService.saveUser(email, password, name, surname);
        });

        String expectedMessage = "Password must be at least 8 characters long";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    public void testSaveUserThrowsExceptionWhenPasswordNotStrongEnough() {
        String email = "test@test.com";
        String password = "weakpassword";
        String name = "John";
        String surname = "Doe";

        RegistrationException exception = Assertions.assertThrows(RegistrationException.class, () -> {
            registrationService.saveUser(email, password, name, surname);
        });

        String expectedMessage = "Password must contain at least one uppercase letter, one lowercase letter, and one digit";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void testSaveUserWithEmailAlreadyTaken() {
        // Arrange
        String email = "junit@gmail.com";
        String password = "Pa55w0rd";
        String name = "John";
        String surname = "Doe";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        RegistrationException exception = Assertions.assertThrows(RegistrationException.class, () -> {
            registrationService.saveUser(email, password, name, surname);
        });
        String expectedMessage = "Email already registered";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}