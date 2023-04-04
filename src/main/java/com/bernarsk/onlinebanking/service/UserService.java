package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Integer findUserLevelByEmail(String email) {
        return userRepository.findUserLevelByEmail(email);
    }
}
