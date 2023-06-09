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
        return new TransactionException("Logged in user, has no access to this function");
    }

    public static TransactionException senderRecieverAccuntError() {
        return new TransactionException("Sender and Reciever cant be the same account");
    }

    public static TransactionException transactionNotApproved() {
        return new TransactionException("transaction is not approved");
    }

    public static TransactionException notUsersAccountError() {
        return new TransactionException("account is not logged in user account");
    }
}
