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

public class xChangesActivity extends AppCompatActivity {

    private RecyclerView xChangesRecyclerView;
    private xChangesAdapter adapter;
    private ArrayList<xChange> xChangesList;
    private User currentUser;

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

    private void initializeUI() {
        xChangesRecyclerView = findViewById(R.id.xChangesRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        xChangesList = intent.getParcelableArrayListExtra("XCHANGES");

        if (currentUser == null || xChangesList == null) {
            showToast("Error loading xChanges data.");
            finish();
        }
    }

    private void setupRecyclerView() {
        xChangesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new xChangesAdapter(xChangesList);
        xChangesRecyclerView.setAdapter(adapter);
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void checkForEmptyList() {
        if (xChangesList.isEmpty()) {
            showToast("No xChanges found.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
