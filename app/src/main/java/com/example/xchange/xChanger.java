package com.example.xchange;

import java.util.ArrayList;

public class xChanger extends User {
    private static Long nextId = 10L;
    private Float sumOfratings;
    private int numOfratings;
    private float rating;
    ArrayList<String> reports;
    private static ArrayList<User> xchangers=new ArrayList<>();
    private ArrayList<Item> items;
    private ArrayList<Request> requests;
    private ArrayList<Counteroffer> counterOffers;
    private ArrayList<xChange> finalized;
    private String location;
    private int succeed_Deals;
    private int failed_Deals;

    xChanger(String username, String email, SimpleCalendar join_date, String password, String location) {
        super(nextId++, username, email, join_date, password, location);
        items = new ArrayList<>();
        requests = new ArrayList<>();
        counterOffers = new ArrayList<>();
        finalized = new ArrayList<>();
        this.location = location;
        this.numOfratings = 0;
        this.sumOfratings = 0.0f;
        this.rating = 0;
        this.reports=new ArrayList<>();
        this.register(this);
    }

    // Implement login method
    @Override
    public boolean login(String username, String password) {
        for (User user : xchangers) {
            if (user.getPassword().equals(password) && user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean register(User user) {
        for (User temp_user : xchangers) {
            if (temp_user.getUsername().equals(user.getUsername()) || temp_user.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        xchangers.add(user);
        return true;
    }

    public static ArrayList<User> getxChangers(){
        return xchangers;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating, xChanger xchanger) {
        xchanger.numOfratings++;
        xchanger.sumOfratings += rating;
        xchanger.rating = xchanger.sumOfratings / xchanger.numOfratings;
    }

    public void setRating(Float rating) {
        this.numOfratings++;
        this.sumOfratings += rating;
        this.rating = this.sumOfratings / this.numOfratings;
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

    public void deleteItem(Item item_for_deletion) {
        this.getItems().removeIf(item -> item == item_for_deletion);
    }

    public Item getItem(Item item_to_get) {
        for (Item item : this.getItems()) {
            if (item == item_to_get) {
                return item;
            }
        }
        return null;
    }

    public void UploadItem(String item_name, String item_description, String item_category, String item_condition, ArrayList<Image> item_images) {
        this.getItems().add(new Item(item_name, item_description, item_category, item_condition, item_images));
    }

    public void RequestItem(xChanger xchanger2, Item offered_item, Item requested_item) {
        Request request = new Request(this, xchanger2, offered_item, requested_item, new SimpleCalendar(2024, 12, 3));
    }

    public void plusOneSucceedDeal() {
        this.succeed_Deals++;
    }

    public void plusOneFailedDeal() {
        this.failed_Deals++;
    }

    public void report(xChanger xchanger, String message, xChange finalized) {
        if (finalized.getStatus() != null) {
            xchanger.setRating((float) (xchanger.getRating() - 0.2));
            message = "User " + this.getUsername() + " reported user " + xchanger.getUsername();
        }
        this.reports.add(message);
    }

    public String acceptRequest(Request request) {
        String email = "";
        xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
        email = deal.acceptOffer();
        return email;
    }


    public String acceptCounteroffer(Counteroffer counteroffer) {
        String email = "";
        xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
        email = deal.acceptOffer();
        return email;
    }

    public void rejectRequest(Request request) {
        xChange deal = new xChange(request, new SimpleCalendar(2024, 12, 3));
        deal.rejectOffer();
    }

    public void rejectCounteroffer(Counteroffer counteroffer) {
        xChange deal = new xChange(counteroffer.getRequest(), counteroffer, new SimpleCalendar(2024, 12, 3));
        deal.rejectOffer();
    }

    public void counterOffer(Item item, String message, Request request) {
        if (item == null || message == null || request == null) {
            throw new IllegalArgumentException("Item, message, or request cannot be null.");
        }
        Counteroffer counter=new Counteroffer(request, message, item);
    }
    public int getSucceed_Deals(){
        return this.succeed_Deals;
    }
    public int getFailed_Deals(){
        return this.failed_Deals;
    }
    public ArrayList<String> getReports(){
        return this.reports;
    }
}