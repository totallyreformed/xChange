// File: com/example/xchange/SimpleCalendar.java

package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class SimpleCalendar implements Parcelable {
    private int year;
    private int month;
    private int day;

    // Constructor
    public SimpleCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    // Parcelable Implementation
    protected SimpleCalendar(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SimpleCalendar> CREATOR = new Creator<SimpleCalendar>() {
        @Override
        public SimpleCalendar createFromParcel(Parcel in) {
            return new SimpleCalendar(in);
        }

        @Override
        public SimpleCalendar[] newArray(int size) {
            return new SimpleCalendar[size];
        }
    };

    // Optional: Override toString() for better readability
    @Override
    public String toString() {
        return year + "-" + (month + 1) + "-" + day;
    }

    // Static method to convert from String to SimpleCalendar (if needed)
    public static SimpleCalendar fromString(String dateString) {
        // Implement parsing logic based on the toString() format
        // Example implementation:
        try {
            String[] dateTime = dateString.split(" ");
            String[] dateParts = dateTime[0].split("-");

            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Months are 0-based in Calendar
            int day = Integer.parseInt(dateParts[2]);

            return new SimpleCalendar(year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
