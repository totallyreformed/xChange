package com.example.xchange.ProfileData;

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

import com.example.xchange.Counteroffer;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.CounteroffersAdapter;
import com.example.xchange.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class for displaying counteroffers in the xChange application.
 * <p>
 * This activity shows a list of sent or received counteroffers in a RecyclerView and allows
 * navigation to item details. It also handles system bar adjustments and provides a back button
 * to return to the previous screen.
 * </p>
 */
public class CounteroffersActivity extends AppCompatActivity {

    private CounteroffersAdapter adapter;
    private RecyclerView recyclerView;
    private List<Counteroffer> counterofferList = new ArrayList<>();
    private User currentUser;

    /**
     * Initializes the activity, sets up UI components, and handles intent data.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counteroffers);

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
        adapter = new CounteroffersAdapter(counterofferList, this::onCounterofferClicked);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Handles intent data to populate the RecyclerView with counteroffers.
     * If no valid data is provided, displays an error message and finishes the activity.
     */
    private void handleIntentData() {
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        String counterofferType = intent.getStringExtra("REQUEST_TYPE");
        ArrayList<Counteroffer> counteroffersFromIntent = intent.getParcelableArrayListExtra("COUNTEROFFERS");

        if (currentUser == null || counterofferType == null) {
            showToast("Invalid user or counteroffer type");
            finish();
            return;
        }

        populateCounteroffers(counteroffersFromIntent);
        displayTypeMessage(counterofferType);
    }

    /**
     * Populates the RecyclerView with counteroffers from the intent data.
     *
     * @param counteroffersFromIntent The list of counteroffers from the intent.
     */
    private void populateCounteroffers(ArrayList<Counteroffer> counteroffersFromIntent) {
        if (counteroffersFromIntent != null && !counteroffersFromIntent.isEmpty()) {
            counterofferList.addAll(counteroffersFromIntent);
            adapter.notifyDataSetChanged();
        } else {
            showToast("No counteroffers found");
        }
    }

    /**
     * Displays a message indicating the type of counteroffers being displayed.
     *
     * @param counterofferType The type of counteroffers (e.g., "COUNTER_OFFERS_SENT" or "COUNTER_OFFERS_RECEIVED").
     */
    private void displayTypeMessage(String counterofferType) {
        if ("COUNTER_OFFERS_SENT".equals(counterofferType)) {
            showToast("Displaying sent counteroffers...");
        } else if ("COUNTER_OFFERS_RECEIVED".equals(counterofferType)) {
            showToast("Displaying received counteroffers...");
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
     * Handles click events on a counteroffer, navigating to the item detail screen.
     *
     * @param counteroffer The clicked counteroffer.
     */
    private void onCounterofferClicked(Counteroffer counteroffer) {
        Intent intent = new Intent(this, ItemDetailActivity.class);
        intent.putExtra("ITEM_ID", counteroffer.getRequestedItem().getItemId());
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