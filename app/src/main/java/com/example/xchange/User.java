package com.example.xchange;

import java.time.LocalDate;

public abstract class User {
    private final Long user_id;
    private String username;
    private String email;
    private final LocalDate join_Date;
    private String password;

    // Constructor
    User(Long user_id, String username, String email, LocalDate join_date,String password) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.join_Date = join_date;
        this.password=password;
    }

    // Getters
    public Long getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getJoinDate() {
        return join_Date;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.username = password;
    }

    public String getPassword() {
       return password;
    }


    // Abstract methods for login and registration
    public abstract boolean login(String username, String password);

    public abstract boolean register(String username, String email, String password);
}
