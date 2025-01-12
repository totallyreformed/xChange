package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Utility class for converting {@link Request} objects to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link Request} objects.
 */
public class RequestConverter {

    /**
     * Converts a {@link Request} object to its JSON String representation.
     *
     * @param request the {@link Request} object to convert. May be null.
     * @return the JSON String representation of the {@link Request}, or null if the input is null.
     */
    @TypeConverter
    public static String fromRequest(Request request) {
        if (request == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Request>() {}.getType();
        return gson.toJson(request, type);
    }

    /**
     * Converts a JSON String representation of a {@link Request} object to a {@link Request} object.
     *
     * @param requestString the JSON String representation of the {@link Request}. May be null.
     * @return the corresponding {@link Request} object, or null if the input string is null.
     */
    @TypeConverter
    public static Request toRequest(String requestString) {
        if (requestString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Request>() {}.getType();
        return gson.fromJson(requestString, type);
    }
}
