package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    private String filePath; // Path to the image file (optional)
    private String description; // Optional metadata for the image

    // Constructor
    public Image(String filePath, String description) {
        this.filePath = filePath;
        this.description = description;
    }

    // Parcelable constructor
    public Image(Parcel in) {
        filePath = in.readString();
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    // Getters
    public String getFilePath() {
        return filePath;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Image{" +
                "filePath='" + filePath + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
