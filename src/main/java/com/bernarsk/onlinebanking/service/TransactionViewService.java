package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.models.User;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.TransactionRepository;
import com.bernarsk.onlinebanking.repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionViewService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    public List<Transaction> viewAccountTransactions(HttpSession session, Account recieverAccount, Account senderAccount) {
        UUID userId = (UUID) session.getAttribute("UUID"); //logged in user id
        User loggedInUser = userRepository.findById(userId).orElse(null);
        String senderAccountNumber = senderAccount.getAccountNumber();
        String recieverAccountNumber = recieverAccount.getAccountNumber();
        if (loggedInUser.getUserLevel()==0 && (!recieverAccount.getUserId().equals(userId)||!senderAccount.getUserId().equals(userId))){
                throw TransactionException.accessError();
        }//throws exception if logged in as normal user and tries to see other user account
        return transactionRepository.findAllByAccountFromOrAccountTo(senderAccountNumber, recieverAccountNumber);
    }
    public List<Transaction> findAllByStatusApproved(Integer statusApproved) {
        return transactionRepository.findAllByStatusApproved(statusApproved);
    }

    public Transaction findByID(UUID transactionId) {
        return transactionRepository.findById(transactionId).orElse(null);
    }
}
