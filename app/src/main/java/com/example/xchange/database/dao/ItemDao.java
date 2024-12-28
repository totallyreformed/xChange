package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Category;
import com.example.xchange.Item;

import java.util.List;

@Dao
public interface ItemDao {

    // Insert a new item
    @Insert
    long insertItem(Item item);

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getAllItems();

    /**
     * Search for items where the name contains the query string (case-insensitive).
     *
     * @param query The search query.
     * @return A list of matching items.
     */
    @Query("SELECT * FROM items WHERE LOWER(item_name) LIKE '%' || LOWER(:query) || '%'")
    List<Item> searchItemsByName(String query);

    /**
     * Retrieve items filtered by a specific category.
     *
     * @param category The category to filter by.
     * @return A list of items in the specified category.
     */
    @Query("SELECT * FROM items WHERE item_category = :category")
    List<Item> filterItemsByCategory(Category category);

    /**
     * Search for items by name and filter by category simultaneously.
     *
     * @param query    The search query.
     * @param category The category to filter by.
     * @return A list of items matching both criteria.
     */
    @Query("SELECT * FROM items WHERE LOWER(item_name) LIKE '%' || LOWER(:query) || '%' AND item_category = :category")
    List<Item> searchItemsByNameAndCategory(String query, Category category);

    // Retrieve an item by ID
    @Query("SELECT * FROM items WHERE itemId = :itemId LIMIT 1")
    LiveData<Item> getItemById(long itemId);

    // Update an existing item
    @Update
    void updateItem(Item item);

    // Delete an item
    @Delete
    void deleteItem(Item item);

    // Delete all items
    @Query("DELETE FROM items")
    void deleteAllItems();
}
