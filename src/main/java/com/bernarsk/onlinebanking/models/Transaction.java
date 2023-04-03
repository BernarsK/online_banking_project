package com.bernarsk.onlinebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="user_transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountFrom;
    private String accountTo;
    private Double amount;
    private LocalDate date;
    private String reference;
    private Integer status_approved;
    public Transaction() {}

    public Transaction(String accountFrom, String accountTo, LocalDate date, Double amount, String reference) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.date = date;
        this.amount = amount;
        this.reference = reference;
        if (amount>200)
            this.status_approved=0;
        else this.status_approved=1;
    }


    public String getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(String accountFrom) {
        this.accountFrom = accountFrom;
    }

    public String getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(String accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getStatus_approved() {
        return status_approved;
    }

    public void setStatus_approved(Integer status_approved) {
        this.status_approved = status_approved;
    }


}
