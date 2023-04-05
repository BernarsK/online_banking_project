package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionSendService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    public void saveTransaction(HttpSession session, Transaction transaction) {
        String userId = (String)session.getAttribute("email"); //logged in user id
        Account senderId = accountRepository.findByAccountNumber(transaction.getAccountFrom());//user id of sender account
//        List<Account> userAccounts = accountRepository.findByUserId(userId);
        if (userId.equals(senderId.getUserId())) {//need to check if acountFrom is logged in user account
            if (accountRepository.existsByAccountNumber(transaction.getAccountTo())) {
                transaction.setDate(LocalDate.now());
                if (transaction.getAmount() > 200)
                    transaction.setStatus_approved(0);
                else transaction.setStatus_approved(1); //transactions with amount above 200 need approval
                transactionRepository.save(transaction);
            } else ;//reciever account does not exist
        }
    }
}
