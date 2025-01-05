package com.example.xchange.CounterOffer;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
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
        viewModel = new ViewModelProvider(this).get(CounterofferViewModel.class);

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
        viewModel.getSpinnerItems().observe(this, spinnerItems -> populateSpinner(spinnerItems));

        Request request = getIntent().getParcelableExtra("REQUEST");
        ArrayList<Item> items = getIntent().getParcelableArrayListExtra("XCHANGER_ITEMS");

        // Pass request to ViewModel
        if (request != null) {
            viewModel.setRequestDetails(request);
        }

        // Pass items to ViewModel to populate the spinner
        if (items != null) {
            viewModel.populateSpinner(items);
        }
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
