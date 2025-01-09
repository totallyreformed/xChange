package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.TypeConverters;

import com.example.xchange.database.ItemConverter;
import com.example.xchange.database.RequestConverter;
import com.example.xchange.database.XChangerConverter;

@Entity(tableName = "counteroffers")
public class Counteroffer implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "counteroffer_id")
    private Long counterofferId;

    @TypeConverters(RequestConverter.class)
    @ColumnInfo(name = "request")
    private Request request;

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "offered_item")
    private Item offeredItem;

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "requested_item")
    private Item requestedItem;

    @ColumnInfo(name = "counterofferer")
    @TypeConverters(XChangerConverter.class)
    private xChanger counterofferer; // Included in parceling

    @TypeConverters(XChangerConverter.class)
    @ColumnInfo(name = "counterofferee")
    private xChanger counterofferee; // Included in parceling

    private Boolean active;

    // Constructor
    public Counteroffer(Request request, Item offeredItem, xChanger counterofferer, xChanger counterofferee) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        if (offeredItem == null) {
            throw new IllegalArgumentException("Offered item cannot be null.");
        }
        if (counterofferer == null || counterofferee == null) {
            throw new IllegalArgumentException("Counterofferer and Counterofferee cannot be null.");
        }
        this.request = request;
        this.offeredItem = offeredItem;
        this.requestedItem = request.getRequestedItem();
        this.counterofferer = counterofferer;
        this.counterofferee = counterofferee;
        this.active = true;
    }

    // Parcelable Constructor
    protected Counteroffer(Parcel in) {
        if (in.readByte() == 0) {
            counterofferId = null;
        } else {
            counterofferId = in.readLong();
        }
        request = in.readParcelable(Request.class.getClassLoader());
        offeredItem = in.readParcelable(Item.class.getClassLoader());
        requestedItem = in.readParcelable(Item.class.getClassLoader());
        counterofferer = in.readParcelable(xChanger.class.getClassLoader());
        counterofferee = in.readParcelable(xChanger.class.getClassLoader());
        byte activeByte = in.readByte();
        active = activeByte == 1;
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
        if (counterofferId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(counterofferId);
        }
        dest.writeParcelable(request, flags);
        dest.writeParcelable(offeredItem, flags);
        dest.writeParcelable(requestedItem, flags);
        dest.writeParcelable(counterofferer, flags); // Included in parceling
        dest.writeParcelable(counterofferee, flags); // Included in parceling
        dest.writeByte((byte) (active != null && active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
    public Long getCounterofferId() {
        return counterofferId;
    }

    public void setCounterofferId(Long counterofferId) {
        this.counterofferId = counterofferId;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
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

    public xChanger getCounterofferer() {
        return counterofferer;
    }

    public void setCounterofferer(xChanger counterofferer) {
        this.counterofferer = counterofferer;
    }

    public xChanger getCounterofferee() {
        return counterofferee;
    }

    public void setCounterofferee(xChanger counterofferee) {
        this.counterofferee = counterofferee;
    }

    public Boolean isActive() {
        return active;
    }

    public void make_unactive() {
        this.active = false;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // toString method for better debugging
    @Override
    public String toString() {
        return "Counteroffer{" +
                "counterofferId=" + counterofferId +
                ", requestId=" + (request != null ? request.getRequestId() : "null") +
                ", offeredItem=" + (offeredItem != null ? offeredItem.getItemId() : "null") +
                ", requestedItem=" + (requestedItem != null ? requestedItem.getItemId() : "null") +
                ", counterofferer=" + (counterofferer != null ? counterofferer.getUsername() : "null") +
                ", counterofferee=" + (counterofferee != null ? counterofferee.getUsername() : "null") +
                ", active=" + active +
                '}';
    }
}
