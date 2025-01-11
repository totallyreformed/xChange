package com.example.xchange.CounterOffer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.xChanger;

import java.util.ArrayList;
import java.util.List;

public class CounterofferActivity extends AppCompatActivity {

    private TextView requesterTextView, requesteeTextView, requestedItemTextView;
    private Spinner offeredItemSpinner;
    private CounterofferViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_offer);

        initializeUI();
        initializeViewModel();
        handleIntentData();
        observeViewModelData();
        setupCounterofferButton();
    }

    private void initializeUI() {
        requesterTextView = findViewById(R.id.requesterTextView);
        requesteeTextView = findViewById(R.id.requesteeTextView);
        requestedItemTextView = findViewById(R.id.requestedItemTextView);
        offeredItemSpinner = findViewById(R.id.offeredItemSpinner);
    }

    private void initializeViewModel() {
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CounterofferViewModel(CounterofferActivity.this);
            }
        }).get(CounterofferViewModel.class);
    }

    private void handleIntentData() {
        Request request = getIntent().getParcelableExtra("REQUEST");
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra("XCHANGER_ITEMS");

        if (request != null) {
            viewModel.setRequestDetails(request);
        }

        if (items != null) {
            viewModel.populateSpinner(items);
        }
    }

    private void observeViewModelData() {
        viewModel.getRequesterText().observe(this, text -> requesterTextView.setText(text));
        viewModel.getRequesteeText().observe(this, text -> requesteeTextView.setText(text));
        viewModel.getRequestedItemText().observe(this, text -> requestedItemTextView.setText(text));

        viewModel.getSpinnerItems().observe(this, this::populateSpinner);

        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner(List<Item> items) {
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeredItemSpinner.setAdapter(adapter);

        offeredItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Item selectedItem = (Item) offeredItemSpinner.getSelectedItem();
                viewModel.handleItemSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.handleNoSelection();
            }
        });
    }

    private void setupCounterofferButton() {
        Button initializeCounterofferButton = findViewById(R.id.initializeCounterofferButton);
        initializeCounterofferButton.setOnClickListener(view -> initializeCounteroffer());
    }

    private void initializeCounteroffer() {
        Item selectedItem = (Item) offeredItemSpinner.getSelectedItem();

        if (selectedItem == null) {
            Toast.makeText(this, "Please select an item to counteroffer.", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = getIntent().getParcelableExtra("REQUEST");
        User user = request != null ? request.getRequestee() : null;

        if (user == null || request == null) {
            Toast.makeText(this, "Invalid request or user data.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.findRequest(request.getRequestedItem().getItemId(), user.getUsername(), (found, foundRequest) -> {
            runOnUiThread(() -> {
                if (found && foundRequest != null) {
                    xChanger xchanger = new xChanger(user.getUsername(), user.getEmail(), user.getJoin_Date(), user.getPassword(), user.getLocation());
                    viewModel.initializeCounterRequest(foundRequest, selectedItem, xchanger);
                    Toast.makeText(this, "Counter offer initialized", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity(user, selectedItem);
                } else {
                    Toast.makeText(this, "No matching request found!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void navigateToMainActivity(User user, Item selectedItem) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USER", user);
        intent.putExtra("REQUESTED_ITEM_ID", selectedItem.getItemId());
        startActivity(intent);
    }
}
