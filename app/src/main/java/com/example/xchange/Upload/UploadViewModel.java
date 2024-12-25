package com.example.xchange.Upload;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.xchange.Item;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadViewModel extends AndroidViewModel {

    private final ItemDao itemDao;
    private final ExecutorService executor;

    public UploadViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Uploads the item to the database.
     *
     * @param item          The item to upload.
     * @param onSuccess     Callback for successful upload.
     * @param onFailure     Callback for failed upload.
     */
    public void uploadItem(Item item, Runnable onSuccess, java.util.function.Consumer<String> onFailure) {
        executor.execute(() -> {
            try {
                itemDao.insertItem(item);
                onSuccess.run();
            } catch (Exception e) {
                onFailure.accept(e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
