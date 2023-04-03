package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setDateCreated(LocalDate.now());
            userRepository.save(user);
        }
        else{
            //email already exists in database
        }
    }
}
