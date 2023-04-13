package com.bernarsk.onlinebanking.service;

import com.bernarsk.onlinebanking.exceptions.TransactionException;
import com.bernarsk.onlinebanking.models.Account;
import com.bernarsk.onlinebanking.models.Transaction;
import com.bernarsk.onlinebanking.repositories.AccountRepository;
import com.bernarsk.onlinebanking.repositories.TransactionRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionSendServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserService userService;
    @Mock
    private HttpSession session;

    @InjectMocks
    private TransactionSendService transactionSendService;

    private UUID userId;
    private Account senderAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        senderAccount = new Account();
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setUserId(userId);
        senderAccount.setBalance(500.0);
    }

    @Test
    void testSaveTransactionBelow200() throws TransactionException {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        Account recieverAccount = new Account();
        senderAccount.setUserId(userId);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(1000.0);

        recieverAccount.setUserId(userId);
        recieverAccount.setAccountNumber("0987654321");
        recieverAccount.setBalance(1000.0);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);
        when(accountRepository.findByAccountNumber(recieverAccount.getAccountNumber())).thenReturn(recieverAccount);
        when(accountRepository.existsByAccountNumber("0987654321")).thenReturn(true);


        Transaction transaction = new Transaction();
        transaction.setReference("JUnit test transaction");
        transaction.setId(UUID.randomUUID());
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo("0987654321");
        transaction.setAmount(100.0);

        when(transactionRepository.save(any())).thenReturn(transaction);
        // Exercise
        transactionSendService.saveTransaction(session, transaction);

        // Verify
        assertEquals(900.0, senderAccount.getBalance());
        assertEquals(1100.0, recieverAccount.getBalance());
    }
    @Test
    void testSaveTransactionAbove200() throws TransactionException {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        Account recieverAccount = new Account();
        senderAccount.setUserId(userId);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(1000.0);

        recieverAccount.setUserId(userId);
        recieverAccount.setAccountNumber("0987654321");
        recieverAccount.setBalance(1000.0);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);
        when(accountRepository.findByAccountNumber(recieverAccount.getAccountNumber())).thenReturn(recieverAccount);
        when(accountRepository.existsByAccountNumber("0987654321")).thenReturn(true);


        Transaction transaction = new Transaction();
        transaction.setReference("JUnit test transaction");
        transaction.setId(UUID.randomUUID());
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo("0987654321");
        transaction.setAmount(400.0);

        when(transactionRepository.save(any())).thenReturn(transaction);
        // Exercise
        transactionSendService.saveTransaction(session, transaction);

        // Verify
        assertEquals(1000.0, senderAccount.getBalance());
        assertEquals(1000.0, recieverAccount.getBalance());
        assertEquals(transaction.getStatusApproved(), 0);
    }
    @Test
    void testSaveTransactionThrowsExceptionIfRecieverDoesNotExist() throws TransactionException {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        Account recieverAccount = new Account();
        senderAccount.setUserId(userId);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(1000.0);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);
        when(accountRepository.existsByAccountNumber("0987654321")).thenReturn(false);


        Transaction transaction = new Transaction();
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo(senderAccount.getAccountNumber());

        assertThrows(TransactionException.class, () -> {
            // Exercise
            transactionSendService.saveTransaction(session, transaction);
        });
    }
    @Test
    void testSaveTransactionThrowsExceptionIfAmountIsNegative() throws TransactionException {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        Account recieverAccount = new Account();
        senderAccount.setUserId(userId);
        senderAccount.setAccountNumber("1234567890");
        senderAccount.setBalance(1000.0);

        recieverAccount.setUserId(userId);
        recieverAccount.setAccountNumber("0987654321");
        recieverAccount.setBalance(1000.0);

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);
        when(accountRepository.findByAccountNumber(recieverAccount.getAccountNumber())).thenReturn(recieverAccount);
        when(accountRepository.existsByAccountNumber("0987654321")).thenReturn(true);


        Transaction transaction = new Transaction();
        transaction.setReference("JUnit test transaction");
        transaction.setId(UUID.randomUUID());
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo("0987654321");
        transaction.setAmount(-400.0);

        when(transactionRepository.save(any())).thenReturn(transaction);
        // Verify
        assertThrows(TransactionException.class, () -> {
            // Exercise
            transactionSendService.saveTransaction(session, transaction);
        });
    }

    @Test
    void testSaveTransactionThrowsExceptionIfSenderAndReceiverAccountsAreTheSame() {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        senderAccount.setUserId(userId);
        senderAccount.setAccountNumber("1234567890");

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo(senderAccount.getAccountNumber());

        // Verify
        assertThrows(TransactionException.class, () -> {
            // Exercise
            transactionSendService.saveTransaction(session, transaction);
        });
    }

    @Test
    void testSaveTransactionThrowsExceptionIfAccountFromDoesNotBelongToLoggedInUser() {
        // Setup
        UUID userId = UUID.randomUUID();
        Account senderAccount = new Account();
        senderAccount.setUserId(UUID.randomUUID());
        senderAccount.setAccountNumber("1234567890");

        when(session.getAttribute("UUID")).thenReturn(userId);
        when(accountRepository.findByAccountNumber(senderAccount.getAccountNumber())).thenReturn(senderAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(senderAccount.getAccountNumber());
        transaction.setAccountTo("0987654321");

        // Verify
        assertThrows(TransactionException.class, () -> {
            // Exercise
            transactionSendService.saveTransaction(session, transaction);
        });
    }
}
