package com.example.xchange.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.AcceptRequest.AcceptRequestActivity;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.RequestsAdapter;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing all sent requests in the xChange app as an admin user.
 * Allows navigation to AcceptRequestActivity to handle individual requests.
 */
public class AdminSentRequestsActivity extends AppCompatActivity {

    private RecyclerView adminSentRequestsRecyclerView;
    private RequestsAdapter requestsAdapter;
    private UserRepository userRepository;
    private User currentUser; // Admin user

    /**
     * Called when the activity is created.
     * Sets up the RecyclerView, toolbar, and fetches sent requests data.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the saved data.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sent_requests);

        // Initialize Toolbar (Optional)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.adminSentRequestsToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hides the title text
        }

        // Initialize RecyclerView
        adminSentRequestsRecyclerView = findViewById(R.id.adminSentRequestsRecyclerView);
        adminSentRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminSentRequestsRecyclerView.setHasFixedSize(true);

        // Initialize Adapter with empty list and currentUser
        currentUser = getAdminUser(); // Implement this method based on your app's logic
        requestsAdapter = new RequestsAdapter(
                new ArrayList<>(),
                currentUser,
                request -> {
                    // Navigate to AcceptRequestActivity
                    Intent intent = new Intent(AdminSentRequestsActivity.this, AcceptRequestActivity.class);
                    intent.putExtra("REQUEST", request);
                    intent.putExtra("USER", currentUser);
                    startActivity(intent);
                },
                this
        );
        adminSentRequestsRecyclerView.setAdapter(requestsAdapter);

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Fetch sent requests
        userRepository.getSentRequests(new UserRepository.RequestItemsCallback() {
            @Override
            public void onSuccess(List<Request> requests) {
                runOnUiThread(() -> requestsAdapter.setRequests(requests));
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> Toast.makeText(AdminSentRequestsActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * Retrieves the admin user.
     * This method should be implemented based on the application's logic for retrieving the admin user.
     *
     * @return The admin user.
     */
    private User getAdminUser() {
        // Retrieve the Admin user from SharedPreferences, Intent, or any other method you use
        // For example:
        // return getIntent().getParcelableExtra("USER");
        return null; // Replace with actual retrieval logic
    }

    /**
     * Called when the activity is destroyed.
     * Ensures proper cleanup of resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        userRepository.shutdownExecutor(); // Ensure to shutdown the executor to prevent leaks
    }

    /**
     * Handles the navigation up action.
     *
     * @return True if the action was handled successfully.
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
