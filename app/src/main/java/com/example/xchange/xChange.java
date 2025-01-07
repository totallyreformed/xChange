package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.xchange.database.CounterofferConverter;
import com.example.xchange.database.ItemConverter;
import com.example.xchange.database.RequestConverter;
import com.example.xchange.database.CalendarConverter;
import com.example.xchange.database.XChangerConverter;

@Entity(tableName = "xchanges")
public class xChange implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "xchange_id")
    private Long xChangeId;

    @ColumnInfo(name = "deal_status")
    private String dealStatus;

    @TypeConverters(RequestConverter.class)
    @ColumnInfo(name = "request")
    private Request request;

    @TypeConverters(CounterofferConverter.class)
    @ColumnInfo(name = "counteroffer")
    private Counteroffer counteroffer;

    @ColumnInfo(name = "finalized_id")
    private Long finalizedId;

    @TypeConverters(CalendarConverter.class)
    @ColumnInfo(name = "date_finalized")
    private SimpleCalendar dateFinalized;

    @TypeConverters(XChangerConverter.class)
    @ColumnInfo(name = "offerer")
    private transient xChanger offerer; // Excluded from parceling

    @TypeConverters(XChangerConverter.class)
    @ColumnInfo(name = "offeree")
    private transient xChanger offeree; // Excluded from parceling

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "offered_item")
    private Item offeredItem;

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "requested_item")
    private Item requestedItem;

    // Constructors
    @Ignore
    public xChange(Request request, SimpleCalendar dateFinalized) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        this.request = request;
        this.finalizedId = request.getRequestId();
        this.dateFinalized = dateFinalized;
        this.dealStatus = null;
        this.offerer = request.getRequester();
        this.offeree = request.getRequestee();
        this.offeredItem = request.getOfferedItem();
        this.requestedItem = request.getRequestedItem();
    }

    public xChange(Request request, Counteroffer counteroffer, SimpleCalendar dateFinalized) {
        this.counteroffer = counteroffer;
        this.request = request;
        this.finalizedId = request.getRequestId();
        this.dateFinalized = dateFinalized;
        this.dealStatus = null;
        if (counteroffer != null) {
            this.offerer = counteroffer.getCounterofferer();
            this.offeree = counteroffer.getCounterofferee();
            this.offeredItem = counteroffer.getOfferedItem();
            this.requestedItem = counteroffer.getRequestedItem();
        } else {
            this.offerer = request.getRequester();
            this.offeree = request.getRequestee();
            this.offeredItem = request.getOfferedItem();
            this.requestedItem = request.getRequestedItem();
        }
    }

    // Getters and Setters
    public Long getXChangeId() {
        return xChangeId;
    }

    public void setXChangeId(Long xChangeId) {
        this.xChangeId = xChangeId;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        if (dealStatus == null || dealStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Deal status cannot be null or empty.");
        }
        this.dealStatus = dealStatus;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Counteroffer getCounteroffer() {
        return counteroffer;
    }

    public void setCounteroffer(Counteroffer counteroffer) {
        this.counteroffer = counteroffer;
    }

    public Long getFinalizedId() {
        return finalizedId;
    }

    public void setFinalizedId(Long finalizedId) {
        this.finalizedId = finalizedId;
    }

    public SimpleCalendar getDateFinalized() {
        return dateFinalized;
    }

    public void setDateFinalized(SimpleCalendar dateFinalized) {
        this.dateFinalized = dateFinalized;
    }

    public xChanger getOfferer() {
        return offerer;
    }

    public void setOfferer(xChanger offerer) {
        this.offerer = offerer;
    }

    public xChanger getOfferee() {
        return offeree;
    }

    public void setOfferee(xChanger offeree) {
        this.offeree = offeree;
    }

    public Item getOfferedItem() {
        return offeredItem;
    }

    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }

    public Item getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(Item requestedItem) {
        this.requestedItem = requestedItem;
    }

    // Parcelable Implementation
    protected xChange(Parcel in) {
        if (in.readByte() == 0) {
            xChangeId = null;
        } else {
            xChangeId = in.readLong();
        }
        dealStatus = in.readString();
        request = in.readParcelable(Request.class.getClassLoader());
        counteroffer = in.readParcelable(Counteroffer.class.getClassLoader());
        if (in.readByte() == 0) {
            finalizedId = null;
        } else {
            finalizedId = in.readLong();
        }
        dateFinalized = in.readParcelable(SimpleCalendar.class.getClassLoader());
        // Excluded: offerer and offeree
        this.offerer = null;
        this.offeree = null;
        offeredItem = in.readParcelable(Item.class.getClassLoader());
        requestedItem = in.readParcelable(Item.class.getClassLoader());
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
        if (xChangeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(xChangeId);
        }
        dest.writeString(dealStatus);
        dest.writeParcelable(request, flags);
        dest.writeParcelable(counteroffer, flags);
        if (finalizedId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(finalizedId);
        }
        dest.writeParcelable(dateFinalized, flags);
        // Excluded: offerer and offeree
        dest.writeParcelable(offeredItem, flags);
        dest.writeParcelable(requestedItem, flags);
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
        if (this.getCounteroffer() != null && this.getCounteroffer().getRequest().equals(this.getRequest())) {
            this.getCounteroffer().make_unactive();
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
        if (this.getCounteroffer() != null && this.getCounteroffer().getRequest().equals(this.getRequest())) {
            this.getCounteroffer().make_unactive();
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
                "xChangeId=" + xChangeId +
                ", dealStatus='" + dealStatus + '\'' +
                ", requestId=" + (request != null ? request.getRequestId() : "null") +
                ", counterofferId=" + (counteroffer != null ? counteroffer.getCounterofferId() : "null") +
                ", finalizedId=" + finalizedId +
                ", dateFinalized=" + (dateFinalized != null ? dateFinalized.toString() : "null") +
                ", offerer=" + (offerer != null ? offerer.getUsername() : "null") +
                ", offeree=" + (offeree != null ? offeree.getUsername() : "null") +
                ", offeredItem=" + (offeredItem != null ? offeredItem.getItemId() : "null") +
                ", requestedItem=" + (requestedItem != null ? requestedItem.getItemId() : "null") +
                '}';
    }
}
