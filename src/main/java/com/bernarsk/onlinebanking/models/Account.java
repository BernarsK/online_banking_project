package com.bernarsk.onlinebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="user_accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false)
    private UUID id;
    private UUID userId;
    private String accountNumber;

    private Integer accountType;

    public Account() {}

    public Account(UUID userId, String accountNumber, Integer accountType) {
        this.userId = userId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
    }

    public Account(UUID userId) {

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
}
