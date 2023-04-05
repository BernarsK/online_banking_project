package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccountsForUser(UUID userId) {
        List<Account> accountList = new ArrayList<>();
        System.out.println(userId);
        System.out.println(accountRepository.findByUserId(userId));
        return accountRepository.findByUserId(userId);
    }
}


