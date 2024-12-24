package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

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

    protected SimpleCalendar(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
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

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
    }
}
