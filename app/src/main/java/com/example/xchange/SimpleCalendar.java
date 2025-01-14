package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;

/**
 * A simple representation of a calendar date, including year, month, and day.
 * <p>
 * This class provides basic functionality for representing and manipulating dates,
 * and it implements {@link Parcelable} for data transfer.
 * </p>
 */
public class SimpleCalendar implements Parcelable {
    private int year;
    private int month;
    private int day;

    /**
     * Constructs a new {@link SimpleCalendar} instance.
     *
     * @param year  The year of the date.
     * @param month The month of the date (0-based, January = 0).
     * @param day   The day of the month.
     */
    public SimpleCalendar(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Gets the year of the date.
     *
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the month of the date.
     *
     * @return The month (0-based, January = 0).
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the day of the month.
     *
     * @return The day.
     */
    public int getDay() {
        return day;
    }

    /**
     * Static helper method to create a {@link SimpleCalendar} instance representing today's date.
     *
     * @return A {@link SimpleCalendar} instance for the current date.
     */
    public static SimpleCalendar today() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // January is 0!
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new SimpleCalendar(year, month, day);
    }

    /**
     * Constructs a {@link SimpleCalendar} instance from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link SimpleCalendar}.
     */
    protected SimpleCalendar(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
    }

    /**
     * Writes the {@link SimpleCalendar} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
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
     * A {@link Parcelable.Creator} implementation for creating {@link SimpleCalendar} objects from a {@link Parcel}.
     */
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

    /**
     * Converts the {@link SimpleCalendar} object to a string representation.
     *
     * @return A string in the format "YYYY-MM-DD".
     */
    @Override
    public String toString() {
        if (month == 0) {
            return year + "-" + (month + 1) + "-" + day;
        }
        return year + "-" + (month) + "-" + day;
    }

    /**
     * Creates a {@link SimpleCalendar} instance from a string representation.
     * <p>
     * The string must be in the format "YYYY-MM-DD".
     * </p>
     *
     * @param dateString The string representation of the date.
     * @return A {@link SimpleCalendar} instance, or {@code null} if parsing fails.
     */
    public static SimpleCalendar fromString(String dateString) {
        // Implement parsing logic based on the toString() format
        // Example implementation:
        try {
            String[] dateParts = dateString.split("-");
            int year = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]); // Months are 0-based in Calendar
            int day = Integer.parseInt(dateParts[2]);

            if (month == 0) {
                return new SimpleCalendar(year, month + 1, day);
            }
            return new SimpleCalendar(year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
