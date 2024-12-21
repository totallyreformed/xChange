package com.example.xchange.MainActivity;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.xchange.Item;
import com.example.xchange.User;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.ItemDao;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<User> userLiveData;
    private LiveData<List<Item>> allItemsLiveData;
    private ItemDao itemDao;

    public MainActivityViewModel(Application application) {
        super(application);
        userLiveData = new MutableLiveData<>();
        itemDao=AppDatabase.getItemDao();
        allItemsLiveData = itemDao.getAllItems();
    }

    public MutableLiveData<User> getUserLiveData() {
        return userLiveData;
    }

    public void setUser(User user) {
        userLiveData.setValue(user);
    }
    public LiveData<List<Item>> getAllItems() {
        return allItemsLiveData;
    }
}
