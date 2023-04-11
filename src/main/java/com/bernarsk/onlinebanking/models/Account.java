package com.bernarsk.onlinebanking.models;

import com.bernarsk.onlinebanking.utils.IbanGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="user_accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false)
    private UUID id;
    @Column(name = "user_id", columnDefinition = "BINARY(16)", updatable = false)
    private UUID userId;
    private String accountNumber;

    private Double balance;

    private Integer accountType;
    // accountType 0 -> default checkings account
    // accountType 1 -> savings account

    private Integer accountActive;

    public static final List<String> ACCOUNT_TYPES = Arrays.asList("Checking", "Savings");

    public Account() {}

    public Account(UUID userId, String accountNumber, Integer accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Account(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.accountNumber = IbanGenerator.generateIban();
        this.accountType = 0; // create checkings account on default
        this.balance = 0.00; // default balance is 0.00
        this.accountActive = 1;

    }

    public Account(UUID userId, Integer accType) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.accountNumber = IbanGenerator.generateIban();
        this.accountType = accType;
        this.balance = 0.00; // default balance is 0.00
        this.accountActive = 0; // has to wait for approval

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
