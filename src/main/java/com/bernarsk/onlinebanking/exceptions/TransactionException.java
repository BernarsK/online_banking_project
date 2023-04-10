package com.bernarsk.onlinebanking.exceptions;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }

    public static TransactionException insufficientFunds() {
        return new TransactionException("Insufficient funds.");
    }

    public static TransactionException recieverAccountNotFound() {
        return new TransactionException("Reciever account not found.");
    }

    public static TransactionException transactionAmountError() {
        return new TransactionException("Amount must be positive number");
    }

    public static TransactionException accessError() {
        return new TransactionException("Logged in user, has no access to other user accounts");
    }
}
