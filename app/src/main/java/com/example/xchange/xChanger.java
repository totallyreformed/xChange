package com.example.xchange;

import android.os.Build;

import java.nio.file.attribute.FileAttribute;
import java.time.LocalDate;
import java.util.ArrayList;

public class xChanger extends User{
    private static Long nextId = 10L;
    private Float rating=null;
    private ArrayList<Item> items;
    private ArrayList<Request> requests;
    private ArrayList<Counteroffer> counterOffers;
    private ArrayList<Finalized> finalized;


    xChanger(String username, String email, LocalDate join_date,String password) {
        super(nextId++, username, email, join_date,password);
        items=new ArrayList<>();
        requests=new ArrayList<>();
        counterOffers=new ArrayList<>();
        finalized=new ArrayList<>();
    }

    // Implement login method
    @Override
    public boolean login(String username, String password) {
        for (User user : MainActivity.xChangers) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean register(String username, String email, String password){

        for (User user : MainActivity.xChangers) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }
        User newUser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newUser = new xChanger(username, email, LocalDate.now(),password);
        }
        MainActivity.xChangers.add(newUser);
        return true;
    }


    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
    public ArrayList<Item> getItem() {
        return this.items;
    }
    public ArrayList<Request> getRequests() {
        return this.requests;
    }
    public ArrayList<Counteroffer> getCounterOffers() {
        return this.counterOffers;
    }
    public ArrayList<Finalized> getFinalized() {
        return this.finalized;
    }
}
