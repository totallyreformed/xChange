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
    private String location;


    xChanger(String username, String email, LocalDate join_date,String password, String location) {
        super(nextId++, username, email, join_date,password,location);
        items=new ArrayList<>();
        requests=new ArrayList<>();
        counterOffers=new ArrayList<>();
        finalized=new ArrayList<>();
        this.location = location;
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
    public boolean register(String username, String email, String password,String location){

        for (User user : MainActivity.xChangers) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }

        User newUser = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            newUser = new xChanger(username, email, LocalDate.now(),password,location);
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
    public ArrayList<Item> getItems() {
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
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }


    public void deleteItem(Item item_for_deletion){
        this.getItems().removeIf(item -> item == item_for_deletion);
    }

    public Item getItem(Item item_to_get){
        for(Item item:this.getItems()){
            if(item==item_to_get){
                return item;
            }
        }
        return null;
    }

}
