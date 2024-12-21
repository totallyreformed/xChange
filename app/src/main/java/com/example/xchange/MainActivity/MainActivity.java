package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.R;
import com.example.xchange.Item;
import com.example.xchange.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private TextView usernameTextView;
    private TextView itemsTextView;  // TextView to display items

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameTextView = findViewById(R.id.usernameTextView);
        itemsTextView = findViewById(R.id.itemsTextView);  // Initialize the TextView for items

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // Retrieve the user object from the Intent
        User user = getIntent().getParcelableExtra("USER");

        if (user != null) {
            // Display the username
            usernameTextView.setText("Username: " + user.getUsername());
        }

        // Observe the LiveData for all items and update the UI
        viewModel.getAllItems().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                // Create a string to display all items
                StringBuilder itemsText = new StringBuilder();
                for (Item item : items) {
                    itemsText.append("Item: ").append(item.getItemName())
                            .append("\nCategory: ").append(item.getItemCategory())
                            .append("\nCondition: ").append(item.getItemCondition())
                            .append("\n\n");
                }
                // Set the items' text to the TextView
                itemsTextView.setText(itemsText.toString());
            } else {
                // Handle the case where there are no items
                itemsTextView.setText("No items available");
            }
        });
    }
}
