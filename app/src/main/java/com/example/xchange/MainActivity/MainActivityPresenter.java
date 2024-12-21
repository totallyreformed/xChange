package com.example.xchange.MainActivity;

import android.content.Context;

import com.example.xchange.User;

public class MainActivityPresenter {

    private MainActivityViewModel viewModel;

    public MainActivityPresenter(MainActivityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void loadUser(User user) {
        if (user != null) {
            viewModel.setUser(user);
        }
    }
}
