package com.example.xchange.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.AcceptRequest.AcceptRequestActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.RequestsAdapter;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class AdminReceivedRequestsActivity extends AppCompatActivity {

    private RecyclerView adminReceivedRequestsRecyclerView;
    private RequestsAdapter requestsAdapter;
    private UserRepository userRepository;
    private User currentUser; // Admin user

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_received_requests);

        // Initialize Toolbar (Optional)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.adminReceivedRequestsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize RecyclerView
        adminReceivedRequestsRecyclerView = findViewById(R.id.adminReceivedRequestsRecyclerView);
        adminReceivedRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminReceivedRequestsRecyclerView.setHasFixedSize(true);

        // Initialize Adapter with empty list and currentUser
        currentUser = getAdminUser(); // Implement this method based on your app's logic
        requestsAdapter = new RequestsAdapter(
                new ArrayList<>(),
                currentUser,
                request -> {
                    // Navigate to AcceptRequestActivity
                    Intent intent = new Intent(AdminReceivedRequestsActivity.this, AcceptRequestActivity.class);
                    intent.putExtra("REQUEST", request);
                    intent.putExtra("USER", currentUser);
                    startActivity(intent);
                },
                this
        );
        adminReceivedRequestsRecyclerView.setAdapter(requestsAdapter);

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Fetch received requests
        userRepository.getReceivedRequestsAdmin(new UserRepository.RequestItemsCallback() {
            @Override
            public void onSuccess(List<Request> requests) {
                runOnUiThread(() -> requestsAdapter.setRequests(requests));
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> Toast.makeText(AdminReceivedRequestsActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    // Optional: Implement method to retrieve Admin user
    private User getAdminUser() {
        // Retrieve the Admin user from SharedPreferences, Intent, or any other method you use
        // For example:
        // return getIntent().getParcelableExtra("USER");
        return null; // Replace with actual retrieval logic
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userRepository.shutdownExecutor(); // Ensure to shutdown the executor to prevent leaks
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
