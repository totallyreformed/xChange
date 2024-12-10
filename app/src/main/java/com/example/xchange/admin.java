package com.example.xchange;

import java.util.ArrayList;

public class admin extends User {

    private static long nextId = 1;
    private static ArrayList<User> admins=new ArrayList<>();


    // Constructor using SimpleCalendar
    public admin(String username, String email, SimpleCalendar join_date, String password, String location) {
        super(nextId++, username, email, join_date, password, location);
        this.register(this);
    }

    // Implement login method
    @Override
    public boolean login(String username, String password) {
        for (User user : admins) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean register(User user) {
        // Check if the user already exists
        for (User temp_user : admins) {
            if (temp_user.getUsername().equals(user.getUsername()) || temp_user.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        admins.add(user);
        return true;
    }

    public static ArrayList<User> getadmins(){
        return admins;
    }

    public static void resetNextId() {
        nextId = 1L;
    }
}