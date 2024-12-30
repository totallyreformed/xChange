package com.example.xchange.EditItem;

import android.util.Log;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.database.dao.ItemDao;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class EditItemPresenter {

    private final ItemDao itemDao;
    private final Executor executor;

    public EditItemPresenter(ItemDao itemDao, Executor executor) {
        if (executor == null) {
            throw new IllegalArgumentException("Executor cannot be null");
        }
        this.itemDao = itemDao;
        this.executor = executor;
    }

    public void updateItem(Item item, String name, String description, String condition, String category, ArrayList<Image> images) {
        executor.execute(() -> {
            try {
                Category itemCategory = Category.fromDisplayName(category);
                item.setItemName(name);
                item.setItemDescription(description);
                item.setItemCondition(condition);
                item.setItemCategory(itemCategory);
                item.setItemImages(images); // Assuming `setItemImages` exists in your `Item` class
                itemDao.updateItem(item);
            } catch (IllegalArgumentException e) {
                Log.e("EditItemPresenter", "Invalid category: " + category, e);
            } catch (Exception e) {
                Log.e("EditItemPresenter", "Error updating item", e);
            }
        });
    }

}
