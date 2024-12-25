// File: ItemDetailViewModel.java
package com.example.xchange.ItemDetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;

public class ItemDetailViewModel extends AndroidViewModel {

    private final ItemDao itemDao;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
    }

    /**
     * Retrieves an Item by its ID.
     *
     * @param itemId The unique identifier of the item.
     * @return LiveData containing the Item.
     */
    public LiveData<Item> getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }
}
