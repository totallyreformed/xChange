package com.example.xchange.Upload;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Presenter class for managing the upload functionality in the xChange application.
 * <p>
 * This class interacts with the {@link xChanger} model to upload items and handle success or failure callbacks.
 * </p>
 */
public class UploadPresenter {

    private final xChanger xchanger;

    /**
     * Constructs an {@code UploadPresenter} with the given xChanger instance.
     *
     * @param xchanger The xChanger instance representing the current user.
     */
    public UploadPresenter(xChanger xchanger) {
        this.xchanger = xchanger;
    }

    /**
     * Uploads an item using the xChanger's upload functionality.
     *
     * @param itemName        The name of the item to upload.
     * @param itemDescription A description of the item.
     * @param itemCategory    The category of the item.
     * @param itemCondition   The condition of the item (e.g., "New", "Used").
     * @param itemImage       A list of {@link Image} objects representing the item's images.
     * @param onSuccess       A callback to execute when the upload is successful.
     * @param onFailure       A callback to execute with an error message when the upload fails.
     */
    public void uploadItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImage, Runnable onSuccess, Consumer<String> onFailure) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        try {
            xchanger.UploadItem(itemName, itemDescription, itemCategory, itemCondition, itemImage);
            onSuccess.run();
        } catch (Exception e) {
            onFailure.accept("Failed to upload item: " + e.getMessage());
        }
    }
}
