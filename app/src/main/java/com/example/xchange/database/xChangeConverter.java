// xChangeConverter.java
package com.example.xchange.database;

import androidx.room.TypeConverter;
import com.example.xchange.xChange;
import com.google.gson.Gson;

public class xChangeConverter {
    @TypeConverter
    public static xChange fromString(String value) {
        return new Gson().fromJson(value, xChange.class);
    }

    @TypeConverter
    public static String xChangeToString(xChange xchange) {
        return new Gson().toJson(xchange);
    }
}
