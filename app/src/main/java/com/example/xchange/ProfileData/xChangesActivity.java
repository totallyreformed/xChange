package com.example.xchange.ProfileData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.xChange;
import com.example.xchange.xChangesAdapter;

import java.util.ArrayList;

/**
 * Activity class for displaying user exchanges (xChanges) in the xChange application.
 * <p>
 * This activity shows a list of exchanges in a RecyclerView and provides a back button
 * to return to the previous screen. It handles intent data to populate the list and
 * displays a message if no exchanges are found.
 * </p>
 */
public class xChangesActivity extends AppCompatActivity {

    private RecyclerView xChangesRecyclerView;
    private xChangesAdapter adapter;
    private ArrayList<xChange> xChangesList;
    private User currentUser;

    /**
     * Initializes the activity, sets up UI components, and handles intent data.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xchanges);

        initializeUI();
        handleIntentData();
        setupRecyclerView();
        setupBackButton();
        checkForEmptyList();
    }

    /**
     * Initializes UI components such as the RecyclerView and the back button.
     */
    private void initializeUI() {
        xChangesRecyclerView = findViewById(R.id.xChangesRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Handles intent data to populate the list of exchanges and retrieve the current user.
     * If no valid data is provided, displays an error message and finishes the activity.
     */
    private void handleIntentData() {
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        xChangesList = intent.getParcelableArrayListExtra("XCHANGES");

        if (currentUser == null || xChangesList == null) {
            showToast("Error loading xChanges data.");
            finish();
        }
    }

    /**
     * Configures the RecyclerView with a layout manager and an adapter.
     */
    private void setupRecyclerView() {
        xChangesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new xChangesAdapter(xChangesList);
        xChangesRecyclerView.setAdapter(adapter);
    }

    /**
     * Configures the back button to finish the activity and return to the previous screen.
     */
    private void setupBackButton() {
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Checks if the list of exchanges is empty and displays a message if no exchanges are found.
     */
    private void checkForEmptyList() {
        if (xChangesList.isEmpty()) {
            showToast("No xChanges found.");
        }
    }

    /**
     * Displays a toast message.
     *
     * @param message The message to display.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
