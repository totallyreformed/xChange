// File: XChangerHomeFragment.java
package com.example.xchange.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.ItemsAdapter;
import com.example.xchange.R;
import com.example.xchange.User;

import java.util.ArrayList;

public class XChangerHomeFragment extends Fragment {

    private TextView usernameTextView;
    private RecyclerView itemsRecyclerView;
    private ItemsAdapter itemsAdapter;
    private User currentUser;
    private MainActivityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.xchanger_home, container, false);

        // Initialize TextView
        usernameTextView = view.findViewById(R.id.usernameTextView);

        // Initialize RecyclerView
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setNestedScrollingEnabled(true);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter
        currentUser = getCurrentUserFromActivity();
        itemsAdapter = new ItemsAdapter(new ArrayList<>(), currentUser);
        itemsAdapter.setOnItemClickListener(itemId -> {
            Intent detailIntent = new Intent(getActivity(), ItemDetailActivity.class);
            detailIntent.putExtra("ITEM_ID", itemId);
            detailIntent.putExtra("USER", currentUser);
            startActivity(detailIntent);
        });
        itemsRecyclerView.setAdapter(itemsAdapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        viewModel.getItemsList().observe(getViewLifecycleOwner(), items -> {
            if (items != null && !items.isEmpty()) {
                itemsAdapter.setItems(items);
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
        });

        // Set Username TextView
        if (currentUser != null) {
            usernameTextView.setText("Welcome " + currentUser.getUsername().toUpperCase() + " !");
        } else {
            Toast.makeText(getContext(), "Current user is null", Toast.LENGTH_SHORT).show();
        }

        return view;

    }

    private User getCurrentUserFromActivity() {
        if (getActivity() instanceof MainActivity) {
            return ((MainActivity) getActivity()).getCurrentUser();
        }
        return null;
    }
}