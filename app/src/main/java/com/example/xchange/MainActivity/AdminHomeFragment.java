package com.example.xchange.MainActivity;

import com.example.xchange.Admin.AdminItemsActivity;
import com.example.xchange.Admin.AdminSentRequestsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Login.LoginActivity;
import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;

public class AdminHomeFragment extends Fragment {

    // UI Components
    private TextView adminWelcomeTextView;
    private TextView adminTotalItemsTextView;
    private Button adminViewItemsButton;
    private TextView adminRequestsCountTextView;
    private Button adminViewRequestsButton;
    private TextView totalUsersTextView;
    private TextView totalCategoriesTextView;

    private UserRepository userRepository;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the updated admin_home layout
        View view = inflater.inflate(R.layout.admin_home, container, false);
        Button adminLogoutButton = view.findViewById(R.id.adminLogoutButton);

        // Handle Logout Button
        adminLogoutButton.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(getActivity(), LoginActivity.class); // Replace with your login activity
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutIntent);
            requireActivity().finish();
        });

        // Initialize Views
        adminWelcomeTextView = view.findViewById(R.id.adminWelcomeTextView);
        adminTotalItemsTextView = view.findViewById(R.id.adminTotalItemsTextView);
        adminViewItemsButton = view.findViewById(R.id.adminViewItemsButton);
        adminRequestsCountTextView = view.findViewById(R.id.adminRequestsCountTextView);
        adminViewRequestsButton = view.findViewById(R.id.adminViewRequestsButton);
        totalUsersTextView = view.findViewById(R.id.adminTotalUsersTextView);
        totalCategoriesTextView = view.findViewById(R.id.adminTotalCategoriesTextView);

        // Initialize UserRepository
        userRepository = new UserRepository(getContext());

        // Retrieve the current user from the hosting activity
        if (getActivity() instanceof MainActivity) {
            currentUser = ((MainActivity) getActivity()).getCurrentUser();
        }

        if (currentUser != null) {
            // Set Welcome Text
            adminWelcomeTextView.setText("Welcome " + currentUser.getUsername() + "!");

            // Fetch and display statistics
            fetchStatistics();
        } else {
            Toast.makeText(getContext(), "Current user is null", Toast.LENGTH_SHORT).show();
        }

        // Set Button Click Listeners
        adminViewItemsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminItemsActivity.class);
            startActivity(intent);
        });

        adminViewRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminSentRequestsActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchStatistics() {
        // Fetch Total Items
        userRepository.getTotalItems(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                requireActivity().runOnUiThread(() -> adminTotalItemsTextView.setText(stats));
            }

            @Override
            public void onFailure(String errorMessage) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });

        // Fetch Requests Count
        userRepository.getSentRequestsCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer requestsCount) {
                if (requestsCount != null) {
                    adminRequestsCountTextView.setText("Requests: " + requestsCount);
                } else {
                    adminRequestsCountTextView.setText("Requests: 0");
                }
            }
        });

        // Fetch Total Users
        userRepository.getTotalUsers(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                requireActivity().runOnUiThread(() -> totalUsersTextView.setText(stats));
            }

            @Override
            public void onFailure(String errorMessage) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });

        // Fetch Total Categories
        userRepository.getTotalCategories(new UserRepository.UserStatisticsCallback() {
            @Override
            public void onSuccess(String stats) {
                requireActivity().runOnUiThread(() -> totalCategoriesTextView.setText(stats));
            }

            @Override
            public void onFailure(String errorMessage) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
