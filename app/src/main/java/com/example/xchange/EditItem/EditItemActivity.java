package com.example.xchange.EditItem;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.EditItemViewModelFactory;
import com.example.xchange.R;

import java.util.Arrays;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private EditItemViewModel viewModel;
    private EditText itemNameEditText, itemDescriptionEditText;
    private Spinner categorySpinner, conditionSpinner;
    private ImageView itemPhotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Αρχικοποίηση UI στοιχείων
        itemNameEditText = findViewById(R.id.editItemNameEditText);
        itemDescriptionEditText = findViewById(R.id.editItemDescriptionEditText);
        categorySpinner = findViewById(R.id.editItemCategorySpinner);
        conditionSpinner = findViewById(R.id.editItemConditionSpinner);
        itemPhotoImageView = findViewById(R.id.editItemPhotoImageView);
        Button uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        Button saveButton = findViewById(R.id.saveButton);

        // Ανάκτηση ID αντικειμένου από το Intent
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Αρχικοποίηση ViewModel
        viewModel = new ViewModelProvider(this, new EditItemViewModelFactory(getApplication(), itemId)).get(EditItemViewModel.class);

        // Φόρτωση κατηγοριών στο Spinner
        List<String> categories = getCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Φόρτωση καταστάσεων στο Condition Spinner
        List<String> conditions = getConditions();
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        // Παρατήρηση δεδομένων αντικειμένου
        viewModel.getItem().observe(this, item -> {
            if (item != null) {
                itemNameEditText.setText(item.getItemName());
                itemDescriptionEditText.setText(item.getItemDescription());
                categorySpinner.setSelection(categories.indexOf(item.getItemCategory().getDisplayName()));
                conditionSpinner.setSelection(conditions.indexOf(item.getItemCondition()));

            }
        });

        uploadPhotoButton.setOnClickListener(v -> {
            // Προσθέστε λογική για μεταφόρτωση/επιλογή φωτογραφίας
        });

        // Λογική κουμπιού "Save"
        saveButton.setOnClickListener(v -> {
            String updatedName = itemNameEditText.getText().toString();
            String updatedDescription = itemDescriptionEditText.getText().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String selectedCondition = conditionSpinner.getSelectedItem().toString();

            viewModel.updateItem(updatedName, updatedDescription, selectedCondition, selectedCategory);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    // Μέθοδος για τις κατηγορίες
    private List<String> getCategories() {
        // Αντικαταστήστε με τις πραγματικές κατηγορίες
        return Arrays.asList("Electronics", "Clothing", "Books", "Home Appliances", "Others");
    }

    // Μέθοδος για τις καταστάσεις
    private List<String> getConditions() {
        // Αντικαταστήστε με τις πραγματικές καταστάσεις
        return Arrays.asList("New", "Like New", "Used", "Refurbished");
    }
}
