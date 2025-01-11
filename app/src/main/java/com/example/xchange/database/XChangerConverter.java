package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.xChanger;
import com.google.gson.Gson;

/**
 * Utility class for converting {@link xChanger} objects to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link xChanger} objects.
 */
public class XChangerConverter {

    private static final Gson gson = new Gson();

    /**
     * Converts an {@link xChanger} object into its JSON String representation.
     *
     * @param xchanger the {@link xChanger} object to convert. May be null.
     * @return the JSON String representation of the {@link xChanger}, or null if the input is null.
     */
    @TypeConverter
    public static String fromXChanger(xChanger xchanger) {
        if (xchanger == null) {
            return null;
        }
        return gson.toJson(xchanger);
    }

    /**
     * Converts a JSON String representation of an {@link xChanger} object into an {@link xChanger} instance.
     *
     * @param xchangerString the JSON String representation of the {@link xChanger}. May be null.
     * @return the corresponding {@link xChanger} object, or null if the input string is null.
     */
    @TypeConverter
    public static xChanger toXChanger(String xchangerString) {
        if (xchangerString == null) {
            return null;
        }
        return gson.fromJson(xchangerString, xChanger.class);
    }
}
