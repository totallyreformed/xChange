package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ItemConverter {

    private static final Gson gson = new Gson();

    // Convert an Item to a JSON String
    @TypeConverter
    public static String fromItem(Item item) {
        if (item == null) {
            return null;
        }
        return gson.toJson(item);
    }

    // Convert a JSON String back to an Item
    @TypeConverter
    public static Item toItem(String itemString) {
        if (itemString == null) {
            return null;
        }
        return gson.fromJson(itemString, Item.class);
    }

    // Convert a List of Items to a JSON String
    @TypeConverter
    public static String fromItemList(ArrayList<Item> items) {
        if (items == null) {
            return null;
        }
        return gson.toJson(items);
    }

    // Convert a JSON String back to a List of Items
    @TypeConverter
    public static ArrayList<Item> toItemList(String itemListString) {
        if (itemListString == null) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
        return gson.fromJson(itemListString, listType);
    }
}
