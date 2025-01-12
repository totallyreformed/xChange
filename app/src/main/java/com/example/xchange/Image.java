package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents an image with an optional file path and description.
 * <p>
 * This class implements {@link Parcelable} for data transfer and persistence.
 * </p>
 */
public class Image implements Parcelable {
    private String filePath; // Path to the image file (optional)
    private String description; // Optional metadata for the image

    /**
     * Constructs a new {@link Image}.
     *
     * @param filePath    The file path to the image.
     * @param description An optional description or metadata for the image.
     */
    // Constructor
    public Image(String filePath, String description) {
        this.filePath = filePath;
        this.description = description;
    }

    /**
     * Constructs an {@link Image} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link Image}.
     */
    protected Image(Parcel in) {
        filePath = in.readString();
        description = in.readString();
    }

    /**
     * Writes the {@link Image} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Flags for writing the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeString(description);
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
     * A {@link Parcelable.Creator} implementation for creating {@link Image} objects from a {@link Parcel}.
     */
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    /**
     * Gets the file path of the image.
     *
     * @return The file path of the image.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets the description or metadata of the image.
     *
     * @return The description of the image.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the file path of the image.
     *
     * @param filePath The file path to set.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Sets the description or metadata of the image.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a string representation of the {@link Image}.
     *
     * @return A string representation of the image, including its file path and description.
     */
    @Override
    public String toString() {
        return "Image{" +
                "filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
