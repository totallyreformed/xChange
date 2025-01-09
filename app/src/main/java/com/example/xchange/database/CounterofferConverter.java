package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Counteroffer;
import com.google.gson.Gson;

public class CounterofferConverter {
    @TypeConverter
    public static Counteroffer fromString(String value) {
        return new Gson().fromJson(value, Counteroffer.class);
    }

    @TypeConverter
    public static String counterofferToString(Counteroffer counteroffer) {
        return new Gson().toJson(counteroffer);
    }
}