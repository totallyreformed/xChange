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
    @Query("SELECT * FROM items")
    List<Item> getAllItemsSync();

    @Query("SELECT * FROM items WHERE LOWER(item_name) LIKE '%' || LOWER(:query) || '%'")
    List<Item> searchItemsByName(String query);

    @Query("SELECT * FROM items WHERE item_category = :category")
    List<Item> filterItemsByCategory(Category category);

    @Query("SELECT * FROM items WHERE (:query IS NULL OR LOWER(item_name) LIKE '%' || LOWER(:query) || '%') AND item_category = :category")
    List<Item> searchItemsByNameAndCategory(String query, Category category);


    // Retrieve an item by ID
    @Query("SELECT * FROM items WHERE itemId = :itemId LIMIT 1")
    LiveData<Item> getItemById(long itemId);

    @Update
    void update(Item item);

    @Query("SELECT * FROM items WHERE itemId = :itemId")
    Item getItemByIdSync(long itemId);


    @Query("SELECT COUNT(*) from items")
    LiveData<Integer> getItemCount();

    // Update an existing item
    @Update
    void updateItem(Item item);

    // Delete an item
    @Query("DELETE FROM items WHERE itemId = :itemId")
    void deleteItemById(long itemId);

    // Delete an item object
    @Delete
    void deleteItem(Item item);

    @Query("SELECT * FROM items WHERE xChanger = :xChangerUsername")
    List<Item> getItemsByXChanger(String xChangerUsername);


    // Delete all items
    @Query("DELETE FROM items")
    void deleteAllItems();

    @Query("SELECT COUNT(*) FROM items")
    int getTotalItems();
}