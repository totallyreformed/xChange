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
    private final MutableLiveData<String> itemsTextLiveData = new MutableLiveData<>();

    public MainActivityViewModel(Application application) {
        super(application);
        presenter = new MainActivityPresenter(this);
    }

    public LiveData<String> getUsername() {
        return usernameLiveData;
    }

    public LiveData<String> getItemsText() {
        return itemsTextLiveData;
    }

    public void loadUser(User user) {
        presenter.loadUser(user);
    }

    // Called by the Presenter to update username
    public void updateUsername(String username) {
        usernameLiveData.setValue(username);
    }

    // Called by the Presenter to update items' text
    public void updateItemsText(String itemsText) {
        itemsTextLiveData.setValue(itemsText);
    }
}
