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

        // Retrieve Intent data
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        xChangesList = intent.getParcelableArrayListExtra("XCHANGES");

        if (currentUser == null || xChangesList == null) {
            Toast.makeText(this, "Error loading xChanges data.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize RecyclerView
        xChangesRecyclerView = findViewById(R.id.xChangesRecyclerView);
        xChangesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new xChangesAdapter(xChangesList);
        xChangesRecyclerView.setAdapter(adapter);

        // Handle back button
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());

        // Show a message if there are no xChanges
        if (xChangesList.isEmpty()) {
            Toast.makeText(this, "No xChanges found.", Toast.LENGTH_SHORT).show();
        }
    }
}
