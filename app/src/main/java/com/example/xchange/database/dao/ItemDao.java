package com.example.xchange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.xchange.Item;

import java.util.List;

@Dao
public interface ItemDao {

    // Insert a new item
    @Insert
    void insertItem(Item item);

    @Query("SELECT * FROM items")
    LiveData<List<Item>> getAllItems();

    // Retrieve an item by ID
    @Query("SELECT * FROM items WHERE itemId = :itemId")
    Item getItemById(long itemId);

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
