package com.example.xchange;

import android.graphics.Bitmap;
import android.os.Build;

import java.time.LocalDate;
import java.util.ArrayList;

public class xChanger extends User{
    private static Long nextId = 10L;
    private Float sumOfratings;
    private int numOfratings;
    private float rating;
    private ArrayList<Item> items;
    private ArrayList<Request> requests;
    private ArrayList<Counteroffer> counterOffers;
    private ArrayList<xChange> finalized;
    private String location;
    private int succeed_Deals;
    private int failed_Deals;



    xChanger(String username, String email, LocalDate join_date,String password, String location) {
        super(nextId++, username, email, join_date,password,location);
        items=new ArrayList<>();
        requests=new ArrayList<>();
        counterOffers=new ArrayList<>();
        finalized=new ArrayList<>();
        this.location = location;
        this.numOfratings=0;
        this.sumOfratings=0.0f;
        this.rating=0;
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
        MainActivity.statistics.put("NUMBER OF XCHANGERS", MainActivity.statistics.get("NUMBER XCHANGERS") + 1);
        return true;
    }


    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating,xChanger xchanger) {
        xchanger.numOfratings++;
        xchanger.sumOfratings+=rating;
        xchanger.rating=xchanger.sumOfratings/xchanger.numOfratings;
    }

    public void setRating(Float rating) {
        this.numOfratings++;
        this.sumOfratings+=rating;
        this.rating=this.sumOfratings/this.numOfratings;
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
    public ArrayList<xChange> getFinalized() {
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

    public void UploadItem(String item_name, String item_description, String item_category, String item_condition, ArrayList<Bitmap> item_images){
        this.getItems().add(new Item(item_name,item_description,item_category,item_condition,item_images));
    }

    public void RequestItem(xChanger xchanger2, Item offered_item, Item requested_item){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.getRequests().add(new Request(this, xchanger2, offered_item, requested_item, LocalDate.now()));
        }
    }

    public void plusOneSucceedDeal(){
        this.succeed_Deals++;
    }

    public void plusOneFailedDeal(){
        this.failed_Deals++;
    }

    public void report(xChanger xchanger, String message, xChange finalized){
        if(finalized.getStatus()!=null){
            xchanger.setRating((float) (xchanger.getRating()-0.2));
            message="User "+this.getUsername()+"reported user "+xchanger.getUsername();
        }
        MainActivity.reports.add(message);
        MainActivity.statistics.put("NUMBER OF REPORTS", MainActivity.statistics.get("NUMBER OF REPORTS") + 1);
    }



    public String acceptRequest(Request request){
        String email = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            xChange deal=new xChange(request,LocalDate.now());
            email=deal.acceptOffer();
        }
        MainActivity.statistics.replace(request.getRequestedItem().getItemCategory(),MainActivity.statistics.get(request.getRequestedItem().getItemCategory())+1);
        MainActivity.statistics.replace(request.getOfferedItem().getItemCategory(),MainActivity.statistics.get(request.getOfferedItem().getItemCategory())+1);
        MainActivity.statistics.put("NUMBER OF SUCCED DEALS", MainActivity.statistics.get("NUMBER OF SUCCED DEALS") + 1);
        return email;
    }

    public String acceptCounteroffer(Counteroffer counteroffer){
        String email="";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            xChange deal=new xChange(counteroffer.getRequest(),counteroffer,LocalDate.now());
            email=deal.acceptOffer();
        }
        MainActivity.statistics.replace(counteroffer.getRequestedItem().getItemCategory(),MainActivity.statistics.get(counteroffer.getRequestedItem().getItemCategory())+1);
        MainActivity.statistics.replace(counteroffer.getOfferedItem().getItemCategory(),MainActivity.statistics.get(counteroffer.getOfferedItem().getItemCategory())+1);
        MainActivity.statistics.put("NUMBER OF SUCCED DEALS", MainActivity.statistics.get("NUMBER OF SUCCED DEALS") + 1);
        return email;
    }

    public void rejectRequest(Request request){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            xChange deal=new xChange(request,LocalDate.now());
            deal.rejectOffer();
        }
        MainActivity.statistics.put("NUMBER OF FAILED DEALS", MainActivity.statistics.get("FAILED OF SUCCED DEALS") + 1);

    }

    public void rejectCounteroffer(Counteroffer counteroffer){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            xChange deal=new xChange(counteroffer.getRequest(),counteroffer,LocalDate.now());
            deal.rejectOffer();
        }
        MainActivity.statistics.put("NUMBER OF FAILED DEALS", MainActivity.statistics.get("FAILED OF SUCCED DEALS") + 1);
    }

    public void counterOffer(Item item,String Message,Request request){
        this.counterOffers.add(new Counteroffer(request,Message,item));
    }
}
