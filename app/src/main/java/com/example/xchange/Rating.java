package com.example.xchange;

public class Rating {
    private float rating;
    private final xChanger rater;
    private final xChanger ratee;
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

    // Getter for rating
    public float getRating() {
        return rating;
    }

    // Setter for rating
    public void setRating(float rating) {
        if (rating >= 0 && rating <= 5) { // Assuming ratings are between 0 and 5
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
    }

    // Getter for rater
    public xChanger getRater() {
        return rater;
    }

    // Getter for ratee
    public xChanger getRatee() {
        return ratee;
    }

    // Getter for request
    public Request getRequest() {
        return request;
    }

    // Setter for request
    public void setRequest(Request request) {
        this.request = request;
    }

    // Getter for xChange
    public xChange getXChange() {
        return xChange;
    }

    // Setter for xChange
    public void setXChange(xChange xChange) {
        this.xChange = xChange;
    }

    // Method to display the rating details
    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", rater=" + rater +
                ", ratee=" + ratee +
                ", request=" + request +
                ", xChange=" + xChange +
                '}';
    }
}
