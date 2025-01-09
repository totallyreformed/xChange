package com.example.xchange.ProfileData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.RequestsAdapter;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {

    private RequestsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Request> requestList = new ArrayList<>();
    private User currentUser; // Store current user globally
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.requestsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve the current user and request type from the intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER"); // Assign to global variable
        String requestType = intent.getStringExtra("REQUEST_TYPE");
        ArrayList<Request> requestsFromIntent = intent.getParcelableArrayListExtra("REQUESTS");

        if (currentUser == null || requestType == null) {
            Toast.makeText(this, "Invalid user or request type", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize the adapter with the click listener
        adapter = new RequestsAdapter(requestList, currentUser, this::onRequestClicked, context);
        recyclerView.setAdapter(adapter);

        if (requestsFromIntent != null && !requestsFromIntent.isEmpty()) {
            requestList.addAll(requestsFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No requests found", Toast.LENGTH_SHORT).show();
        }

        loadRequests(requestType, currentUser);
    }

    private void loadRequests(String requestType, User currentUser) {
        if ("SENT".equals(requestType)) {
            Toast.makeText(this, "Displaying sent requests...", Toast.LENGTH_SHORT).show();
        } else if ("RECEIVED".equals(requestType)) {
            Toast.makeText(this, "Displaying received requests...", Toast.LENGTH_SHORT).show();
        }
    }

    private void onRequestClicked(Request request) { // Only one parameter needed
        Intent intent = new Intent(this, ItemDetailActivity.class);

        // Pass the required data to ItemDetailActivity
        intent.putExtra("ITEM_ID", request.getRequestedItem().getItemId());
        intent.putExtra("USER", currentUser); // Use the global variable

        startActivity(intent);
    }
}
