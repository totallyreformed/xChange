package com.example.xchange.MainActivity;

import androidx.lifecycle.LiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter {
    private final MainActivityViewModel viewModel;

    public MainActivityPresenter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void loadUser(User user) {
        if (user != null) {
            viewModel.updateUsername(user.getUsername());
        } else {
            viewModel.updateUsername("Guest");
        }
    }

    public LiveData<List<Item>> loadItems() {
        // Fetch items from database (returns LiveData)
        return AppDatabase.getItemDao().getAllItems();
    }
}


