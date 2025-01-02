package com.example.xchange.MainActivity;

import com.example.xchange.Admin.AdminItemsActivity;
import com.example.xchange.Admin.AdminReceivedRequestsActivity;
import com.example.xchange.Admin.AdminSentRequestsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import Button
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

public class AdminHomeFragment extends Fragment {

    // UI Components
    private TextView adminWelcomeTextView;
    private TextView adminTotalItemsTextView;
    private Button adminViewItemsButton;
    private TextView adminRequestsSentCountTextView;
    private Button adminViewRequestsSentButton;
    private TextView adminRequestsReceivedCountTextView;
    private Button adminViewRequestsReceivedButton;
    private TextView totalCategoriesTextView; // LATER


    private UserRepository userRepository;
    private User currentUser;
    private MainActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the updated admin_home layout
        View view = inflater.inflate(R.layout.admin_home, container, false);

        // Initialize Views
        adminWelcomeTextView = view.findViewById(R.id.adminWelcomeTextView);
        adminTotalItemsTextView = view.findViewById(R.id.adminTotalItemsTextView);
        adminViewItemsButton = view.findViewById(R.id.adminViewItemsButton);
        adminRequestsSentCountTextView = view.findViewById(R.id.adminRequestsSentCountTextView);
        adminViewRequestsSentButton = view.findViewById(R.id.adminViewRequestsSentButton);
        adminRequestsReceivedCountTextView = view.findViewById(R.id.adminRequestsReceivedCountTextView);
        adminViewRequestsReceivedButton = view.findViewById(R.id.adminViewRequestsReceivedButton);
        // Initialize additional views here

        // Initialize UserRepository
        userRepository = new UserRepository(getContext());

        // Retrieve the current user from the hosting activity
        if (getActivity() instanceof MainActivity) {
            currentUser = ((MainActivity) getActivity()).getCurrentUser();
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        if (currentUser != null) {
            // Set Welcome Text
            adminWelcomeTextView.setText("Welcome " + currentUser.getUsername() + "!");

            // Fetch and observe all statistics
            viewModel.fetchAllStatistics();

            // Observe Total Items
            userRepository.getItemCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer totalItems) {
                    if (totalItems != null) {
                        adminTotalItemsTextView.setText("Total Items: " + totalItems);
                    } else {
                        adminTotalItemsTextView.setText("Total Items: 0");
                    }
                }
            });

            // Observe Requests Sent
            userRepository.getSentRequestsCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer requestsSent) {
                    if (requestsSent != null) {
                        adminRequestsSentCountTextView.setText("Requests Sent: " + requestsSent);
                    } else {
                        adminRequestsSentCountTextView.setText("Requests Sent: 0");
                    }
                }
            });

            // Observe Requests Received
            userRepository.getReceivedRequestsCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer requestsReceived) {
                    if (requestsReceived != null) {
                        adminRequestsReceivedCountTextView.setText("Requests Received: " + requestsReceived);
                    } else {
                        adminRequestsReceivedCountTextView.setText("Requests Received: 0");
                    }
                }
            });

            // Observe Total Exchanges (Assuming it's similar to Requests Received)
            viewModel.getTotalExchangesLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer count) {
                    adminRequestsReceivedCountTextView.setText(count + " Requests Received");
                }
            });

            // Observe Total Categories (If needed)
            viewModel.getTotalCategoriesLiveData().observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer count) {
                    // If you have a TextView for total categories, set it here
                    // Example:
                    // adminTotalCategoriesTextView.setText("Total Categories: " + count);
                }
            });
        } else {
            Toast.makeText(getContext(), "Current user is null", Toast.LENGTH_SHORT).show();
        }

        // Set Button Click Listeners

        // View Items Button
        adminViewItemsButton.setOnClickListener(v -> {
            // Navigate to Items List Activity or Fragment
            // Example:
            Intent intent = new Intent(getActivity(), AdminItemsActivity.class);
            startActivity(intent);
        });

        // View Requests Sent Button
        adminViewRequestsSentButton.setOnClickListener(v -> {
            // Navigate to Sent Requests Activity or Fragment
            Intent intent = new Intent(getActivity(), AdminSentRequestsActivity.class);
            startActivity(intent);
        });

        // View Requests Received Button
        adminViewRequestsReceivedButton.setOnClickListener(v -> {
            // Navigate to Received Requests Activity or Fragment
            Intent intent = new Intent(getActivity(), AdminReceivedRequestsActivity.class);
            startActivity(intent);
        });

        // Set listeners for additional buttons as needed

        return view;
    }

}