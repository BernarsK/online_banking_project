package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TransactionSendService {
    @Autowired
    private TransactionRepository transactionRepository;

    public void saveTransaction(Transaction transaction) {
        transaction.setDate(LocalDate.now());
        if (transaction.getAmount()>200)
            transaction.setStatus_approved(0);
        else transaction.setStatus_approved(1);
        transactionRepository.save(transaction);
    }
}
