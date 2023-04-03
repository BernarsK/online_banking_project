package com.bernarsk.onlinebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    private LocalDate creation_date;
    public User() {}

    public User(String password, String email, LocalDate creation_date) {
        this.password = password;
        this.email = email;
        this.creation_date = creation_date;
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
}
