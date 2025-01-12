package com.example.xchange.ProfileData;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Activity class for displaying sent or received requests in the xChange application.
 * <p>
 * This activity shows a list of requests in a RecyclerView and allows navigation to the
 * item details. It also adjusts system bars and provides a back button to return to the
 * previous screen.
 * </p>
 */
public class RequestsActivity extends AppCompatActivity {

    private RequestsAdapter adapter;
    private RecyclerView recyclerView;
    private List<Request> requestList = new ArrayList<>();
    private User currentUser;

    /**
     * Initializes the activity, sets up UI components, and handles intent data.
     *
     * @param savedInstanceState The saved state of the activity.
     */
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

    /**
     * Initializes UI components such as the RecyclerView and the back button.
     */
    private void initializeUI() {
        recyclerView = findViewById(R.id.requestsRecyclerView);
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Adjusts system bars to ensure proper padding for UI elements.
     */
    private void adjustSystemBars() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Configures the RecyclerView with a layout manager and an adapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestsAdapter(requestList, null, this::onRequestClicked, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Handles intent data to populate the RecyclerView with requests.
     * If no valid data is provided, displays an error message and finishes the activity.
     */
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

    /**
     * Populates the RecyclerView with requests from the intent data.
     *
     * @param requestsFromIntent The list of requests from the intent.
     */
    private void populateRequests(ArrayList<Request> requestsFromIntent) {
        if (requestsFromIntent != null && !requestsFromIntent.isEmpty()) {
            requestList.addAll(requestsFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            showToast("No requests found");
        }
    }

    /**
     * Displays a message indicating the type of requests being displayed.
     *
     * @param requestType The type of requests (e.g., "SENT" or "RECEIVED").
     */
    private void loadRequests(String requestType) {
        if ("SENT".equals(requestType)) {
            showToast("Displaying sent requests...");
        } else if ("RECEIVED".equals(requestType)) {
            showToast("Displaying received requests...");
        }
    }

    /**
     * Configures the back button to finish the activity and return to the previous screen.
     */
    private void setupBackButton() {
        Button backButton = findViewById(R.id.backToProfileButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Handles click events on a request, navigating to the item detail screen.
     *
     * @param request The clicked request.
     */
    private void onRequestClicked(Request request) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("ITEM_ID", request.getRequestedItem().getItemId());
        intent.putExtra("USER", currentUser);
        startActivity(intent);
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