package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.xchange.database.AppDatabase;

import java.util.ArrayList;

/**
 * Represents an xChanger user in the xChange application.
 * <p>
 * An xChanger is a type of {@link User} who can upload items, make requests,
 * counteroffer, and participate in exchanges. This class includes functionality
 * for managing items, ratings, deals, and reports, and implements {@link Parcelable}
 * for data transfer.
 * </p>
 */
public class xChanger extends User implements Parcelable {
    private float averageRating;
    private int totalRatings;
    private transient ArrayList<Rating> ratings;       // Excluded from parceling
    private transient ArrayList<String> reports;        // Excluded from parceling
    private transient ArrayList<Item> items;            // Excluded from parceling
    private transient ArrayList<Request> requests;      // Excluded from parceling
    private transient ArrayList<Counteroffer> counterOffers; // Excluded from parceling
    private transient ArrayList<xChange> finalized;      // Excluded from parceling
    private int succeedDeals;
    private int failedDeals;

    /**
     * Constructs a new {@link xChanger}.
     *
     * @param username  The username of the xChanger.
     * @param email     The email address of the xChanger.
     * @param join_date The date the xChanger joined, as a {@link SimpleCalendar}.
     * @param password  The password of the xChanger.
     * @param location  The location of the xChanger.
     */
    public xChanger(String username, String email, SimpleCalendar join_date, String password, String location) {
        super(username, email, join_date, password, location, "xChanger");
        this.averageRating = 0;
        this.totalRatings = 0;
        this.ratings = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.items = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.counterOffers = new ArrayList<>();
        this.finalized = new ArrayList<>();
        this.succeedDeals = 0;
        this.failedDeals = 0;
    }

    /**
     * Constructs an {@link xChanger} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link xChanger}.
     */
    protected xChanger(Parcel in) {
        super(in); // Call the User's Parcelable constructor
        averageRating = in.readFloat();
        totalRatings = in.readInt();
        // Initialize transient fields as empty lists
        this.ratings = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.items = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.counterOffers = new ArrayList<>();
        this.finalized = new ArrayList<>();
        succeedDeals = in.readInt();
        failedDeals = in.readInt();
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link xChanger} objects from a {@link Parcel}.
     */
    public static final Creator<xChanger> CREATOR = new Creator<xChanger>() {
        @Override
        public xChanger createFromParcel(Parcel in) {
            return new xChanger(in);
        }

        @Override
        public xChanger[] newArray(int size) {
            return new xChanger[size];
        }
    };

    /**
     * Writes the {@link xChanger} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // Write User's Parcelable data
        dest.writeFloat(averageRating);
        dest.writeInt(totalRatings);
        // Do not write lists to avoid cyclic dependencies
        dest.writeInt(succeedDeals);
        dest.writeInt(failedDeals);
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
     * Gets the average rating of the xChanger.
     *
     * @return The average rating as a float.
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Gets the total number of ratings received by the xChanger.
     *
     * @return The total ratings count.
     */
    public int getTotalRatings() {
        return totalRatings;
    }

    /**
     * Gets the list of ratings for the xChanger.
     *
     * @return An {@link ArrayList} of {@link Rating} objects.
     */
    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    /**
     * Gets the list of reports made by the xChanger.
     *
     * @return An {@link ArrayList} of reports as {@link String}.
     */
    public ArrayList<String> getReports() {
        return reports;
    }

    /**
     * Gets the list of items owned by the xChanger.
     *
     * @return An {@link ArrayList} of {@link Item} objects.
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Sets the list of items owned by the xChanger.
     *
     * @param items An {@link ArrayList} of {@link Item} objects to set.
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * Gets the list of requests made by the xChanger.
     *
     * @return An {@link ArrayList} of {@link Request} objects.
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * Gets the list of counteroffers made by the xChanger.
     *
     * @return An {@link ArrayList} of {@link Counteroffer} objects.
     */
    public ArrayList<Counteroffer> getCounterOffers() {
        return counterOffers;
    }

    /**
     * Gets the list of finalized xChanges for the xChanger.
     *
     * @return An {@link ArrayList} of {@link xChange} objects.
     */
    public ArrayList<xChange> getFinalized() {
        return finalized;
    }

    /**
     * Gets the number of successful deals completed by the xChanger.
     *
     * @return The count of successful deals.
     */
    public int getSucceedDeals() {
        return succeedDeals;
    }

    /**
     * Gets the number of failed deals involving the xChanger.
     *
     * @return The count of failed deals.
     */
    public int getFailedDeals() {
        return failedDeals;
    }

    /**
     * Adds a {@link Rating} to the xChanger and updates the average rating.
     *
     * @param rating The {@link Rating} to add.
     */
    public void addRating(Rating rating) {
        if (rating != null) {
            this.ratings.add(rating);
            this.totalRatings++;
            this.averageRating = (this.averageRating * (this.totalRatings - 1) + rating.getRating()) / this.totalRatings;
        }
    }

