package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

public class xChange implements Parcelable {
    private String deal_status;
    private Request request;
    private Counteroffer counteroffer;
    private Long finalized_id;
    private SimpleCalendar date_finalized;
    private transient xChanger offerer;    // Excluded from parceling
    private transient xChanger offeree;    // Excluded from parceling
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
        if (counteroffer != null) {
            this.offerer = counteroffer.getCounterofferer();
            this.offeree = counteroffer.getCounterofferee();
            this.offered_item = counteroffer.getOfferedItem();
            this.requested_item = counteroffer.getRequestedItem();
        } else {
            this.offerer = request.getRequester();
            this.offeree = request.getRequestee();
            this.offered_item = request.getOfferedItem();
            this.requested_item = request.getRequestedItem();
        }
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


    // Parcelable Constructor
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
        // Excluded: offerer and offeree
        this.offerer = null;
        this.offeree = null;
        offered_item = in.readParcelable(Item.class.getClassLoader());
        requested_item = in.readParcelable(Item.class.getClassLoader());
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
        // Excluded: offerer and offeree
        dest.writeParcelable(offered_item, flags);
        dest.writeParcelable(requested_item, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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


    // toString method for better debugging
    @Override
    public String toString() {
        return "xChange{" +
                "deal_status='" + deal_status + '\'' +
                ", requestId=" + (request != null ? request.getRequestId() : "null") +
                ", counterofferId=" + (counteroffer != null ? counteroffer.getCounterofferId() : "null") +
                ", finalized_id=" + finalized_id +
                ", date_finalized=" + (date_finalized != null ? date_finalized.toString() : "null") +
                ", offerer=" + (offerer != null ? offerer.getUsername() : "null") +
                ", offeree=" + (offeree != null ? offeree.getUsername() : "null") +
                ", offered_item=" + (offered_item != null ? offered_item.getItemId() : "null") +
                ", requested_item=" + (requested_item != null ? requested_item.getItemId() : "null") +
                '}';
    }
}
