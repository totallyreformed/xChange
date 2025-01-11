package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Item;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Utility class for converting {@link Item} objects and lists of {@link Item} objects
 * to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link Item} objects and lists.
 */
public class ItemConverter {

    private static final Gson gson = new Gson();

    /**
     * Converts an {@link Item} object to its JSON String representation.
     *
     * @param item the {@link Item} object to convert. May be null.
     * @return the JSON String representation of the {@link Item}, or null if the input is null.
     */
    @TypeConverter
    public static String fromItem(Item item) {
        if (item == null) {
            return null;
        }
        return gson.toJson(item);
    }

    /**
     * Converts a JSON String representation of an {@link Item} to an {@link Item} object.
     *
     * @param itemString the JSON String representation of the {@link Item}. May be null.
     * @return the corresponding {@link Item} object, or null if the input string is null.
     */
    @TypeConverter
    public static Item toItem(String itemString) {
        if (itemString == null) {
            return null;
        }
        return gson.fromJson(itemString, Item.class);
    }

    /**
     * Converts a list of {@link Item} objects to its JSON String representation.
     *
     * @param items the list of {@link Item} objects to convert. May be null.
     * @return the JSON String representation of the list of items, or null if the input list is null.
     */
    @TypeConverter
    public static String fromItemList(ArrayList<Item> items) {
        if (items == null) {
            return null;
        }
        return gson.toJson(items);
    }

    /**
     * Converts a JSON String representation of a list of {@link Item} objects
     * to an {@link ArrayList} of {@link Item}.
     *
     * @param itemListString the JSON String representation of the list of items. May be null.
     * @return the corresponding {@link ArrayList} of {@link Item} objects, or null if the input string is null.
     */
    @TypeConverter
    public static ArrayList<Item> toItemList(String itemListString) {
        if (itemListString == null) {
            return null;
        }
        Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
        return gson.fromJson(itemListString, listType);
    }
}
