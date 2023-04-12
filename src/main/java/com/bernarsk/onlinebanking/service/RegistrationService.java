package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.RegistrationException;
import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RegistrationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveUser(String email, String password) {
        if (userRepository.existsByEmail(email)){
            throw RegistrationException.emailNotAvailable();//not enough balance
        }
        User user = new User(email, passwordEncoder.encode(password));
//        User user = new User(email, password);
        UUID userId = user.getId();

        if (userRepository.existsById(user.getId())) {
            // UUID already exists in the database, call the same function
            saveUser(email, password);
        } else {
            // email verification for new user
            String verificationCode = generateVerificationCode();
            emailService.sendSimpleMessage("kazoksbernars@gmail.com", "Verification code for new account", "Hello! Thanks for registering! Your verification code is: " + verificationCode);
            user.setVerificationCode(verificationCode);
            // register new user
            userRepository.save(user);
            // first account creation for a newly created user
            createNewAccount(userId);
        }
    }
    public String generateVerificationCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println(generatedString);
        return generatedString;
    }

    public void createNewAccount(UUID userId) {
        Account account = new Account(userId);
        if (accountRepository.existsById(account.getId()) || accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            createNewAccount(userId);
        } else {
            accountRepository.save(account);
        }
    }
}
