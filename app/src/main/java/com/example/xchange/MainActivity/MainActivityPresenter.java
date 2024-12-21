package com.example.xchange.MainActivity;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;

import java.util.List;

public class MainActivityPresenter {

    private final MainActivityViewModel viewModel;

    public MainActivityPresenter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void loadUser(User user) {
        if (user != null) {
            viewModel.updateUsername(user.getUsername());
            loadItems();
        }
    }

    private void loadItems() {
        // Observe LiveData instead of trying to fetch synchronously
        AppDatabase.getItemDao().getAllItems().observeForever(items -> {
            if (items != null && !items.isEmpty()) {
                StringBuilder itemsText = new StringBuilder();
                for (Item item : items) {
                    itemsText.append("Item: ").append(item.getItemName())
                            .append("\nCategory: ").append(item.getItemCategory())
                            .append("\nCondition: ").append(item.getItemCondition())
                            .append("\nDescription: ").append(item.getItemDescription())
                            .append("\n\n");
                }
                viewModel.updateItemsText(itemsText.toString());
            } else {
                viewModel.updateItemsText("No items available");
            }
        });
    }

}
