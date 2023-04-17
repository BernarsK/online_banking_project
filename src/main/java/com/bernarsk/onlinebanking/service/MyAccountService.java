package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.MyAccountException;
import com.bernarsk.onlinebanking.exceptions.RegistrationException;
import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class MyAccountService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void setFullName(UUID userID, String name, String surname, HttpSession session) {
        User user = userService.findUserById(userID);
        UUID logedInUserId = (UUID) session.getAttribute("UUID"); //logged in user id
        User loggedInUser = userService.findUserById(logedInUserId);
        if (user==null){
            throw MyAccountException.userNotFoundException();
        }
        if (loggedInUser==null){
            throw MyAccountException.userNotLogedIn();
        }
        if (loggedInUser.getUserLevel()!=1 && logedInUserId != userID) {//throws exception if loged in user is not admin and tries to change other users data
            throw MyAccountException.accessError();
        }
        user.setName(name);
        user.setSurname(surname);
        userRepository.save(user);
    }

    public void setEmail(UUID userID, String email, HttpSession session) {
        User user = userService.findUserById(userID);
        UUID logedInUserId = (UUID) session.getAttribute("UUID"); //logged in user id
        User loggedInUser = userService.findUserById(logedInUserId);
        if (user==null){
            throw MyAccountException.userNotFoundException();
        }
        if (loggedInUser==null){
            throw MyAccountException.userNotLogedIn();
        }
        if (loggedInUser.getUserLevel()!=1 && logedInUserId != userID) {//throws exception if loged in user is not admin and tries to change other users data
            throw MyAccountException.accessError();
        }
        if (userService.findUserIdByEmail(email)==null){
            user.setEmail(email);
            userRepository.save(user);
        }
        else throw MyAccountException.emailAlreadyUsed(); //throws exception if email already used

    }

    public void changePassword(UUID userID, String password1, String password, HttpSession session) {
        try {
            RegistrationService.checkPasswordStrength(password1);
        }
        catch (RegistrationException e)
        {
            throw e;
        }
        User user = userService.findUserById(userID);

        UUID logedInUserId = (UUID) session.getAttribute("UUID"); //logged in user id
        User loggedInUser = userService.findUserById(logedInUserId);
        if (user==null){
            throw MyAccountException.userNotFoundException();
        }
        if (loggedInUser==null){
            throw MyAccountException.userNotLogedIn();
        }
        if (loggedInUser.getUserLevel()!=1 && logedInUserId != userID) {//throws exception if loged in user is not admin and tries to change other users data
            throw MyAccountException.accessError();
        }
        if (!passwordEncoder.matches(password, user.getPassword()) && loggedInUser.getUserLevel()!=1){ //admins can change user passwords without providing old password
            throw MyAccountException.passwordError();
        }
        user.setPassword(passwordEncoder.encode(password1));

        userRepository.save(user);
    }
}
