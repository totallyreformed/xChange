package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.R;
import com.example.xchange.User;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private TextView usernameTextView;
    private TextView itemsTextView; // TextView to display items

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTextView = findViewById(R.id.usernameTextView);
        itemsTextView = findViewById(R.id.itemsTextView); // Initialize the TextView for items

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Observe the LiveData for the username
        viewModel.getUsername().observe(this, username -> {
            if (username != null) {
                usernameTextView.setText("Username: " + username);
            } else {
                usernameTextView.setText("No username available");
            }
        });

        // Observe the LiveData for items
        viewModel.getItemsText().observe(this, itemsText -> {
            if (itemsText != null && !itemsText.isEmpty()) {
                itemsTextView.setText(itemsText);
            } else {
                itemsTextView.setText("No items available");
            }
        });

        // Pass the user object to the ViewModel
        User user = getIntent().getParcelableExtra("USER");
        viewModel.loadUser(user);
    }
}
