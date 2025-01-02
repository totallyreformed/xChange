package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

public class Counteroffer implements Parcelable {
    private final Request request;
    private Item offered_item;
    private Item requested_item;
    private final xChanger counterofferer;
    private final xChanger counterofferee;
    private final Long counteroffer_id; // Derived from the request's Room-generated ID
    private String message;
    private Boolean active;

    // Constructor
    public Counteroffer(Request request, String message, Item item) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        if (item == null) {
            throw new IllegalArgumentException("Offered item cannot be null.");
        }
        if (request.getRequestId() == null) {
            throw new IllegalStateException("Request ID cannot be null. Ensure the Request is saved in the database.");
        }

        this.counterofferer = request.getRequester();
        this.counterofferee = request.getRequestee();
        this.request = request;
        this.offered_item = item;
        this.requested_item = request.getRequestedItem();
        this.counteroffer_id = request.getRequestId(); // Use the Room-managed ID
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

    // Parcelable Implementation
    protected Counteroffer(Parcel in) {
        request = in.readParcelable(Request.class.getClassLoader());
        offered_item = in.readParcelable(Item.class.getClassLoader());
        requested_item = in.readParcelable(Item.class.getClassLoader());
        counterofferer = in.readParcelable(xChanger.class.getClassLoader());
        counterofferee = in.readParcelable(xChanger.class.getClassLoader());
        counteroffer_id = in.readByte() == 0 ? null : in.readLong();
        message = in.readString();
        active = in.readByte() != 0;
    }

    public static final Creator<Counteroffer> CREATOR = new Creator<Counteroffer>() {
        @Override
        public Counteroffer createFromParcel(Parcel in) {
            return new Counteroffer(in);
        }

        @Override
        public Counteroffer[] newArray(int size) {
            return new Counteroffer[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(request, flags);
        dest.writeParcelable(offered_item, flags);
        dest.writeParcelable(requested_item, flags);
        dest.writeParcelable(counterofferer, flags);
        dest.writeParcelable(counterofferee, flags);
        if (counteroffer_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(counteroffer_id);
        }
        dest.writeString(message);
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
