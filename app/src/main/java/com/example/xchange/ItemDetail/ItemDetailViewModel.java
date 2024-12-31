package com.example.xchange.ItemDetail;
import java.util.concurrent.Executors;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.database.dao.ItemDao;

import java.util.concurrent.Executor;
public class ItemDetailViewModel extends AndroidViewModel {
    private final ItemDao itemDao;
    private final Executor executor;
    private UserRepository repository;

    public ItemDetailViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        this.repository=new UserRepository(application);// Δημιουργία τοπικού executor
    }

    public LiveData<Item> getItemById(long itemId) {
        return itemDao.getItemById(itemId);
    }

    public void deleteItemById(long itemId) {
        executor.execute(() -> itemDao.deleteItemById(itemId));
    }
    public LiveData<User> getUserByUsername(String name){
        return this.repository.getUserByUsername(name);
    }
}

