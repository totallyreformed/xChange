package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Minimal test implementation of SimpleCalendar.
 */
public class SimpleCalendar implements Parcelable {
    private final String date;

    public SimpleCalendar(String date) {
        this.date = date;
    }

    protected SimpleCalendar(Parcel in) {
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
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

    @Override
    public String toString() {
        return date;
    }
}
