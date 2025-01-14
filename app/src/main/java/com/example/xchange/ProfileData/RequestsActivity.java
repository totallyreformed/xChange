package com.example.xchange.ProfileData;

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
import com.example.xchange.RequestsAdapter.OnRequestClickListener;
import com.example.xchange.User;
import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {

    private RequestsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Request> requestList = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        initializeUI();
        adjustSystemBars();
        setupRecyclerView();
        handleIntentData();
        setupBackButton();
    }

    private void initializeUI() {
        recyclerView = findViewById(R.id.requestsRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void adjustSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Update the RecyclerView setup to pass only requestList and the listener.
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Pass currentUser, the request list, and the click listener (this::onRequestClicked)
        adapter = new RequestsAdapter(requestList, currentUser, this::onRequestClicked, this);
        recyclerView.setAdapter(adapter);
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        String requestType = intent.getStringExtra("REQUEST_TYPE");
        ArrayList<Request> requestsFromIntent = intent.getParcelableArrayListExtra("REQUESTS");

        if (currentUser == null || requestType == null) {
            showToast("Invalid user or request type");
            finish();
            return;
        }

        populateRequests(requestsFromIntent);
        loadRequests(requestType);
    }

    private void populateRequests(ArrayList<Request> requestsFromIntent) {
        if (requestsFromIntent != null && !requestsFromIntent.isEmpty()) {
            requestList.clear();
            requestList.addAll(requestsFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            showToast("No requests found");
        }
    }

    private void loadRequests(String requestType) {
        if ("SENT".equals(requestType)) {
            showToast("Displaying sent requests...");
        } else if ("RECEIVED".equals(requestType)) {
            showToast("Displaying received requests...");
        }
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    // Now, the onRequestClicked method should get triggered when an item is clicked.
    private void onRequestClicked(Request request) {
        Log.d("RequestsActivity", "Request clicked: " + request.getRequestId());
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("ITEM_ID", request.getRequestedItem().getItemId());
        intent.putExtra("USER", currentUser);
        // Optionally, if you want to send the complete Request object:
        intent.putExtra("REQUEST", request);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
