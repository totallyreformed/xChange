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

public class UploadViewModel extends AndroidViewModel {

    private final ItemDao itemDao;
    private final ExecutorService executor;
    private UploadPresenter uploadPresenter;

    public UploadViewModel(@NonNull Application application, xChanger xchanger) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        itemDao = db.itemDao();
        executor = Executors.newSingleThreadExecutor();
        uploadPresenter = new UploadPresenter(xchanger);
    }

    public void uploadItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages, Runnable onSuccess, java.util.function.Consumer<String> onFailure) {
        uploadPresenter.uploadItem(itemName, itemDescription, itemCategory, itemCondition, itemImages, onSuccess, onFailure);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}
