package com.bernarsk.onlinebanking.models;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", updatable = false)
    private UUID id;

    private String name;

    private String surname;
    private String email;
    private String password;

    private LocalDate creation_date;

    private String verificationCode;

    private Integer active;

    private Integer userLevel;
    public User() {}

    public User(String password, String email, LocalDate creation_date, Integer userLevel) {
        this.password = password;
        this.email = email;
        this.creation_date = creation_date;
        this.userLevel = userLevel;
    }

    public User(String email, String password, String name, String surname) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.verificationCode = "";
        this.active = 0;
        this.creation_date = LocalDate.now();
        this.userLevel = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate creation_date() {
        return creation_date;
    }

    public void setDateCreated(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
