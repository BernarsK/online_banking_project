package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void saveUser(String email, String password) {
        User user = new User(email, password);
        UUID userId = user.getId();

        if (userRepository.existsById(user.getId())) {
            // UUID already exists in the database, call the same function
            saveUser(email, password);
        } else {
            // register new user
            userRepository.save(user);
            // first account creation for a newly created user
            createNewAccount(userId);
        }
    }

    public void createNewAccount(UUID userId) {
        Account account = new Account(userId);
        if (accountRepository.existsById(account.getId())) {
            createNewAccount(userId);
        } else {
            accountRepository.save(account);
        }
    }
}
