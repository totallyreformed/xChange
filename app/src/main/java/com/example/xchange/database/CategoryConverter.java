// File: CategoryConverter.java
package com.example.xchange.database;

import android.util.Log;

import androidx.room.TypeConverter;
import com.example.xchange.Category;

// File: CategoryConverter.java

public class CategoryConverter {

    @TypeConverter
    public static String fromCategory(Category category) {
        String categoryName = (category == null) ? null : category.name();
        Log.d("CategoryConverter", "Converting from Category: " + category + " to String: " + categoryName);
        return categoryName;
    }

    @TypeConverter
    public static Category toCategory(String categoryString) {
        if (categoryString == null) {
            Log.d("CategoryConverter", "Converting from String: null to Category: null");
            return null;
        }
        try {
            Category category = Category.valueOf(categoryString);
            Log.d("CategoryConverter", "Converting from String: " + categoryString + " to Category: " + category);
            return category;
        } catch (IllegalArgumentException e) {
            // Log the exception and return a default category
            Log.e("CategoryConverter", "Invalid category string: " + categoryString + ". Defaulting to ALL.", e);
            return Category.ALL; // Default category
        }
    }
}

