package com.example.xchange.MainActivity;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.User;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private final MainActivityPresenter presenter;
    private final MutableLiveData<String> usernameLiveData = new MutableLiveData<>();
    private LiveData<List<Item>> itemsLiveData;

    public MainActivityViewModel(Application application) {
        super(application);
        presenter = new MainActivityPresenter(this);
        itemsLiveData = presenter.loadItems();
    }

    // Getter for username LiveData
    public LiveData<String> getUsername() {
        return usernameLiveData;
    }

    // Getter for items LiveData
    public LiveData<List<Item>> getItemsList() {
        return itemsLiveData;
    }

    // Method to load user
    public void loadUser(User user) {
        presenter.loadUser(user);
    }

    // Called by the Presenter to update username
    public void updateUsername(String username) {
        usernameLiveData.setValue(username);
    }

}
