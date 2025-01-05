package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class RequestConverter {

    @TypeConverter
    public static String fromRequest(Request request) {
        if (request == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<Request>() {}.getType();
        return gson.toJson(request, type);
    }

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