    /**
     * Reports another xChanger user and associates the report with a finalized exchange.
     *
     * @param xchanger  The {@link xChanger} being reported.
     * @param message   The report message.
     * @param finalized The {@link xChange} associated with the report.
     */
    public void report(xChanger xchanger, String message, xChange finalized) {
        if (finalized.getDealStatus() != null) {
            message = "User " + this.getUsername() + " reported user " + xchanger.getUsername();
        }
        this.reports.add(message);
    }

    /**
     * Increments the count of successful deals for the xChanger.
     */
    public void plusOneSucceedDeal() {
        this.succeedDeals++;
    }

    /**
     * Increments the count of failed deals for the xChanger.
     */
    public void plusOneFailedDeal() {
        this.failedDeals++;
    }

    /**
     * Deletes an {@link Item} from the xChanger's inventory.
     *
     * @param item The {@link Item} to delete.
     */
    public void deleteItem(Item item) {
        this.items.removeIf(i -> i.equals(item));
    }

    /**
     * Retrieves an {@link Item} from the xChanger's inventory.
     *
     * @param item The {@link Item} to retrieve.
     * @return The {@link Item} if found, otherwise {@code null}.
     */
    public Item getItem(Item item) {
        return this.items.stream().filter(i -> i.equals(item)).findFirst().orElse(null);
    }

    /**
     * Uploads a new {@link Item} to the xChanger's inventory and saves it to the database.
     *
     * @param itemName        The name of the item.
     * @param itemDescription The description of the item.
     * @param itemCategory    The {@link Category} of the item.
     * @param itemCondition   The condition of the item.
     * @param itemImages      A list of {@link Image} objects associated with the item.
     */

    public void UploadItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        Item item = new Item(this.getUsername(), itemName, itemDescription, itemCategory, itemCondition, itemImages);
        this.items.add(item);
        new Thread(() -> {
            try {
                AppDatabase.getItemDao().insertItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Sends a {@link Request} for an item to another {@link xChanger}.
     *
     * @param targetUser    The target {@link xChanger} to send the request to.
     * @param offeredItem   The {@link Item} offered in exchange.
     * @param requestedItem The {@link Item} being requested.
     */
    public void RequestItem(xChanger targetUser, Item offeredItem, Item requestedItem) {
        Request request = new Request(this, targetUser, offeredItem, requestedItem, null);
        this.requests.add(request);
    }

    /**
     * Creates a {@link Counteroffer} for a given {@link Request}.
     *
     * @param item    The {@link Item} being offered in the counteroffer.
     * @param request The {@link Request} to counter.
     * @throws IllegalArgumentException If the item or request is {@code null}.
     */
    public void counterOffer(Item item, Request request) {
        if (item == null || request == null) {
            throw new IllegalArgumentException("Item, message, or request cannot be null.");
        }
        // Pass usernames instead of full xChanger objects
        Counteroffer counterOffer = new Counteroffer(request, item, request.getRequestee(), request.getRequester());
        new Thread(() -> {
            try {
                long id = AppDatabase.getCounterofferDao().insertCounteroffer(counterOffer);
                Log.d("TEST", String.valueOf(id));
            } catch (Exception e) {
                Log.e("xChanger", "Error saving counteroffer", e);
            }
        }).start();
        this.counterOffers.add(counterOffer);
    }

    /**
     * Accepts a {@link Request} and finalizes it with the specified rating.
     *
     * @param request The {@link Request} to accept.
     * @param rating  The rating value to assign.
     */
    public void acceptRequest(Request request, float rating){
        xChange xChange = new xChange(request, null);
        xChange.acceptOffer(rating);
    }

    /**
     * Rejects a {@link Request} and finalizes it with the specified rating.
     *
     * @param request The {@link Request} to reject.
     * @param rating  The rating value to assign.
     */
    public void rejectRequest(Request request, float rating){
        xChange xChange = new xChange(request, null);
        xChange.rejectOffer(rating);
    }

    /**
     * Accepts a {@link Counteroffer} and finalizes it with the specified rating.
     *
     * @param counteroffer The {@link Counteroffer} to accept.
     * @param rating       The rating value to assign.
     */
    public void acceptCounteroffer(Counteroffer counteroffer, float rating){
        xChange xChange = new xChange(counteroffer.getRequest(), counteroffer, null);
        xChange.acceptOffer(rating);
    }

    /**
     * Rejects a {@link Counteroffer} and finalizes it with the specified rating.
     *
     * @param counteroffer The {@link Counteroffer} to reject.
     * @param rating       The rating value to assign.
     */
    public void rejectCounteroffer(Counteroffer counteroffer, float rating){
        xChange xChange = new xChange(counteroffer.getRequest(), counteroffer, null);
        xChange.rejectOffer(rating);
    }

    /**
     * Returns a string representation of the xChanger.
     *
     * @return A string containing the average rating, total ratings, successful deals, and failed deals.
     */
    @Override
    public String toString() {
        return "xChanger{" +
                "averageRating=" + averageRating +
                ", totalRatings=" + totalRatings +
                ", succeedDeals=" + succeedDeals +
                ", failedDeals=" + failedDeals +
                '}';
    }
}
