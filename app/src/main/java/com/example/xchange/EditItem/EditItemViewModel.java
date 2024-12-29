package com.example.xchange.EditItem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditItemViewModel extends AndroidViewModel {

    private static final int NUMBER_OF_THREADS = 4;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); // Δημιουργία Executor
    private final EditItemPresenter presenter;
    private final LiveData<Item> item;

    public EditItemViewModel(@NonNull Application application, long itemId) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        presenter = new EditItemPresenter(db.itemDao(), executor); // Πέρασμα Executor στον Presenter
        item = db.itemDao().getItemById(itemId);
    }

    public LiveData<Item> getItem() {
        return item;
    }

    public void updateItem(String name, String description, String condition, String category) {
        Item currentItem = item.getValue();
        if (currentItem != null) {
            presenter.updateItem(currentItem, name, description, condition, category);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown(); // Κλείσιμο του Executor για αποφυγή διαρροών μνήμης
    }
}
