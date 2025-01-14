// File: CategoryConverter.java
package com.example.xchange.database;

import android.util.Log;

import androidx.room.TypeConverter;
import com.example.xchange.Category;

// File: CategoryConverter.java

/**
 * Utility class for converting {@link Category} objects to and from their String representation.
 * This class is used by Room database to handle the persistence of {@link Category} enums.
 */
public class CategoryConverter {

    /**
     * Converts a {@link Category} object to its String representation.
     *
     * @param category the {@link Category} object to convert. May be null.
     * @return the name of the category as a String, or null if the category is null.
     */
    @TypeConverter
    public static String fromCategory(Category category) {
        String categoryName = (category == null) ? null : category.getDisplayName();
        Log.d("CategoryConverter", "Converting from Category: " + category + " to String: " + categoryName);
        return categoryName;
    }
    /**
     * Converts a String representation of a category to a {@link Category} object.
     *
     * @param categoryString the String representation of the category. May be null.
     * @return the corresponding {@link Category} object, or null if the input is null.
     *         If the string does not match any valid {@link Category}, it defaults to {@link Category#ALL}.
     */
    @TypeConverter
    public static Category toCategory(String categoryString) {
        if (categoryString == null) {
            Log.d("CategoryConverter", "Converting from String: null to Category: null");
            return null;
        }
        try {
            Category category = Category.fromDisplayName(categoryString);
            Log.d("CategoryConverter", "Converting from String: " + categoryString + " to Category: " + category);
            return category;
        } catch (IllegalArgumentException e) {
            Log.e("CategoryConverter", "Invalid category string: " + categoryString + ". Defaulting to ALL.", e);
            return Category.ALL;
        }
    }
}

