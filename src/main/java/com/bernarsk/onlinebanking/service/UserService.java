package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Integer findUserLevelByEmail(String email) {
        return userRepository.findUserLevelByEmail(email);
    }

    public UUID findUserIdByEmail(String email) {
        return userRepository.findUserIdByEmail(email);
    }

    public User findUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User findUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }
}
