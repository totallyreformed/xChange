package com.example.xchange.EditItem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel class for managing the state and logic of the Edit Item feature in the xChange app.
 * Provides a bridge between the UI and the Presenter layer, handling item data and updates.
 */
public class EditItemViewModel extends AndroidViewModel {

    private static final int NUMBER_OF_THREADS = 4;
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); // Δημιουργία Executor
    private final EditItemPresenter presenter;
    private final LiveData<Item> item;

    /**
     * Constructs an EditItemViewModel.
     *
     * @param application The application context used to initialize the database and presenter.
     * @param itemId      The ID of the item to be edited.
     */
    public EditItemViewModel(@NonNull Application application, long itemId) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        presenter = new EditItemPresenter(db.itemDao(), executor); // Πέρασμα Executor στον Presenter
        item = db.itemDao().getItemById(itemId);
    }

    /**
     * Gets the LiveData object representing the item being edited.
     *
     * @return A LiveData object containing the item data.
     */
    public LiveData<Item> getItem() {
        return item;
    }

    /**
     * Updates the item with the specified details.
     *
     * @param name        The updated name of the item.
     * @param description The updated description of the item.
     * @param condition   The updated condition of the item.
     * @param category    The updated category of the item.
     * @param images      The updated list of images associated with the item.
     */
    public void updateItem(String name, String description, String condition, String category, ArrayList<Image> images) {
        Item currentItem = item.getValue();
        if (currentItem != null) {
            presenter.updateItem(currentItem, name, description, condition, category,images);
        }
    }

    /**
     * Cleans up resources when the ViewModel is no longer needed.
     * Shuts down the Executor to prevent memory leaks.
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown(); // Κλείσιμο του Executor για αποφυγή διαρροών μνήμης
    }
}
