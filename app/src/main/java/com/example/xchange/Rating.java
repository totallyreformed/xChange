package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

public class Rating implements Parcelable {
    private float rating;
    private transient xChanger rater; // Excluded from parceling
    private transient xChanger ratee; // Excluded from parceling
    private Request request;
    private xChange xChange;

    // Constructor
    public Rating(float rating, xChanger rater, xChanger ratee, Request request, xChange xChange) {
        this.rating = rating;
        this.rater = rater;
        this.ratee = ratee;
        this.request = request;
        this.xChange = xChange;
    }

    // Parcelable Constructor
    protected Rating(Parcel in) {
        rating = in.readFloat();
        request = in.readParcelable(Request.class.getClassLoader());
        xChange = in.readParcelable(xChange.class.getClassLoader());
        // Excluded: rater and ratee
        this.rater = null;
        this.ratee = null;
    }

    public static final Creator<Rating> CREATOR = new Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
        dest.writeParcelable(request, flags);
        dest.writeParcelable(xChange, flags);
        // Excluded: rater and ratee
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        if (rating >= 0 && rating <= 5) { // Assuming ratings are between 0 and 5
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
    }

    public xChanger getRater() {
        return rater;
    }

    public xChanger getRatee() {
        return ratee;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public xChange getXChange() {
        return xChange;
    }

    public void setXChange(xChange xChange) {
        this.xChange = xChange;
    }

    // toString method for better debugging
    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", rater=" + (rater != null ? rater.getUsername() : "null") +
                ", ratee=" + (ratee != null ? ratee.getUsername() : "null") +
                ", request=" + (request != null ? request.getRequestId() : "null") +
                ", xChange=" + (xChange != null ? xChange.getFinalizedID() : "null") +
                '}';
    }
}
