package com.example.xchange;

public class Counteroffer {
    private final Request request;
    private Item offered_item;
    private Item requested_item;
    private final xChanger counterofferer;
    private final xChanger counterofferee;
    private final Long counteroffer_id;
    private String message;

    Counteroffer(Request request, String message) {
        this.request = request;
        this.offered_item = request.getOfferedItem();
        this.requested_item = request.getRequestedItem();
        this.counterofferer = request.getRequester();
        this.counterofferee = request.getRequestee();
        this.counteroffer_id = request.getRequestID();
        this.message = message;
        add_to_lists();
    }

    public Request getRequest() {
        return request;
    }

    public Item getOfferedItem() {
        return offered_item;
    }

    public Item getRequestedItem() {
        return requested_item;
    }

    public xChanger getCounterofferer() {
        return counterofferer;
    }

    public xChanger getCounterofferee() {
        return counterofferee;
    }

    public Long getCounterofferID() {
        return counteroffer_id;
    }

    public String getMessage() {
        return message;
    }

    public void setRequestedItem(Item requested_item) {
        this.requested_item = requested_item;
    }

    public void setOfferedItem(Item offered_item) {
        this.offered_item = offered_item;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void add_to_lists(){
        this.getCounterofferee().getCounterOffers().add(this);
    }
}