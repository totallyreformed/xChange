package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Counteroffer;
import com.google.gson.Gson;

/**
 * Utility class for converting {@link Counteroffer} objects to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link Counteroffer} objects.
 */
public class CounterofferConverter {

    /**
     * Converts a JSON String representation of a {@link Counteroffer} to a {@link Counteroffer} object.
     *
     * @param value the JSON String representation of the {@link Counteroffer}. May be null.
     * @return the corresponding {@link Counteroffer} object, or null if the input is null.
     */
    @TypeConverter
    public static Counteroffer fromString(String value) {
        return new Gson().fromJson(value, Counteroffer.class);
    }

    /**
     * Converts a {@link Counteroffer} object to its JSON String representation.
     *
     * @param counteroffer the {@link Counteroffer} object to convert. May be null.
     * @return the JSON String representation of the {@link Counteroffer}, or null if the input is null.
     */
    @TypeConverter
    public static String counterofferToString(Counteroffer counteroffer) {
        return new Gson().toJson(counteroffer);
    }
}