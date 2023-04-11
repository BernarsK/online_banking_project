package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.repositories.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TransactionSendServiceTest {
    @Mock
    HttpSession session;

    @Before("")
    public void setup() {
        session = mock(HttpSession.class);
    }
    @Autowired
    private TransactionRepository transactionRepository;
    @Test
    void saveTransaction() {
        // Create a transaction object with test data
        Transaction transaction = new Transaction();
        transaction.setAmount(300.31);
      //   transaction.setDate();
      //  transaction.setStatus_approved();
        String id = "";
        session.setAttribute("id", id);
        transaction.setAccountFrom("TestSender");
        transaction.setAccountTo("TestReciever");
        transaction.setReference("Test text");

        // Create an instance of the class containing the saveTransaction method
        TransactionSendService transactionDAO = new TransactionSendService();

        // Call the saveTransaction method
        transactionDAO.saveTransaction(session, transaction);
        Transaction savedTransaction = transactionRepository.getById(transaction.getId());

        // Verify that the session has been updated
//        assertEquals(100, session.getAttribute("totalTransactionAmount"));

        // Verify that the transaction has been saved correctly
        assertEquals(transaction.getAmount(), savedTransaction.getAmount());
        assertEquals(transaction.getAccountTo(), transaction.getAccountTo());
        assertEquals(transaction.getAccountFrom(), savedTransaction.getAccountFrom());
        assertEquals(transaction.getDate(), savedTransaction.getDate());
        assertEquals(transaction.getStatusApproved(), savedTransaction.getStatusApproved());
        assertEquals(transaction.getReference(), savedTransaction.getReference());

//        assertEquals(transaction.getTransactionType(), transactionDAO.getTransactionById(1).getTransactionType());

    }
}