package com.example.xchange;

import android.os.Build;

import java.time.LocalDate;
import java.util.ArrayList;

public class admin extends User {

    private static Long nextId = 1L;

    // Constructor
    public admin(String username, String email, LocalDate join_date,String password,String location) {
        super(nextId++, username, email, join_date,password,location);
    }

    // Implement login method
    @Override
    public boolean login(String username, String password) {
        for (User user : MainActivity.admins) {
            if (user.getPassword().equals("IamtheAdmin") && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean register(String username, String email, String password,String location){

        // Check if the user already exists
        for (User user : MainActivity.admins) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }
        // Register the new user
        User newUser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newUser = new admin(username, email, LocalDate.now(),password,location);
        }
        MainActivity.admins.add(newUser);
        return true;
    }
    public void manageCategories(String category,String action) {
        if(action.equals("add")){
            MainActivity.categories.add(category);
        }
        else{
            for(String cat:MainActivity.categories){
                if(cat.equals(category)){
                    MainActivity.categories.remove(category);
                    return;
                }
                else{
                    System.out.println("This category does not exist");
                }
            }
        }
    }

    public void viewReports() {
        int i=0;
        for(String report:MainActivity.reports){
            System.out.println("Report "+i+":"+report);
            i++;
        }
    }
    public void viewStatistics() {

    }


}
