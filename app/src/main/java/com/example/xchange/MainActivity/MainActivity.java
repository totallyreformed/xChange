package com.example.xchange.MainActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xchange.AcceptRequest.xChangeConfirmationActivity;
import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.Notification;
import com.example.xchange.Request;
import com.example.xchange.Upload.UploadActivity;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.UserRepository;
import com.example.xchange.xChange;
import com.example.xchange.xChanger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemsAdapter;
import com.example.xchange.Profile.ProfileActivity;
import com.example.xchange.R;
import com.example.xchange.Search.SearchActivity;
import com.example.xchange.User;
import com.example.xchange.MainActivity.AdminHomeFragment;
import com.example.xchange.MainActivity.XChangerHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel viewModel;
    private ItemsAdapter itemsAdapter;
    private FloatingActionButton uploadFab;
    private User currentUser;
    private UserRepository userRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppDatabase appDatabase = AppDatabase.getInstance(this);
        userRepository = new UserRepository(this);

        Intent intent = getIntent();
        uploadFab = findViewById(R.id.uploadFab);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);


        currentUser = intent.getParcelableExtra("USER");
        List<Notification> notifications = getIntent().getParcelableArrayListExtra("NOTIFICATIONS");

        // Show notifications after MainActivity is fully loaded
        if (notifications != null && !notifications.isEmpty()) {
            showNotificationDialogsSequentially(notifications);
        }

            int requestedItemId = -1; // Default value if parsing fails
            String requestedItemIdString = intent.getStringExtra("REQUESTED_ITEM_ID");
            if (requestedItemIdString != null) {
                try {
                    requestedItemId = Integer.parseInt(requestedItemIdString);
                    Toast.makeText(this, "Requested Item ID: " + requestedItemId, Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid Requested Item ID", Toast.LENGTH_SHORT).show();
                }
            }


        if (currentUser != null) {
            String userType = currentUser.getUser_type();
            if ("admin".equalsIgnoreCase(userType)) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AdminHomeFragment())
                        .commit();
                uploadFab.setVisibility(View.GONE);
                bottomNavigationView.setVisibility(View.GONE);
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

        uploadFab.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent uploadIntent = new Intent(MainActivity.this, UploadActivity.class);
            uploadIntent.putExtra("USER", currentUser);
            startActivity(uploadIntent);
        });



        // Use the custom factory to instantiate the ViewModel
        MainActivityViewModelFactory factory = new MainActivityViewModelFactory(getApplication());
        viewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);

        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser);
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(MainActivity.this, ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", currentUser);
            startActivity(detailIntent);
        });

        viewModel.getItemsList().observe(this, items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
        });

        // BottomNavigationView setup
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_browse) {
                // Currently in MainActivity, no action needed
                return true;
            } else if (itemId == R.id.menu_search) {
                // Navigate to SearchActivity
                Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                searchIntent.putExtra("USER", currentUser);
                startActivity(searchIntent);
                return true;
            } else if (itemId == R.id.menu_profile) {
                // Navigate to ProfileActivity
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                profileIntent.putExtra("USER", currentUser);
                startActivity(profileIntent);
                return true;
            } else {
                return false;
            }
        });
    }

    private void showNotificationDialogsSequentially(List<Notification> notifications) {
        if (notifications.isEmpty()) {
            // Clear notifications from the database after showing all dialogs
            userRepository.deleteNotificationsForUser(currentUser.getUsername(), new UserRepository.OperationCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Notifications cleared.", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onFailure(String message) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to clear notifications.", Toast.LENGTH_SHORT).show());
                }
            });
            return;
        }

        // Show the first notification
        Notification currentNotification = notifications.remove(0);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Notification")
                .setMessage(currentNotification.getMessage())
                .setCancelable(false);

        // Check if the notification is related to xChange acceptance
        if (currentNotification.getMessage().contains("accepted")) {
            builder.setPositiveButton("View xChange Details", (dialog, which) -> {
                dialog.dismiss();

                xChanger xchanger = new xChanger(
                        currentUser.getUsername(),
                        currentUser.getEmail(),
                        currentUser.getJoin_Date(),
                        currentUser.getPassword(),
                        currentUser.getLocation()
                );

                // Retrieve the xChange details
                Long xChangeId = currentNotification.getXChangeId(); // Assuming metadata contains xChangeId
                if (xChangeId != null) {
                    // Fetch the xChange details from the database
                    LiveData<xChange> xChangeLiveData = userRepository.getXChangeById(xChangeId);

                    xChangeLiveData.observe(MainActivity.this, xChange -> {
                        if (xChange != null) {
                            Intent intent = new Intent(MainActivity.this, xChangeConfirmationActivity.class);
                            intent.putExtra("XCHANGE_ID", xChangeId);
                            intent.putExtra("USER", xchanger); // Ensure `currentUser` is a `xChanger`
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Error loading xChange details. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "xChange ID not found in notification metadata.", Toast.LENGTH_SHORT).show();
                }

                showNotificationDialogsSequentially(notifications); // Continue with the next notification
            });
        }

        builder.setNegativeButton("Dismiss", (dialog, which) -> {
            dialog.dismiss();
            showNotificationDialogsSequentially(notifications); // Show next notification
        });

        runOnUiThread(builder::show);
    }



    public User getCurrentUser() {
        return currentUser;
    }
}