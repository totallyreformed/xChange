package com.example.xchange;

import java.util.ArrayList;

public class xChanger extends User {
    private float averageRating;
    private int totalRatings;
    private ArrayList<Rating> ratings;
    private ArrayList<String> reports;
    private ArrayList<Item> items;
    private ArrayList<Request> requests;
    private ArrayList<Counteroffer> counterOffers;
    private ArrayList<xChange> finalized;
    private int succeedDeals;
    private int failedDeals;

    public xChanger(String username, String email, SimpleCalendar join_date, String password, String location) {
        super(username, email, join_date, password, location, "xChanger");
        this.averageRating = 0;
        this.totalRatings = 0;
        this.ratings = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.items = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.counterOffers = new ArrayList<>();
        this.finalized = new ArrayList<>();
        this.succeedDeals=0;
        this.failedDeals=0;
    }
    public float getAverageRating() {
        return averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void addRating(Rating rating) {
        if (rating != null) {
            this.ratings.add(rating);
            this.totalRatings++;
            this.averageRating = (this.averageRating * (this.totalRatings - 1) + rating.getRating()) / this.totalRatings;
        }
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
        return this.getLocation();
    }

    public void deleteItem(Item item) {
        this.items.removeIf(i -> i.equals(item));
    }

    public Item getItem(Item item) {
        return this.items.stream().filter(i -> i.equals(item)).findFirst().orElse(null);
    }

    public void UploadItem(String item_name, String item_description, String item_category, String item_condition, ArrayList<Image> item_images) {
        Item item = new Item(item_name, item_description, item_category, item_condition, item_images);
        this.getItems().add(item);
    }

    public void RequestItem(xChanger xchanger2, Item offered_item, Item requested_item) {
        Request request = new Request(this, xchanger2, offered_item, requested_item, new SimpleCalendar(2024, 12, 3));
    }

    public void plusOneSucceedDeal() {
        this.succeedDeals++;
    }

    public void plusOneFailedDeal() {
        this.failedDeals++;
    }

    public void report(xChanger xchanger, String message, xChange finalized) {
        if (finalized.getStatus() != null) {
//            xchanger.setRating((float) (xchanger.getRating() - 0.2));
            message = "User " + this.getUsername() + " reported user " + xchanger.getUsername();
        }
        this.reports.add(message);
    }

    public String acceptRequest(Request request,float rating) {
        String email = "";
        xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
        email = deal.acceptOffer(rating);
        return email;
    }


    public String acceptCounteroffer(Counteroffer counteroffer,float rating) {
        String email = "";
        xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
        email = deal.acceptOffer(rating);
        return email;
    }

    public void rejectRequest(Request request,float rating) {
        xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
        deal.rejectOffer(rating);
    }

    public void rejectCounteroffer(Counteroffer counteroffer,float rating) {
        xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
        deal.rejectOffer(rating);
    }

    public void counterOffer(Item item, String message, Request request) {
        if (item == null || message == null || request == null) {
            throw new IllegalArgumentException("Item, message, or request cannot be null.");
        }
        Counteroffer counter=new Counteroffer(request, message, item);
    }
    public int getSucceed_Deals(){
        return this.succeedDeals;
    }
    public int getFailed_Deals(){
        return this.failedDeals;
    }
    public ArrayList<String> getReports(){
        return this.reports;
    }
    public ArrayList<Rating> getRatings(){return this.ratings;}

}