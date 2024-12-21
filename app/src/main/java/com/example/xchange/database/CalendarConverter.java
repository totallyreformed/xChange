package com.example.xchange.database;

import androidx.room.TypeConverter;
import com.example.xchange.SimpleCalendar;

public class CalendarConverter {
    @TypeConverter
    public static String fromSimpleCalendar(SimpleCalendar calendar) {
        return calendar != null ? calendar.toString() : null;
    }

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

