package com.example.xchange.Upload;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.function.Consumer;

public class UploadPresenter {

    private final xChanger xchanger;

    public UploadPresenter(xChanger xchanger) {
        this.xchanger = xchanger;
    }

    public void uploadItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImage, Runnable onSuccess, Consumer<String> onFailure) {
        try {
            xchanger.UploadItem(itemName, itemDescription, itemCategory, itemCondition, itemImage);
            onSuccess.run();
        } catch (Exception e) {
            onFailure.accept("Failed to upload item: " + e.getMessage());
        }
    }
}
