package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.xchange.database.AppDatabase;

import java.util.ArrayList;

public class xChanger extends User implements Parcelable {
    private float averageRating;
    private int totalRatings;
    private ArrayList<Rating> ratings;
    private ArrayList<String> reports;
    private ArrayList<Item> items;
    private ArrayList<Request> requests;
    private ArrayList<Counteroffer> counterOffers;
    private ArrayList<xChange> finalized;
    private int succeedDeals;
    private int failedDeals;

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

    // Parcelable Implementation
    protected xChanger(Parcel in) {
        super(in); // Call the User's Parcelable constructor
        averageRating = in.readFloat();
        totalRatings = in.readInt();
        ratings = in.createTypedArrayList(Rating.CREATOR);
        reports = in.createStringArrayList();
        items = in.createTypedArrayList(Item.CREATOR);
        requests = in.createTypedArrayList(Request.CREATOR);
        counterOffers = in.createTypedArrayList(Counteroffer.CREATOR);
        finalized = in.createTypedArrayList(xChange.CREATOR);
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
        dest.writeTypedList(ratings);
        dest.writeStringList(reports);
        dest.writeTypedList(items);
        dest.writeTypedList(requests);
        dest.writeTypedList(counterOffers);
        dest.writeTypedList(finalized);
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

    public void counterOffer(Item item, String message, Request request) {
        if (item == null || message == null || request == null) {
            throw new IllegalArgumentException("Item, message, or request cannot be null.");
        }
        Counteroffer counterOffer = new Counteroffer(request, message, item);
        this.counterOffers.add(counterOffer);
    }

    public void acceptRequest(Request request,float rating){
        xChange xChange=new xChange(request,null);
        xChange.acceptOffer(rating);
    }
    public void rejectRequest(Request request,float rating){
        xChange xChange=new xChange(request,null);
        xChange.rejectOffer(rating);
    }
    public void acceptCounteroffer(Counteroffer counteroffer,float rating){
        xChange xChange=new xChange(counteroffer.getRequest(),counteroffer,null);
        xChange.acceptOffer(rating);
    }
    public void rejectCounteroffer(Counteroffer counteroffer,float rating){
        xChange xChange=new xChange(counteroffer.getRequest(),counteroffer,null);
        xChange.rejectOffer(rating);
    }
}
