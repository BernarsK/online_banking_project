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

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransactionSendService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    public void saveTransaction(HttpSession session, Transaction transaction) {
        UUID userId = (UUID) session.getAttribute("UUID"); //logged in user id
        Account senderAccount = accountRepository.findByAccountNumber(transaction.getAccountFrom());//account of sender
        if(transaction.getAccountFrom()==transaction.getAccountTo()){
            throw TransactionException.senderRecieverAccuntError();
        }
        if (userId.equals(senderAccount.getUserId())) //need to check if acountFrom is logged in user account
        {
            if (accountRepository.existsByAccountNumber(transaction.getAccountTo())) {
                transaction.setDate(LocalDate.now());
                if (transaction.getAmount()>=0) {
                    if (transaction.getAmount() > 200)
                        transaction.setStatusApproved(0);
                    else transaction.setStatusApproved(1); //transactions with amount above 200 need approval
                    Transaction savedTransaction = transactionRepository.save(transaction);
                    if (savedTransaction.getStatusApproved() == 1) {
                        // transaction was successfully saved and transaction is approved
                        try {
                            processTransaction(savedTransaction);
                        } catch (TransactionException e) {
                            throw e; //propagates processTransaction exception
                        }
                    }
                } else throw TransactionException.transactionAmountError();
            } else throw TransactionException.recieverAccountNotFound();//reciever account does not exist
        }
    }

    private void processTransaction(Transaction transaction) {
        if (transaction.getStatusApproved() == 1) {//processTransaction should be called olny on approved transactions but extra check in case of mistake
            Account recieverAccount = accountRepository.findByAccountNumber(transaction.getAccountTo());
            Account senderAccount = accountRepository.findByAccountNumber(transaction.getAccountFrom());
            Double transactionAmount = transaction.getAmount();
            Double senderBalance = senderAccount.getBalance();
            Double recieverBalance = recieverAccount.getBalance();
            if(senderAccount==recieverAccount){
                throw TransactionException.senderRecieverAccuntError();
            }
            if (senderBalance - transactionAmount >= 0) { //check if sender has balance to cover transaction amount
                recieverAccount.setBalance(recieverBalance + transactionAmount);
                accountRepository.save(recieverAccount);
                senderAccount.setBalance(senderBalance - transactionAmount);
                accountRepository.save(senderAccount);//change balance of accounts by transaction amount and save it to database
            } else throw TransactionException.insufficientFunds();//not enough balance
        } else throw TransactionException.transactionNotApproved();
    }
    public void changeTransactionStatus(Transaction transaction, HttpSession session, Integer statusValue) {
        UUID userId = (UUID) session.getAttribute("UUID"); //logged in user id
        User loggedInUser = userService.findUserById(userId);
        if (loggedInUser.getUserLevel()!=1) {//only admins can have access
            throw TransactionException.accessError();
        }
        if (statusValue==2){//if transaction is being canceld need to change olny transaction status
            transaction.setStatusApproved(statusValue);
            transactionRepository.save(transaction);
            return;
        }
        Account senderAccount = accountRepository.findByAccountNumber(transaction.getAccountFrom());
        if (senderAccount.getBalance()<transaction.getAmount()) { //checks for funds in sender account
            transaction.setStatusApproved(2); //transaction canceled
            transactionRepository.save(transaction);
            throw TransactionException.insufficientFunds();
        }
        transaction.setStatusApproved(statusValue);
        transactionRepository.save(transaction);
        processTransaction(transaction);
    }
}
