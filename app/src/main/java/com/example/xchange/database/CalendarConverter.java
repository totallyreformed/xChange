package com.example.xchange.database;

import androidx.room.TypeConverter;
import com.example.xchange.SimpleCalendar;

public class CalendarConverter {

    @TypeConverter
    public static String fromSimpleCalendar(SimpleCalendar calendar) {
        // Convert SimpleCalendar to a String
        return calendar != null ? calendar.toString() : null;
    }

    @TypeConverter
    public static SimpleCalendar toSimpleCalendar(String calendarString) {
        // Convert String back to SimpleCalendar
        if (calendarString == null) return null;
        String[] parts = calendarString.split("-");
        return new SimpleCalendar(
                Integer.parseInt(parts[0]), // year
                Integer.parseInt(parts[1]), // month
                Integer.parseInt(parts[2])  // day
        );
    }
}
