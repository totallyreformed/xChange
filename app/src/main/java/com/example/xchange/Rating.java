package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Represents a rating in the xChange application.
 * <p>
 * A rating is provided by one user (rater) to another user (ratee) for a specific {@link Request} or {@link xChange}.
 * The rating is represented as a floating-point value between 0 and 5.
 * This class implements {@link Parcelable} for data transfer.
 * </p>
 */
@Entity(tableName = "ratings")
public class Rating implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rating_id")
    private Long ratingId;

    @ColumnInfo(name = "rating")
    private float rating;
    @ColumnInfo(name = "rater")
    private String raterUsername;
    @ColumnInfo(name = "ratee")
    private String rateeUsername;
    @Ignore
    private transient xChanger rater; // Excluded from parceling
    @Ignore
    private transient xChanger ratee; // Excluded from parceling
    @Ignore
    private Request request;
    @Ignore
    private xChange xChange;

    /**
     * Constructs a new {@link Rating}.
     *
     * @param rating  The rating value (must be between 0 and 5).
     * @param rater   The {@link xChanger} who gives the rating.
     * @param ratee   The {@link xChanger} who receives the rating.
     * @param request The {@link Request} associated with the rating.
     * @param xChange The {@link xChange} associated with the rating.
     */
    public Rating(float rating, xChanger rater, xChanger ratee, Request request, xChange xChange) {
        this.rating = rating;
        this.raterUsername = rater.getUsername();
        this.rateeUsername = ratee.getUsername();
        this.rater = rater;
        this.ratee = ratee;
        this.request = request;
        this.xChange = xChange;
    }

    // Default Room Constructor
    public Rating() {
    }

    /**
     * Constructs a {@link Rating} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link Rating}.
     */
    protected Rating(Parcel in) {
        rating = in.readFloat();
        ratingId = in.readLong();
        request = in.readParcelable(Request.class.getClassLoader());
        xChange = in.readParcelable(xChange.class.getClassLoader());
        raterUsername = in.readString();
        rateeUsername = in.readString();
        // Excluded: rater and ratee
        this.rater = null;
        this.ratee = null;
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link Rating} objects from a {@link Parcel}.
     */
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

    /**
     * Writes the {@link Rating} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(rating);
        dest.writeLong(ratingId);
        dest.writeParcelable(request, flags);
        dest.writeParcelable(xChange, flags);
        dest.writeString(raterUsername);
        dest.writeString(rateeUsername);
        // Excluded: rater and ratee
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
     * Gets the rating value.
     *
     * @return The rating value.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Gets the rating ID.
     *
     * @return The rating ID.
     */
    public Long getRatingId() {
        return ratingId;
    }

    /**
     * Sets the rating ID.
     *
     * @param ratingId The rating ID.
     */
    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    /**
     * Sets the rating value.
     *
     * @param rating The rating value (must be between 0 and 5).
     * @throws IllegalArgumentException if the rating is not between 0 and 5.
     */
    public void setRating(float rating) {
        if (rating >= 0 && rating <= 5) { // Assuming ratings are between 0 and 5
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be between 0 and 5.");
        }
    }

    /**
     * Gets the {@link xChanger} who gives the rating.
     *
     * @return The rater.
     */
    public xChanger getRater() {
        return rater;
    }

    /**
     * Gets the {@link xChanger} who receives the rating.
     *
     * @return The ratee.
     */
    public xChanger getRatee() {
        return ratee;
    }

    /**
     * Gets the username of the {@link xChanger} who gives the rating.
     *
     * @return The username of the rater.
     */
    public String getRaterUsername() {
        return raterUsername;
    }

    /**
     * Sets the username of the {@link xChanger} who gives the rating.
     *
     * @param raterUsername The username of the rater.
     */
    public void setRaterUsername(String raterUsername) {
        this.raterUsername = raterUsername;
    }


    /**
     * Gets the username of the {@link xChanger} who receives the rating.
     *
     * @return The username of the ratee.
     */
    public String getRateeUsername() {
        return rateeUsername;
    }

    /**
     * Sets the username of the {@link xChanger} who receives the rating.
     *
     * @param rateeUsername The username of the ratee.
     */
    public void setRateeUsername(String rateeUsername) {
        this.rateeUsername = rateeUsername;
    }

    /**
     * Gets the {@link Request} associated with the rating.
     *
     * @return The associated {@link Request}.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the {@link Request} associated with the rating.
     *
     * @param request The {@link Request} to set.
     */
    public void setRequest(Request request) {
        this.request = request;
    }


    /**
     * Gets the {@link xChange} associated with the rating.
     *
     * @return The associated {@link xChange}.
     */
    public xChange getXChange() {
        return xChange;
    }

    /**
     * Sets the {@link xChange} associated with the rating.
     *
     * @param xChange The {@link xChange} to set.
     */
    public void setXChange(xChange xChange) {
        this.xChange = xChange;
    }

    /**
     * Returns a string representation of the {@link Rating}.
     *
     * @return A string representation of the rating, including the rating value, rater, ratee, request, and xChange.
     */
    @Override
    public String toString() {
        return "Rating{" +
                "rating=" + rating +
                ", rater=" + (rater != null ? rater.getUsername() : "null") +
                ", ratee=" + (ratee != null ? ratee.getUsername() : "null") +
                ", request=" + (request != null ? request.getRequestId() : "null") +
                ", xChange=" + (xChange != null ? xChange.getFinalizedId() : "null") +
                '}';
    }
}
