package com.example.xchange.Admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.xChange;
import com.example.xchange.xChangesAdapter;
import com.example.xchange.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for displaying and managing all xChanges in the xChange app as an admin user.
 * Provides a list of completed exchanges (xChanges) for administrative review or management.
 */
public class AdminXChangesActivity extends AppCompatActivity {
    private RecyclerView adminXChangesRecyclerView;
    private xChangesAdapter xChangesAdapter;
    private UserRepository userRepository;
    private User currentUser; // Admin user

    /**
     * Called when the activity is created.
     * Sets up the RecyclerView, toolbar, and fetches xChanges data.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the saved data.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_xchanges);

        // Initialize Toolbar (Optional)
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.adminXChangesToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hides the title text
        }

        // Initialize RecyclerView
        adminXChangesRecyclerView = findViewById(R.id.adminXChangesRecyclerView);
        adminXChangesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adminXChangesRecyclerView.setHasFixedSize(true);

        // Initialize Adapter with empty list
        xChangesAdapter = new xChangesAdapter(new ArrayList<>());
        adminXChangesRecyclerView.setAdapter(xChangesAdapter);

        // Initialize UserRepository
        userRepository = new UserRepository(this);

        // Fetch xChanges
        userRepository.getAllXChanges(new UserRepository.UserXChangesCallback() {
            @Override
            public void onSuccess(List<xChange> xChanges) {
                runOnUiThread(() -> xChangesAdapter.setXChanges(xChanges));
            }

            @Override
            public void onFailure(String message) {
                runOnUiThread(() -> Toast.makeText(AdminXChangesActivity.this, message, Toast.LENGTH_SHORT).show());
            }
        });
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