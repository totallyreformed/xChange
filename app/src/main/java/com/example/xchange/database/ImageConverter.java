package com.example.xchange.database;

import androidx.room.TypeConverter;

import com.example.xchange.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Utility class for converting a list of {@link Image} objects to and from their JSON String representation.
 * This class is used by Room database to handle the persistence of {@link Image} lists.
 */
public class ImageConverter {

    /**
     * Converts a list of {@link Image} objects to its JSON String representation.
     *
     * @param images the list of {@link Image} objects to convert. May be null.
     * @return the JSON String representation of the list of images, or null if the input list is null.
     */
    @TypeConverter
    public static String fromImageList(ArrayList<Image> images) {
        if (images == null) {
            return null;
        }
        Gson gson = new Gson();
        return gson.toJson(images);
    }

    /**
     * Converts a JSON String representation of a list of {@link Image} objects to an {@link ArrayList} of {@link Image}.
     *
     * @param imagesString the JSON String representation of the list of images. May be null.
     * @return the corresponding {@link ArrayList} of {@link Image} objects, or null if the input string is null.
     */
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
