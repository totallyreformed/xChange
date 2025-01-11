package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

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

/**
 * Represents an exchange deal in the xChange application.
 * <p>
 * The {@code xChange} class tracks the details of a finalized or in-progress exchange,
 * including the associated {@link Request}, {@link Counteroffer}, involved users, items, and its status.
 * This class is a Room entity and implements {@link Parcelable} for data persistence and transfer.
 * </p>
 */
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
    private  xChanger offerer; // Excluded from parceling

    @TypeConverters(XChangerConverter.class)
    @ColumnInfo(name = "offeree")
    private  xChanger offeree; // Excluded from parceling

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "offered_item")
    private Item offeredItem;

    @TypeConverters(ItemConverter.class)
    @ColumnInfo(name = "requested_item")
    private Item requestedItem;

    @ColumnInfo(name = "offerer_username")
    private String offererUsername;

    @ColumnInfo(name = "offeree_username")
    private String offereeUsername;

    /**
     * Constructs a new {@link xChange} from a {@link Request}.
     *
     * @param request       The associated {@link Request}.
     * @param dateFinalized The date the exchange was finalized.
     * @throws IllegalArgumentException if the request is {@code null}.
     */
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
        this.offererUsername = offerer != null ? offerer.getUsername() : null;
        this.offereeUsername = offeree != null ? offeree.getUsername() : null;
    }

    /**
     * Constructs a new {@link xChange} from a {@link Request} and a {@link Counteroffer}.
     *
     * @param request       The associated {@link Request}.
     * @param counteroffer  The associated {@link Counteroffer}.
     * @param dateFinalized The date the exchange was finalized.
     */
    public xChange(Request request, Counteroffer counteroffer, SimpleCalendar dateFinalized) {
        this.counteroffer = counteroffer;
        this.request = request;
        this.dateFinalized = dateFinalized;
        this.dealStatus = null;
        if (counteroffer != null) {
            this.offerer = counteroffer.getCounterofferer();
            this.offeree = counteroffer.getCounterofferee();
            this.offeredItem = counteroffer.getOfferedItem();
            this.requestedItem = counteroffer.getRequestedItem();
            this.finalizedId = counteroffer.getCounterofferId();
        } else {
            this.offerer = request.getRequester();
            this.offeree = request.getRequestee();
            this.offeredItem = request.getOfferedItem();
            this.requestedItem = request.getRequestedItem();
            this.finalizedId = request.getRequestId();
        }
    }

    /**
     * Returns the username of the offerer.
     *
     * @return The username of the offerer.
     */
    public String getOffererUsername() {
        return offererUsername;
    }

    /**
     * Sets the username of the offerer.
     *
     * @param offererUsername The username to set.
     */
    public void setOffererUsername(String offererUsername) {
        this.offererUsername = offererUsername;
    }

    /**
     * Returns the username of the offeree.
     *
     * @return The username of the offeree.
     */
    public String getOffereeUsername() {
        return offereeUsername;
    }

    /**
     * Sets the username of the offeree.
     *
     * @param offereeUsername The username to set.
     */
    public void setOffereeUsername(String offereeUsername) {
        this.offereeUsername = offereeUsername;
    }


    /**
     * Gets the unique ID of the xChange.
     *
     * @return The xChange ID.
     */
    public Long getXChangeId() {
        return xChangeId;
    }

    /**
     * Sets the unique ID of the xChange.
     *
     * @param xChangeId The ID to set.
     */
    public void setXChangeId(Long xChangeId) {
        this.xChangeId = xChangeId;
    }

    /**
     * Gets the current deal status of the xChange.
     *
     * @return The deal status as a {@link String}.
     */
    public String getDealStatus() {
        return dealStatus;
    }

    /**
     * Sets the deal status of the xChange.
     *
     * @param dealStatus The deal status to set.
     */
    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    /**
     * Gets the associated {@link Request} of the xChange.
     *
     * @return The associated {@link Request}.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the associated {@link Request} of the xChange.
     *
     * @param request The {@link Request} to set.
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Gets the associated {@link Counteroffer} of the xChange.
     *
     * @return The associated {@link Counteroffer}.
     */
    public Counteroffer getCounteroffer() {
        return counteroffer;
    }

    /**
     * Sets the associated {@link Counteroffer} of the xChange.
     *
     * @param counteroffer The {@link Counteroffer} to set.
     */
    public void setCounteroffer(Counteroffer counteroffer) {
        this.counteroffer = counteroffer;
    }

    /**
     * Gets the finalized ID of the xChange.
     *
     * @return The finalized ID.
     */
    public Long getFinalizedId() {
        return finalizedId;
    }

    /**
     * Sets the finalized ID of the xChange.
     *
     * @param finalizedId The finalized ID to set.
     */
    public void setFinalizedId(Long finalizedId) {
        this.finalizedId = finalizedId;
    }

    /**
     * Gets the date the xChange was finalized.
     *
     * @return The finalized date as a {@link SimpleCalendar}.
     */
    public SimpleCalendar getDateFinalized() {
        return dateFinalized;
    }

    /**
     * Sets the date the xChange was finalized.
     *
     * @param dateFinalized The finalized date to set as a {@link SimpleCalendar}.
     */
    public void setDateFinalized(SimpleCalendar dateFinalized) {
        this.dateFinalized = dateFinalized;
    }

    /**
     * Gets the offerer involved in the xChange.
     *
     * @return The offerer as a {@link xChanger}.
     */
    public xChanger getOfferer() {
        return offerer;
    }

    /**
     * Sets the offerer involved in the xChange.
     *
     * @param offerer The offerer to set as a {@link xChanger}.
     */
    public void setOfferer(xChanger offerer) {
        this.offerer = offerer;
    }

    /**
     * Gets the offeree involved in the xChange.
     *
     * @return The offeree as a {@link xChanger}.
     */
    public xChanger getOfferee() {
        return offeree;
    }

    /**
     * Sets the offeree involved in the xChange.
     *
     * @param offeree The offeree to set as a {@link xChanger}.
     */
    public void setOfferee(xChanger offeree) {
        this.offeree = offeree;
    }

    /**
     * Gets the offered {@link Item} in the xChange.
     *
     * @return The offered {@link Item}.
     */
    public Item getOfferedItem() {
        return offeredItem;
    }

    /**
     * Sets the offered {@link Item} in the xChange.
     *
     * @param offeredItem The offered {@link Item} to set.
     */
    public void setOfferedItem(Item offeredItem) {
        this.offeredItem = offeredItem;
    }

    /**
     * Gets the requested {@link Item} in the xChange.
     *
     * @return The requested {@link Item}.
     */
    public Item getRequestedItem() {
        return requestedItem;
    }

    /**
     * Sets the requested {@link Item} in the xChange.
     *
     * @param requestedItem The requested {@link Item} to set.
     */
    public void setRequestedItem(Item requestedItem) {
        this.requestedItem = requestedItem;
    }

    /**
     * Constructs an {@link xChange} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link xChange}.
     */
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
        offerer = in.readParcelable(xChanger.class.getClassLoader());
        offeree = in.readParcelable(xChanger.class.getClassLoader());
        offeredItem = in.readParcelable(Item.class.getClassLoader());
        requestedItem = in.readParcelable(Item.class.getClassLoader());
        offererUsername = in.readString();
        offereeUsername = in.readString();
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link xChange} objects from a {@link Parcel}.
     */
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

    /**
     * Writes the {@link xChange} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
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
        dest.writeParcelable(offerer, flags);
        dest.writeParcelable(offeree, flags);
        dest.writeParcelable(offeredItem, flags);
        dest.writeParcelable(requestedItem, flags);
        dest.writeString(offererUsername);
        dest.writeString(offereeUsername);
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
     * Accepts the exchange offer and finalizes the deal.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Sets the deal status to "Accepted".</li>
     *     <li>Removes the requested and offered items from the inventories of the offerer and offeree.</li>
     *     <li>Marks the associated {@link Request} as inactive.</li>
     *     <li>Deactivates the associated {@link Counteroffer}, if present.</li>
     *     <li>Updates the deal statistics for both the offerer and offeree.</li>
     *     <li>Creates and assigns a {@link Rating} for the offeree based on the provided rating value.</li>
     * </ul>
     * </p>
     *
     * @param ratingValue The rating value (e.g., between 0 and 5) to assign to the offeree.
     * @return The email of the offeree.
     */
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

    /**
     * Accepts the exchange offer and finalizes the deal.
     * <p>
     * This method performs the following steps:
     * <ul>
     *     <li>Sets the deal status to "Accepted".</li>
     *     <li>Removes the requested and offered items from the inventories of the offerer and offeree.</li>
     *     <li>Marks the associated {@link Request} as inactive.</li>
     *     <li>Deactivates the associated {@link Counteroffer}, if present.</li>
     *     <li>Updates the deal statistics for both the offerer and offeree.</li>
     *     <li>Creates and assigns a {@link Rating} for the offeree based on the provided rating value.</li>
     * </ul>
     * </p>
     *
     * @param ratingValue The rating value (e.g., between 0 and 5) to assign to the offeree.
     * @return The email of the offeree.
     */
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

    /**
     * Returns a string representation of the {@link xChange}.
     *
     * @return A string containing details about the xChange, including IDs, deal status, usernames, and items involved.
     */
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
