package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;

    public Boolean authenticateUser(HttpSession session, String email, String password) {
        if (userRepository == null) {
            return false;
        }

        User user = userRepository.findByEmail(email);
        if (user != null) {
//            String hashedPassword = hashPassword(password, user.getSalt());
//            return hashedPassword.equals(user.getPassword());
            if (password.equals(user.getPassword())){
                session.setAttribute("UUID", user.getId());
                session.setAttribute("email", email);
                return true;
            }
        }
        return false;
    }

//    private String hashPassword(String password, String salt) {
//        // Code to hash password with salt goes here
//    }


}
