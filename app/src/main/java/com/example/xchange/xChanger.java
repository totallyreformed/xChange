package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.xchange.database.AppDatabase;

import java.util.ArrayList;

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

    // Constructor
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

    // Parcelable Constructor
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags); // Write User's Parcelable data
        dest.writeFloat(averageRating);
        dest.writeInt(totalRatings);
        // Do not write lists to avoid cyclic dependencies
        dest.writeInt(succeedDeals);
        dest.writeInt(failedDeals);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getter and Setter Methods
    public float getAverageRating() {
        return averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public ArrayList<String> getReports() {
        return reports;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public ArrayList<Counteroffer> getCounterOffers() {
        return counterOffers;
    }

    public ArrayList<xChange> getFinalized() {
        return finalized;
    }

    public int getSucceedDeals() {
        return succeedDeals;
    }

    public int getFailedDeals() {
        return failedDeals;
    }

    // Rating Methods
    public void addRating(Rating rating) {
        if (rating != null) {
            this.ratings.add(rating);
            this.totalRatings++;
            this.averageRating = (this.averageRating * (this.totalRatings - 1) + rating.getRating()) / this.totalRatings;
        }
    }

    public void report(xChanger xchanger, String message, xChange finalized) {
        if (finalized.getStatus() != null) {
            message = "User " + this.getUsername() + " reported user " + xchanger.getUsername();
        }
        this.reports.add(message);
    }

    // Deal Statistics
    public void plusOneSucceedDeal() {
        this.succeedDeals++;
    }

    public void plusOneFailedDeal() {
        this.failedDeals++;
    }

    public void deleteItem(Item item) {
        this.items.removeIf(i -> i.equals(item));
    }

    public Item getItem(Item item) {
        return this.items.stream().filter(i -> i.equals(item)).findFirst().orElse(null);
    }

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

    public void RequestItem(xChanger targetUser, Item offeredItem, Item requestedItem) {
        Request request = new Request(this, targetUser, offeredItem, requestedItem, null);
        this.requests.add(request);
    }

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

    public void acceptRequest(Request request, float rating){
        xChange xChange = new xChange(request, null, null);
        xChange.acceptOffer(rating);
    }

    public void rejectRequest(Request request, float rating){
        xChange xChange = new xChange(request, null, null);
        xChange.rejectOffer(rating);
    }

    public void acceptCounteroffer(Counteroffer counteroffer, float rating){
        xChange xChange = new xChange(counteroffer.getRequest(), counteroffer, null);
        xChange.acceptOffer(rating);
    }

    public void rejectCounteroffer(Counteroffer counteroffer, float rating){
        xChange xChange = new xChange(counteroffer.getRequest(), counteroffer, null);
        xChange.rejectOffer(rating);
    }

    // toString method for better debugging
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
