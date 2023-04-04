package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public boolean authenticateUser(String email, String password) {
        if (userRepository == null) {
            System.out.println("userRepository is null");
            return false;
        }
        User user = userRepository.findByEmail(email);
        if (user != null) {
//            String hashedPassword = hashPassword(password, user.getSalt());
//            return hashedPassword.equals(user.getPassword());
            return password.equals(user.getPassword());
        }
        return false;
    }

//    private String hashPassword(String password, String salt) {
//        // Code to hash password with salt goes here
//    }
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

}
