package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.LoginException;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Boolean authenticateUser(HttpSession session, String email, String password) {

        User user = userRepository.findByEmail(email);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
//            throw LoginException.invalidCredentials();
            return false;
        }

        session.setAttribute("UUID", user.getId());
        session.setAttribute("email", email);
        return true;
    }



}
