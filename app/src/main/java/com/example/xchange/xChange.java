package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

public class xChange implements Parcelable {
    private String deal_status;
    private Request request;
    private Counteroffer counteroffer;
    private Long finalized_id;
    private SimpleCalendar date_finalized;
    private xChanger offerer;
    private xChanger offeree;
    private Item offered_item;
    private Item requested_item;

    // Constructors
    public xChange(Request request, SimpleCalendar date_finalized) {
        this.request = request;
        this.finalized_id = request.getRequestId();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = request.getRequester();
        this.offeree = request.getRequestee();
        this.offered_item = request.getOfferedItem();
        this.requested_item = request.getRequestedItem();
    }

    public xChange(Request request, Counteroffer counteroffer, SimpleCalendar date_finalized) {
        this.counteroffer = counteroffer;
        this.request = request;
        this.finalized_id = request.getRequestId();
        this.date_finalized = date_finalized;
        this.deal_status = null;
        this.offerer = counteroffer.getCounterofferer();
        this.offeree = counteroffer.getCounterofferee();
        this.offered_item = counteroffer.getOfferedItem();
        this.requested_item = counteroffer.getRequestedItem();
    }

    // Getters
    public Request getRequest() {
        return request;
    }

    public Counteroffer getCounterOffer() {
        return counteroffer;
    }

    public Long getFinalizedID() {
        return finalized_id;
    }

    public SimpleCalendar getDateFinalized() {
        return date_finalized;
    }

    public xChanger getOfferer() {
        return offerer;
    }

    public xChanger getOfferee() {
        return offeree;
    }

    public Item getOfferedItem() {
        return offered_item;
    }

    public Item getRequestedItem() {
        return requested_item;
    }

    public void setDealStatus(String deal_status) {
        if (deal_status == null || deal_status.trim().isEmpty()) {
            throw new IllegalArgumentException("Deal status cannot be null or empty.");
        }
        this.deal_status = deal_status;
    }

    public String getStatus() {
        return this.deal_status;
    }

    // Parcelable Implementation
    protected xChange(Parcel in) {
        deal_status = in.readString();
        request = in.readParcelable(Request.class.getClassLoader());
        counteroffer = in.readParcelable(Counteroffer.class.getClassLoader());
        if (in.readByte() == 0) {
            finalized_id = null;
        } else {
            finalized_id = in.readLong();
        }
        date_finalized = in.readParcelable(SimpleCalendar.class.getClassLoader());
        offerer = in.readParcelable(xChanger.class.getClassLoader());
        offeree = in.readParcelable(xChanger.class.getClassLoader());
        offered_item = in.readParcelable(Item.class.getClassLoader());
        requested_item = in.readParcelable(Item.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deal_status);
        dest.writeParcelable(request, flags);
        dest.writeParcelable(counteroffer, flags);
        if (finalized_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(finalized_id);
        }
        dest.writeParcelable(date_finalized, flags);
        dest.writeParcelable(offerer, flags);
        dest.writeParcelable(offeree, flags);
        dest.writeParcelable(offered_item, flags);
        dest.writeParcelable(requested_item, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<xChange> CREATOR = new Creator<xChange>() {
        @Override
        public xChange createFromParcel(Parcel in) {
            return new xChange(in);
        }

        @Override
        public xChange[] newArray(int size) {
            return new xChange[size];
        }
    };


    // Methods to accept or reject the offer
    public String acceptOffer(float ratingValue) {
        this.setDealStatus("Accepted");

        // Remove items from inventories
        this.getOfferer().deleteItem(this.getRequestedItem());
        this.getOfferee().deleteItem(this.getOfferedItem());

        // Finalize the exchange
        this.getOfferee().getFinalized().add(this);
        this.getOfferer().getFinalized().add(this);
        this.getRequest().make_unactive();

        // Deactivate counteroffer if present
        if (this.getCounterOffer() != null && this.getCounterOffer().getRequest() == this.getRequest()) {
            this.getCounterOffer().make_unactive();
        }

        // Update deals statistics
        this.getOfferee().plusOneSucceedDeal();
        this.getOfferer().plusOneSucceedDeal();

        // Add rating to offeree
        Rating rating = new Rating(ratingValue, this.getOfferer(), this.getOfferee(), this.getRequest(), this);
        this.getOfferee().addRating(rating);

        return this.getOfferee().getEmail();
    }

    public void rejectOffer(float ratingValue) {
        this.setDealStatus("Rejected");

        // Finalize the exchange
        this.getOfferee().getFinalized().add(this);
        this.getOfferer().getFinalized().add(this);
        this.getRequest().make_unactive();

        // Deactivate counteroffer if present
        if (this.getCounterOffer() != null && this.getCounterOffer().getRequest() == this.getRequest()) {
            this.getCounterOffer().make_unactive();
        }

        // Update deals statistics
        this.getOfferee().plusOneFailedDeal();
        this.getOfferer().plusOneFailedDeal();

        // Add rating to offeree
        Rating rating = new Rating(ratingValue, this.getOfferer(), this.getOfferee(), this.getRequest(), this);
        this.getOfferee().addRating(rating);
    }
}
