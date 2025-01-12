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

/**
 * Represents a trade request in the xChange application.
 * <p>
 * A request is initiated by a user (requester) to exchange an item with another user (requestee).
 * This class is a Room entity and implements {@link Parcelable} for data persistence and transfer.
 * </p>
 */
@Entity(tableName = "requests")
public class Request implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private Long requestId;

    @ColumnInfo(name = "requester")
    @TypeConverters(XChangerConverter.class)
    private transient xChanger requester;

    @ColumnInfo(name = "requestee")
    @TypeConverters(XChangerConverter.class)
    private transient xChanger requestee;

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

    /**
     * Default constructor for Room.
     */
    public Request() {}

    /**
     * Constructs a new {@link Request}.
     *
     * @param requester      The {@link xChanger} who initiated the request.
     * @param requestee      The {@link xChanger} who receives the request.
     * @param offeredItem    The {@link Item} being offered in the trade.
     * @param requestedItem  The {@link Item} being requested in the trade.
     * @param dateInitiated  The {@link SimpleCalendar} representing the date the request was initiated.
     */
    public Request(xChanger requester, xChanger requestee, Item offeredItem, Item requestedItem, SimpleCalendar dateInitiated) {
        this.requester = requester;
        this.requestee = requestee;
        this.offeredItem = offeredItem;
        this.requestedItem = requestedItem;
        this.dateInitiated = dateInitiated;
        this.active = true;
    }


    /**
     * Gets the unique ID of the request.
     *
     * @return The request ID.
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique ID of the request.
     *
     * @param requestId The ID to set.
     */
    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets the {@link xChanger} who initiated the request.
     *
     * @return The requester.
     */
    public xChanger getRequester() {
        return requester;
    }

    /**
     * Sets the {@link xChanger} who initiated the request.
     *
     * @param requester The requester to set.
     */
    public void setRequester(xChanger requester) {
        this.requester = requester;
    }

    /**
     * Gets the {@link xChanger} who receives the request.
     *
     * @return The requestee.
     */
    public xChanger getRequestee() {
        return requestee;
    }

    /**
     * Sets the {@link xChanger} who receives the request.
     *
     * @param requestee The requestee to set.
     */
    public void setRequestee(xChanger requestee) {
        this.requestee = requestee;
    }

    /**
     * Gets the {@link Item} being offered in the request.
     *
     * @return The offered item.
     */
    public Item getOfferedItem() {
        return offeredItem;
    }

    /**
     * Sets the {@link Item} being offered in the request.
     *
     * @param offeredItem The offered item to set.
     */
    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }

    /**
     * Marks the request as inactive.
     */
    public void make_unactive(){
        this.active = false;
    }

    /**
     * Gets the status of the request as a string.
     *
     * @return "Active" if the request is active, otherwise "Inactive".
     */
    public String getStatus(){
        return this.active ? "Active" : "Inactive";
    }

    /**
     * Gets the {@link Item} being requested in the trade.
     *
     * @return The requested item.
     */
    public Item getRequestedItem() {
        return requestedItem;
    }

    /**
     * Sets the {@link Item} being requested in the trade.
     *
     * @param requestedItem The requested item to set.
     */
    public void setRequestedItem(Item requestedItem) {
        this.requestedItem = requestedItem;
    }

    /**
     * Gets the date the request was initiated.
     *
     * @return The date initiated as a {@link SimpleCalendar}.
     */
    public SimpleCalendar getDateInitiated() {
        return dateInitiated;
    }

    /**
     * Sets the date the request was initiated.
     *
     * @param dateInitiated The date to set as a {@link SimpleCalendar}.
     */
    public void setDateInitiated(SimpleCalendar dateInitiated) {
        this.dateInitiated = dateInitiated;
    }

    /**
     * Checks whether the request is active.
     *
     * @return {@code true} if the request is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the active status of the request.
     *
     * @param active {@code true} to activate the request; {@code false} to deactivate it.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Constructs a {@link Request} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link Request}.
     */
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

    /**
     * Writes the {@link Request} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
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

    /**
     * Describes the contents of the {@link Parcelable} implementation.
     *
     * @return Always returns 0 as no special objects are contained.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link Request} objects from a {@link Parcel}.
     */
    public static final Creator<Request> CREATOR = new Creator<Request>() {
        /**
         * Creates a {@link Request} instance from the provided {@link Parcel}.
         *
         * @param in The {@link Parcel} containing the serialized {@link Request}.
         * @return A new {@link Request} object populated with data from the parcel.
         */
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        /**
         * Creates a new array of {@link Request} objects.
         *
         * @param size The size of the array to create.
         * @return An array of {@link Request} objects, with all elements initialized to {@code null}.
         */

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    /**
     * Returns a string representation of the {@link Request}.
     *
     * @return A string representation including the request ID, requester, and requestee usernames.
     */
    @Override
    public String toString() {
        return "Request ID: " + requestId + " " +
                "Requester: " + requester.getUsername() + " " +
                "Requestee: " + requestee.getUsername() + " ";
    }

    /**
     * Checks whether this {@link Request} is equal to another object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;
        return Objects.equals(requestId, request.requestId); // Compare based on unique identifier
    }

    /**
     * Generates a hash code for the {@link Request}.
     *
     * @return The hash code for the request.
     */
    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
