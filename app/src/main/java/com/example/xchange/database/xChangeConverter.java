package com.example.xchange.database;

import androidx.room.TypeConverter;
import com.example.xchange.xChange;
import com.google.gson.Gson;


/**
 * Utility class for converting {@link xChange} objects to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link xChange} objects.
 */
public class xChangeConverter {
    /**
     * Converts a JSON String representation of an {@link xChange} object into an {@link xChange} instance.
     *
     * @param value the JSON String representation of the {@link xChange}. May be null.
     * @return the corresponding {@link xChange} object, or null if the input string is null.
     */
    @TypeConverter
    public static xChange fromString(String value) {
        return new Gson().fromJson(value, xChange.class);
    }
    /**
     * Converts an {@link xChange} object into its JSON String representation.
     *
     * @param xchange the {@link xChange} object to convert. May be null.
     * @return the JSON String representation of the {@link xChange}, or null if the input is null.
     */
    @TypeConverter
    public static String xChangeToString(xChange xchange) {
        return new Gson().toJson(xchange);
    }
}