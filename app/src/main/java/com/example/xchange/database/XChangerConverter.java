package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.xChanger;
import com.google.gson.Gson;

public class XChangerConverter {

    private static final Gson gson = new Gson();

    // Convert xChanger to JSON String
    @TypeConverter
    public static String fromXChanger(xChanger xchanger) {
        if (xchanger == null) {
            return null;
        }
        return gson.toJson(xchanger);
    }

    // Convert JSON String back to xChanger
    @TypeConverter
    public static xChanger toXChanger(String xchangerString) {
        if (xchangerString == null) {
            return null;
        }
        return gson.fromJson(xchangerString, xChanger.class);
    }
}
