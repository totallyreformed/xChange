package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Rating implements Parcelable {
    private float rating;
    private xChanger rater;
    private xChanger ratee;
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
        rater = in.readParcelable(xChanger.class.getClassLoader());
        ratee = in.readParcelable(xChanger.class.getClassLoader());
        request = in.readParcelable(Request.class.getClassLoader());
        xChange = in.readParcelable(xChange.class.getClassLoader());
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
        dest.writeParcelable(rater, flags);
        dest.writeParcelable(ratee, flags);
        dest.writeParcelable(request, flags);
        dest.writeParcelable(xChange, flags);
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
        String raterName = (rater != null) ? rater.getUsername() : "null";
        String rateeName = (ratee != null) ? ratee.getUsername() : "null";
        String requestId = (request != null) ? request.getRequestId().toString() : "null";
        String xChangeId = (xChange != null) ? xChange.getFinalizedId().toString() : "null";
        return "Rating{" +
                "rating=" + rating +
                ", rater='" + raterName + '\'' +
                ", ratee='" + rateeName + '\'' +
                ", requestId=" + requestId +
                ", xChangeId=" + xChangeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rating rating1 = (Rating) o;
        return Float.compare(rating1.rating, rating) == 0 &&
                Objects.equals(rater, rating1.rater) &&
                Objects.equals(ratee, rating1.ratee) &&
                Objects.equals(request, rating1.request) &&
                Objects.equals(xChange, rating1.xChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rating, rater, ratee, request, xChange);
    }
}
