package com.example.xchange;

public class Counteroffer {
    private final Request request;
    private Item offered_item;
    private Item requested_item;
    private final xChanger counterofferer;
    private final xChanger counterofferee;
    private final Long counteroffer_id;
    private String message;
    private Boolean active;

    public Counteroffer(Request request, String message, Item item) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Offered item cannot be null.");
        }

        this.counterofferer = request.getRequester();
        this.counterofferee = request.getRequestee();
        this.request = request;
        this.offered_item = item;
        this.requested_item = request.getRequestedItem();
        this.counteroffer_id = request.getRequestID();
        this.message = message;
        this.active = true;

        addToLists();
    }

    // Getters
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

    public Boolean isActive() {
        return active;
    }

    // Setters
    public void setRequestedItem(Item requested_item) {
        if (requested_item == null) {
            throw new IllegalArgumentException("Requested item cannot be null.");
        }
        this.requested_item = requested_item;
    }

    public void setOfferedItem(Item offered_item) {
        if (offered_item == null) {
            throw new IllegalArgumentException("Offered item cannot be null.");
        }
        this.offered_item = offered_item;
    }

    public void setMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty.");
        }
        this.message = message;
    }

    // Additional Methods
    private void addToLists() {
        this.counterofferee.getCounterOffers().add(this);
        this.getCounterofferer().getCounterOffers().add(this);
    }

    public void make_unactive() {
        this.active = false;
    }
}