// RequestPresenter.java
package com.example.xchange.request;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.xchange.Item;
import com.example.xchange.Request;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChanger;

import java.util.List;

public class RequestPresenter {

    private final UserRepository userRepository;
    private final RequestViewModel viewModel;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public RequestPresenter(Context context, RequestViewModel viewModel) {
        this.userRepository = new UserRepository(context);
        this.viewModel = viewModel;
    }

    public void loadUserItems(String username) {
        userRepository.getItemsByUsername(username, new UserRepository.UserItemsCallback() {
            @Override
            public void onSuccess(List<Item> items) {
                mainThreadHandler.post(() -> viewModel.updateUserItems(items));
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> viewModel.updateUserItems(null));
            }
        });
    }

    public void createRequest(xChanger requester, xChanger requestee, Item offeredItem, Item requestedItem) {
        Request request = new Request(
                requester,
                requestee,
                offeredItem,
                requestedItem,null);

        userRepository.saveRequest(request, new UserRepository.SaveRequestCallback() {
            @Override
            public void onSuccess() {
                mainThreadHandler.post(() -> {
                });
            }

            @Override
            public void onFailure(String message) {
                mainThreadHandler.post(() -> {
                });
            }
        });
    }


}