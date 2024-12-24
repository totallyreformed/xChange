package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ImageConverter {

    @TypeConverter
    public static String fromImageList(ArrayList<Image> images) {
        if (images == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(images);
    }

    @TypeConverter
    public static ArrayList<Image> toImageList(String imagesString) {
        if (imagesString == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Image>>() {}.getType();
        return gson.fromJson(imagesString, type);
    }
}
