package com.example.xchange.EditItem;

import android.util.Log;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.database.dao.ItemDao;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Presenter class for managing the logic of editing an item in the xChange app.
 * Handles interactions with the database through the ItemDao and performs updates on a background thread.
 */
public class EditItemPresenter {

    private final ItemDao itemDao;
    private final Executor executor;

    /**
     * Constructs an EditItemPresenter.
     *
     * @param itemDao  The DAO for accessing and modifying item data in the database.
     * @param executor The executor for performing database operations on a background thread.
     * @throws IllegalArgumentException If the provided executor is null.
     */
    public EditItemPresenter(ItemDao itemDao, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor cannot be null");
        }
        this.itemDao = itemDao;
        this.executor = executor;
    }

    /**
     * Updates an item with new details and persists the changes to the database.
     *
     * @param item        The item to update.
     * @param name        The updated name of the item.
     * @param description The updated description of the item.
     * @param condition   The updated condition of the item.
     * @param category    The updated category of the item.
     * @param images      The updated list of images associated with the item.
     */
    public void updateItem(Item item, String name, String description, String condition, String category, ArrayList<Image> images) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        executor.execute(() -> {
            try {
                Category itemCategory = Category.fromDisplayName(category);
                item.editItem(name,description, String.valueOf(itemCategory),condition,images);
                itemDao.updateItem(item);
            } catch (IllegalArgumentException e) {
                Log.e("EditItemPresenter", "Invalid category: " + category, e);
            } catch (Exception e) {
                Log.e("EditItemPresenter", "Error updating item", e);
            }
        });
    }

}
