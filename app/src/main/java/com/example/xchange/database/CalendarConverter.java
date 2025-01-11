package com.example.xchange.database;

import androidx.room.TypeConverter;
import com.example.xchange.SimpleCalendar;

/**
 * Converter class for transforming {@link SimpleCalendar} objects to and from String representations.
 * Used by Room for persisting {@link SimpleCalendar} fields in the database.
 */
public class CalendarConverter {

    /**
     * Converts a {@link SimpleCalendar} object to its String representation.
     *
     * @param calendar The {@link SimpleCalendar} object to be converted.
     * @return A String representation of the calendar, or null if the calendar is null.
     */
    @TypeConverter
    public static String fromSimpleCalendar(SimpleCalendar calendar) {
        return calendar != null ? calendar.toString() : null;
    }

    /**
     * Converts a {@link SimpleCalendar} object to its String representation.
     *
     * @param calendarString The {@link SimpleCalendar} object to be converted.
     * @return A String representation of the calendar, or null if the calendar is null.
     */
    @TypeConverter
    public static SimpleCalendar toSimpleCalendar(String calendarString) {
        if (calendarString == null) return null;
        try {
            String[] parts = calendarString.split("-");
            if (parts.length != 3) {
                return new SimpleCalendar(2024, 1, 1);
            }
            return new SimpleCalendar(
                    Integer.parseInt(parts[0]), // year
                    Integer.parseInt(parts[1]), // month
                    Integer.parseInt(parts[2])  // day
            );
        } catch (Exception e) {
            return new SimpleCalendar(2024, 1, 1); // Default date
        }
    }

}

