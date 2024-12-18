package com.example.xchange;

public class admin extends User {
    public admin(String username, String email, SimpleCalendar join_Date, String password, String location) {
        super(username, email, join_Date, password, location, "admin");
    }
}
