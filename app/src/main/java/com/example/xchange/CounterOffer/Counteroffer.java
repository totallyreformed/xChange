package com.example.xchange.CounterOffer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.R;
import com.example.xchange.Item;
import com.example.xchange.Request;

import java.util.ArrayList;
import java.util.List;

public class Counteroffer extends AppCompatActivity {

    TextView Requester, Requestee, RequestedItem;
    Spinner OfferedItemSpinner;

    private CounterofferViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_offer);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new CounterofferViewModel(Counteroffer.this);
            }
        }).get(CounterofferViewModel.class);

        // Retrieve UI components
        Requester = findViewById(R.id.requesterTextView);
        Requestee = findViewById(R.id.requesteeTextView);
        RequestedItem = findViewById(R.id.requestedItemTextView);
        OfferedItemSpinner = findViewById(R.id.offeredItemSpinner);

        // Observe LiveData from ViewModel
        viewModel.getRequesterText().observe(this, text -> Requester.setText(text));
        viewModel.getRequesteeText().observe(this, text -> Requestee.setText(text));
        viewModel.getRequestedItemText().observe(this, text -> RequestedItem.setText(text));

        // Observe spinner items from ViewModel
        viewModel.getSpinnerItems().observe(this, this::populateSpinner);

        Request request = getIntent().getParcelableExtra("REQUEST");
        assert request != null;
        String user=request.getRequestee().getUsername();
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra("XCHANGER_ITEMS");

        viewModel.setRequestDetails(request);

        if (items != null) {
            viewModel.populateSpinner(items);
        }

        Button initializeCounterofferButton = findViewById(R.id.initializeCounterofferButton);
        initializeCounterofferButton.setOnClickListener(view -> {
            Item selectedItem = (Item) OfferedItemSpinner.getSelectedItem();
            if (selectedItem != null) {
                viewModel.findRequest(request.getRequestedItem().getItemId(), user, (found, foundRequest) -> {
                    runOnUiThread(() -> { // Ensure this block runs on the main thread
                        if (found) {
                            Log.d("Counteroffer", "Request found: " + foundRequest);
                            // Handle found request and create counteroffer
                        } else {
                            Toast.makeText(this, "No matching request found!", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            } else {
                Toast.makeText(this, "Please select an item to counteroffer.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Helper method to populate the spinner
    private void populateSpinner(List<Item> items) {
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OfferedItemSpinner.setAdapter(adapter);

        // Notify ViewModel of the selected item
        OfferedItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                Item selectedItem = (Item) OfferedItemSpinner.getSelectedItem();
                viewModel.handleItemSelection(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                viewModel.handleNoSelection();
            }
        });
    }
}
