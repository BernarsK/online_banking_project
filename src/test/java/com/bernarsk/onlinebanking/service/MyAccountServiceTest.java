package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.MyAccountException;
import com.bernarsk.onlinebanking.exceptions.RegistrationException;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MyAccountServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HttpSession session;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyAccountService myAccountService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void setFullName() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String name = "Jim";
        String surname = "Do";
        String newName = "John";
        String newSurname = "Doe";
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        myAccountService.setFullName(userId, newName, newSurname, session);
        assert(user.getName().equals(newName) && user.getSurname().equals(newSurname));
    }
    @Test
    void setFullNameThrowUserNotLoggedInException() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String name = "Jim";
        String surname = "Do";
        String newName = "John";
        String newSurname = "Doe";
        UUID logedInId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);

        when(session.getAttribute("UUID")).thenReturn(logedInId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        MyAccountException exception = Assertions.assertThrows(MyAccountException.class, () -> {
            myAccountService.setFullName(userId, newName, newSurname, session);
        });

        String expectedMessage = "User not logged in";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void setFullNameThrowNoAccessException() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String name = "Jim";
        String surname = "Do";
        String newName = "John";
        String newSurname = "Doe";
        UUID userId = UUID.randomUUID();
        UUID logedInId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);
        User logedInUser = new User( email,  password,  name,  surname);
        logedInUser.setUserLevel(0);

        when(session.getAttribute("UUID")).thenReturn(logedInId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.findUserById(logedInId)).thenReturn(logedInUser);
        when(userRepository.save(user)).thenReturn(user);

        MyAccountException exception = Assertions.assertThrows(MyAccountException.class, () -> {
            myAccountService.setFullName(userId, newName, newSurname, session);
        });

        String expectedMessage = "LogedInUser has no access to this function";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void setFullNameAdmin() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String name = "Jim";
        String surname = "Do";
        String newName = "John";
        String newSurname = "Doe";
        UUID userId = UUID.randomUUID();
        UUID logedInId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);
        User logedInUser = new User( email,  password,  name,  surname);
        logedInUser.setUserLevel(1);

        when(session.getAttribute("UUID")).thenReturn(logedInId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.findUserById(logedInId)).thenReturn(logedInUser);
        when(userRepository.save(user)).thenReturn(user);

        myAccountService.setFullName(userId, newName, newSurname, session);

    }
    @Test
    void setEmail() {
        String email = "john.doe@example.com";
        String newEmail = "Jim.doe@gmail.com";
        String password = "Pa55w0rd";
        String name = "John";
        String surname = "Doe";
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        myAccountService.setEmail(userId,  newEmail, session);
        assert(user.getEmail().equals(newEmail));
    }

    @Test
    void changePassword() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String newpassword = "StrongerPa$$word22";
        String name = "John";
        String surname = "Doe";
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);
        when(session.getAttribute("UUID")).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(passwordEncoder.encode(newpassword)).thenReturn(newpassword);

        myAccountService.changePassword(userId,  newpassword, password, session);

        assert(user.getPassword().equals(newpassword));
    }

    @Test
    void changePasswordThrowsUserNotFoundException() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String newpassword = "StrongerPa$$word22";
        String name = "John";
        String surname = "Doe";
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);
        when(session.getAttribute("UUID")).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(passwordEncoder.encode(newpassword)).thenReturn(newpassword);


        MyAccountException exception = Assertions.assertThrows(MyAccountException.class, () -> {
            myAccountService.changePassword(userId,  newpassword, password, session);
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
    @Test
    void changePasswordThrowsPasswordErrorException() {
        String email = "john.doe@example.com";
        String password = "Pa55w0rd";
        String newpassword = "StrongerPa$$word22";
        String name = "John";
        String surname = "Doe";
        UUID userId = UUID.randomUUID();
        User user = new User( email,  password,  name,  surname);
        when(session.getAttribute("UUID")).thenReturn(userId);
        when(userService.findUserById(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(passwordEncoder.encode(newpassword)).thenReturn(newpassword);


        MyAccountException exception = Assertions.assertThrows(MyAccountException.class, () -> {
            myAccountService.changePassword(userId,  newpassword, password, session);
        });

        String expectedMessage = "Wrong old password";
        String actualMessage = exception.getMessage();

        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}