package com.bernarsk.onlinebanking.models;

import jakarta.persistence.*;
import java.util.UUID;

import java.time.LocalDate;

@Entity
@Table(name="user_accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer userId;
    private String accountNumber;

    private Integer accountType;

    public Account() {}

    public Account(Integer userId, String accountNumber, Integer accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber() {
        UUID uuid = UUID.randomUUID();
        this.accountNumber = uuid.toString();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}