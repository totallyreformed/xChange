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

/**
 * Represents a counteroffer in the xChange application.
 * <p>
 * A counteroffer is a response to a {@link Request}, containing an offered item and requested item.
 * It is associated with a counterofferer and counterofferee.
 * This class is a Room entity and implements {@link Parcelable} for data persistence and transfer.
 * </p>
 */
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
    private xChanger counterofferer; // Excluded from parceling

    @TypeConverters(XChangerConverter.class)
    @ColumnInfo(name = "counterofferee")
    private xChanger counterofferee; // Excluded from parceling

    private Boolean active;

    /**
     * Constructs a new {@code Counteroffer}.
     *
     * @param request        The associated {@link Request}.
     * @param offeredItem    The {@link Item} being offered in the counteroffer.
     * @param counterofferer The {@link xChanger} making the counteroffer.
     * @param counterofferee The {@link xChanger} receiving the counteroffer.
     * @throws IllegalArgumentException if {@code request} or {@code offeredItem} is {@code null}.
     */
    public Counteroffer(Request request, Item offeredItem, xChanger counterofferer, xChanger counterofferee) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null.");
        }
        if (offeredItem == null) {
            throw new IllegalArgumentException("Offered item cannot be null.");
        }
        this.request = request;
        this.offeredItem = offeredItem;
        this.requestedItem = request.getRequestedItem();
        this.counterofferer = counterofferer;
        this.counterofferee = counterofferee;
        this.active = true;
    }

    /**
     * Constructs a {@code Counteroffer} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@code Counteroffer}.
     */
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
        active = in.readByte() != 0;
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link Counteroffer} objects from a {@link Parcel}.
     */
    public static final Creator<Counteroffer> CREATOR = new Creator<Counteroffer>() {

        /**
         * Creates a {@link Counteroffer} from a {@link Parcel}.
         *
         * @param in The {@link Parcel} containing the serialized data.
         * @return A new {@link Counteroffer} object.
         */
        @Override
        public Counteroffer createFromParcel(Parcel in) {
            return new Counteroffer(in);
        }

        /**
         * Creates a new array of {@link Counteroffer} objects.
         *
         * @param size The size of the array.
         * @return An array of {@link Counteroffer} objects.
         */
        @Override
        public Counteroffer[] newArray(int size) {
            return new Counteroffer[size];
        }
    };

    /**
     * Writes the {@link Counteroffer} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write the object's data to.
     * @param flags Additional flags about how the object should be written.
     */
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
        dest.writeParcelable(counterofferer, flags);
        dest.writeParcelable(counterofferee, flags);
        dest.writeByte((byte) (active != null && active ? 1 : 0));
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

    // Getters and Setters

    /**
     * Gets the unique ID of the counteroffer.
     *
     * @return The counteroffer ID.
     */
    public Long getCounterofferId() {
        return counterofferId;
    }

    /**
     * Sets the unique ID of the counteroffer.
     *
     * @param counterofferId The ID to set.
     */
    public void setCounterofferId(Long counterofferId) {
        this.counterofferId = counterofferId;
    }

    /**
     * Gets the associated {@link Request}.
     *
     * @return The associated request.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the associated {@link Request}.
     *
     * @param request The request to set.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the {@link Item} being offered.
     *
     * @return The offered item.
     */
    public Item getOfferedItem() {
        return offeredItem;
    }

    /**
     * Sets the {@link Item} being offered.
     *
     * @param offeredItem The offered item to set.
     */
    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }

    /**
     * Gets the {@link Item} being requested.
     *
     * @return The requested item.
     */
    public Item getRequestedItem() {
        return requestedItem;
    }

    /**
     * Sets the {@link Item} being requested.
     *
     * @param requestedItem The requested item to set.
     */
    public void setRequestedItem(Item requestedItem) {
        this.requestedItem = requestedItem;
    }

    /**
     * Gets the {@link xChanger} making the counteroffer.
     *
     * @return The counterofferer.
     */
    public xChanger getCounterofferer() {
        return counterofferer;
    }

    /**
     * Sets the {@link xChanger} making the counteroffer.
     *
     * @param counterofferer The counterofferer to set.
     */
    public void setCounterofferer(xChanger counterofferer) {
        this.counterofferer = counterofferer;
    }

    /**
     * Gets the {@link xChanger} receiving the counteroffer.
     *
     * @return The counterofferee.
     */
    public xChanger getCounterofferee() {
        return counterofferee;
    }

    /**
     * Sets the {@link xChanger} receiving the counteroffer.
     *
     * @param counterofferee The counterofferee to set.
     */
    public void setCounterofferee(xChanger counterofferee) {
        this.counterofferee = counterofferee;
    }

    /**
     * Checks whether the counteroffer is active.
     *
     * @return {@code true} if the counteroffer is active; {@code false} otherwise.
     */
    public Boolean isActive() {
        return active;
    }

    /**
     * Deactivates the counteroffer.
     */
    public void make_unactive() {
        this.active = false;
    }

    /**
     * Sets the active status of the counteroffer.
     *
     * @param active {@code true} to activate the counteroffer; {@code false} to deactivate it.
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Returns a string representation of the counteroffer.
     *
     * @return A string representation of the counteroffer.
     */
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
