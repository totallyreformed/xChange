package com.example.xchange.MainActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.AcceptRequest.xChangeConfirmationActivity;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.ItemDetail.SeerequestsCounteroffersActivity;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.Notification;
import com.example.xchange.Profile.ProfileActivity;
import com.example.xchange.R;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.Upload.UploadActivity;
import com.example.xchange.User;
import com.example.xchange.MainActivity.AdminHomeFragment;
import com.example.xchange.MainActivity.XChangerHomeFragment;
import com.example.xchange.database.UserRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Main activity for the xChange application.
 * <p>
 * Handles the main navigation and user interface for different user types (Admin and xChanger).
 * Displays notifications, allows navigation to various sections (Browse, Search, Profile),
 * and manages item details, uploads, and user-related actions.
 * </p>
 */
public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton uploadFab;
    private User currentUser;

    /**
     * Initializes the activity components and sets up user-specific UI and logic.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        int requestedItemId = handleRequestedItemId(getIntent());
        if (requestedItemId != -1) {
            navigateToItemDetail(requestedItemId);
        }
        loadUserFragment();
        setupBottomNavigation(findViewById(R.id.bottomNavigationView));
        observeItems();
    }

    /**
     * Initializes UI components and sets up listeners for user actions.
     */
    private void initializeComponents() {
        uploadFab = findViewById(R.id.uploadFab);
        uploadFab.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }
            openUploadActivity();
        });

        currentUser = getIntent().getParcelableExtra("USER");

        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(getApplication(), this);
        viewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);

        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser);
        itemsAdapter.setOnItemClickListener(itemId -> navigateToItemDetail(Math.toIntExact(itemId)));

        List<Notification> notifications = getIntent().getParcelableArrayListExtra("NOTIFICATIONS");
        if (notifications != null && !notifications.isEmpty()) {
            showNotificationDialogsSequentially(notifications);
        }
    }

    /**
     * Handles navigation to an item detail screen if an item ID is provided in the intent.
     *
     * @param intent The intent containing the requested item ID.
     * @return The requested item ID if valid; -1 otherwise.
     */
    private int handleRequestedItemId(Intent intent) {
        int requestedItemId = -1;
        String requestedItemIdString = intent.getStringExtra("REQUESTED_ITEM_ID");

        if (requestedItemIdString != null) {
            try {
                requestedItemId = Integer.parseInt(requestedItemIdString);
                Toast.makeText(this, "Requested Item ID: " + requestedItemId, Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Requested Item ID", Toast.LENGTH_SHORT).show();
            }
        }

        return requestedItemId;
    }

    /**
     * Loads the appropriate fragment based on the current user's type.
     * Admin users see the AdminHomeFragment, while xChangers see the XChangerHomeFragment.
     */
    private void loadUserFragment() {
        if (currentUser != null) {
            String userType = currentUser.getUser_type();
            if ("admin".equalsIgnoreCase(userType)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AdminHomeFragment())
                        .commit();
                uploadFab.setVisibility(View.GONE);
                findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
            } else if ("xChanger".equalsIgnoreCase(userType)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new XChangerHomeFragment())
                        .commit();
            } else {
                Toast.makeText(this, "Unknown user type", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            finish();
        }
    }

    /**
     * Sets up the bottom navigation bar and its item selection listeners.
     *
     * @param bottomNavigationView The BottomNavigationView component.
     */
    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                return handleBrowseSelection();
            } else if (itemId == R.id.menu_search) {
                return handleSearchSelection();
            } else if (itemId == R.id.menu_profile) {
                return handleProfileSelection();
            } else {
                return false;
            }
        });
    }

    /**
     * Handles the Browse menu selection.
     *
     * @return True if handled successfully; false otherwise.
     */
    private boolean handleBrowseSelection() {
        return true;
    }

    /**
     * Handles the Search menu selection and navigates to the SearchActivity.
     *
     * @return True if handled successfully; false otherwise.
     */
    private boolean handleSearchSelection() {
        Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
        searchIntent.putExtra("USER", currentUser);
        startActivity(searchIntent);
        return true;
    }

    /**
     * Handles the Profile menu selection and navigates to the ProfileActivity.
     *
     * @return True if handled successfully; false otherwise.
     */
    private boolean handleProfileSelection() {
        Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
        profileIntent.putExtra("USER", currentUser);
        startActivity(profileIntent);
        return true;
    }

    /**
     * Navigates to the item detail screen for the given item ID.
     *
     * @param itemId The ID of the item to view.
     */
    private void navigateToItemDetail(int itemId) {
        Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
        detailIntent.putExtra("ITEM_ID", itemId);
        detailIntent.putExtra("USER", currentUser);
        startActivity(detailIntent);
    }

    /**
     * Observes the list of items and updates the adapter accordingly.
     */
    private void observeItems() {
        viewModel.getItemsList().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
        });
    }

    /**
     * Opens the UploadActivity for the current user.
     */
    private void openUploadActivity() {
        Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
        uploadIntent.putExtra("USER", currentUser);
        startActivity(uploadIntent);
    }

    /**
     * Displays notification dialogs sequentially for a list of notifications.
     *
     * @param notifications The list of notifications to display.
     */
    private void showNotificationDialogsSequentially(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            viewModel.deleteNotificationsForUser(currentUser.getUsername(), new UserRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Notifications cleared.", Toast.LENGTH_SHORT).show()
                    );
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Failed to clear notifications.", Toast.LENGTH_SHORT).show()
                    );
                }
            });
            return;
        }

        // Get the first notification to display.
        Notification currentNotification = notifications.remove(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage(currentNotification.getMessage())
                .setCancelable(false);

        /*
         * We differentiate notifications based on keywords in the message.
         * - "accepted": for accepted requests.
         * - "rejected": for notifications about rejected requests or counteroffers.
         * - "requested": for plain request notifications.
         * - "counteroffer": for notifications about counteroffers.
         * - "cancelled": for cancellations.
         */
        if (currentNotification.getMessage().contains("accepted")) {
            // Accepted request notification: offer a way to view the xChange details.
            builder.setPositiveButton("View xChange Details", (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, xChangeConfirmationActivity.class);
                intent.putExtra("XCHANGE_ID", currentNotification.getXChangeId()); // Assuming the notification includes this
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        } else if (currentNotification.getMessage().toLowerCase().contains("rejected")) {
            // Rejection notification: inform the user that their request/counteroffer was rejected.
            // Here, we provide a simple dismiss-only dialog.
            builder.setPositiveButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        } else if (currentNotification.getMessage().contains("requested")) {
            // Plain request notification: add a button for viewing the requested item.
            builder.setPositiveButton("View Requested Item", (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
                intent.putExtra("ITEM_ID", currentNotification.getItemId());
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        } else if (currentNotification.getMessage().contains("counteroffer")) {
            // Counteroffer notification: add a button for viewing the counteroffer.
            builder.setPositiveButton("View Counteroffer", (dialog, which) -> {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
                intent.putExtra("XCHANGE_ID", currentNotification.getXChangeId());
                intent.putExtra("HAS_COUNTEROFFER", true);
                intent.putExtra("ITEM_ID", currentNotification.getItemId());
                intent.putExtra("USER", currentUser);
                startActivity(intent);
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        } else if (currentNotification.getMessage().contains("cancelled")) {
            // Request cancellation notification: only a dismiss button.
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        } else {
            // Default notification type: only a dismiss button.
            builder.setNegativeButton("Dismiss", (dialog, which) -> {
                dialog.dismiss();
                showNotificationDialogsSequentially(notifications);
            });
        }

        builder.show();
    }




    /**
     * Returns the current logged-in user.
     *
     * @return The current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }
}