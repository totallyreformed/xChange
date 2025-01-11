package com.example.xchange.Upload;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for managing the upload functionality in the xChange application.
 * <p>
 * This ViewModel interacts with the {@link UploadPresenter} to handle item uploads
 * and uses an {@link ExecutorService} for asynchronous operations.
 * </p>
 */
public class UploadViewModel extends AndroidViewModel {

    private final ItemDao itemDao;
    private final ExecutorService executor;
    private UploadPresenter uploadPresenter;

    /**
     * Constructs an {@code UploadViewModel} with the specified application context and user.
     *
     * @param application The application context.
     * @param xchanger    The {@link xChanger} instance representing the current user.
     */
    public UploadViewModel(@NonNull Application application, xChanger xchanger) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        uploadPresenter = new UploadPresenter(xchanger);
    }

    /**
     * Uploads an item using the {@link UploadPresenter}.
     *
     * @param itemName        The name of the item to upload.
     * @param itemDescription A description of the item.
     * @param itemCategory    The category of the item.
     * @param itemCondition   The condition of the item (e.g., "New", "Used").
     * @param itemImages      A list of {@link Image} objects representing the item's images.
     * @param onSuccess       A callback to execute when the upload is successful.
     * @param onFailure       A callback to execute with an error message when the upload fails.
     */
    public void uploadItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages, Runnable onSuccess, java.util.function.Consumer<String> onFailure) {
        uploadPresenter.uploadItem(itemName, itemDescription, itemCategory, itemCondition, itemImages, onSuccess, onFailure);
    }

    /**
     * Cleans up resources when the ViewModel is cleared.
     * <p>
     * This method shuts down the {@link ExecutorService} to prevent memory leaks.
     * </p>
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
