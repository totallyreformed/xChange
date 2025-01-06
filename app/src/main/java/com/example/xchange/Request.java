package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.xchange.database.CalendarConverter;
import com.example.xchange.database.ItemConverter;
import com.example.xchange.database.XChangerConverter;

import java.util.Objects;

@Entity(tableName = "requests")
public class Request implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long requestId;

    @ColumnInfo(name = "requester")
    @TypeConverters(XChangerConverter.class)
    private xChanger requester;

    @ColumnInfo(name = "requestee")
    @TypeConverters(XChangerConverter.class)
    private xChanger requestee;

    @ColumnInfo(name = "offered_item")
    @TypeConverters(ItemConverter.class)
    private Item offeredItem;

    @ColumnInfo(name = "requested_item")
    @TypeConverters(ItemConverter.class)
    private Item requestedItem;

    @ColumnInfo(name = "date_initiated")
    @TypeConverters(CalendarConverter.class)
    private SimpleCalendar dateInitiated;

    @ColumnInfo(name = "active")
    private boolean active;

    // Default constructor for Room
    public Request() {}

    // Constructor for creating objects
    public Request(xChanger requester, xChanger requestee, Item offeredItem, Item requestedItem, SimpleCalendar dateInitiated) {
        this.requester = requester;
        this.requestee = requestee;
        this.offeredItem = offeredItem;
        this.requestedItem = requestedItem;
        this.dateInitiated = dateInitiated;
        this.active = true;
    }

    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public xChanger getRequester() {
        return requester;
    }

    public void setRequester(xChanger requester) {
        this.requester = requester;
    }

    public xChanger getRequestee() {
        return requestee;
    }

    public void setRequestee(xChanger requestee) {
        this.requestee = requestee;
    }

    public Item getOfferedItem() {
        return offeredItem;
    }

    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }
    public void make_unactive(){
        this.active=false;
    }
    public String getStatus(){
        if (this.active){
            return "Active";
        }else{
            return "Inactive";
        }
    }


    public Item getRequestedItem() {
        return requestedItem;
    }

    public void setRequestedItem(Item requestedItem) {
        this.requestedItem = requestedItem;
    }

    public SimpleCalendar getDateInitiated() {
        return dateInitiated;
    }

    public void setDateInitiated(SimpleCalendar dateInitiated) {
        this.dateInitiated = dateInitiated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // Parcelable Implementation
    protected Request(Parcel in) {
        // Read requestId
        if (in.readByte() == 0) {
            requestId = null;
        } else {
            requestId = in.readLong();
        }

        // Read Parcelable objects
        requester = in.readParcelable(xChanger.class.getClassLoader());
        requestee = in.readParcelable(xChanger.class.getClassLoader());
        offeredItem = in.readParcelable(Item.class.getClassLoader());
        requestedItem = in.readParcelable(Item.class.getClassLoader());

        // Read SimpleCalendar directly if it's Parcelable
        dateInitiated = in.readParcelable(SimpleCalendar.class.getClassLoader());

        // Read active flag
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write requestId
        if (requestId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(requestId);
        }

        // Write Parcelable objects
        dest.writeParcelable(requester, flags);
        dest.writeParcelable(requestee, flags);
        dest.writeParcelable(offeredItem, flags);
        dest.writeParcelable(requestedItem, flags);

        // Write SimpleCalendar directly if it's Parcelable
        dest.writeParcelable(dateInitiated, flags);

        // Write active flag
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public String toString() {
        return "Request ID: " + requestId + " " +
                "Requester: " + requester.getUsername() + " " +
                "Requestee: " + requestee.getUsername() + " ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;
        return Objects.equals(requestId, request.requestId); // Compare based on unique identifier
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
