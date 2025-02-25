package com.example.xchange.EditItem;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.EditItem.EditItemViewModelFactory;
import com.example.xchange.Image;
import com.example.xchange.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Activity for editing the details of an item in the xChange app.
 * Allows users to update item information such as name, description, category, condition, and image.
 */
public class EditItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditItemViewModel viewModel;
    private EditText itemNameEditText, itemDescriptionEditText;
    private Spinner categorySpinner, conditionSpinner;
    private ImageView itemPhotoImageView;
    private String selectedImagePath; // Path of the selected image

    /**
     * Called when the activity is created.
     * Initializes the UI components and ViewModel, and loads item data.
     *
     * @param savedInstanceState If the activity is being re-initialized after being shut down, this Bundle contains the saved data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Initialize UI elements
        itemNameEditText = findViewById(R.id.editItemNameEditText);
        itemDescriptionEditText = findViewById(R.id.editItemDescriptionEditText);
        categorySpinner = findViewById(R.id.editItemCategorySpinner);
        conditionSpinner = findViewById(R.id.editItemConditionSpinner);
        itemPhotoImageView = findViewById(R.id.editItemPhotoImageView);
        Button uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        Button saveButton = findViewById(R.id.saveButton);

        // Get item ID from Intent
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new EditItemViewModelFactory(getApplication(), itemId)).get(EditItemViewModel.class);

        // Load categories into Spinner
        List<String> categories = getCategories();
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Load conditions into Spinner
        List<String> conditions = getConditions();
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        // Observe item data
        viewModel.getItem().observe(this, item -> {
            if (item != null) {
                itemNameEditText.setText(item.getItemName());
                itemDescriptionEditText.setText(item.getItemDescription());
                categorySpinner.setSelection(categories.indexOf(item.getItemCategory().getDisplayName()));
                conditionSpinner.setSelection(conditions.indexOf(item.getItemCondition()));

                if (item.getFirstImage() != null && item.getFirstImage().getFilePath() != null) {
                    selectedImagePath = item.getFirstImage().getFilePath(); // Set current image path
                    Glide.with(this)
                            .load(selectedImagePath)
                            .placeholder(R.drawable.image_placeholder)
                            .into(itemPhotoImageView);
                }
            }
        });

        // Logic for photo upload button
        uploadPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Logic for "Save" button
        saveButton.setOnClickListener(v -> {
            String updatedName = itemNameEditText.getText().toString();
            String updatedDescription = itemDescriptionEditText.getText().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String selectedCondition = conditionSpinner.getSelectedItem().toString();

            // Handle updated images
            ArrayList<Image> updatedImages = new ArrayList<>();
            if (selectedImagePath != null) {
                updatedImages.add(new Image(selectedImagePath, "Updated item image"));
            }

            viewModel.updateItem(updatedName, updatedDescription, selectedCondition, selectedCategory, updatedImages);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    /**
     * Handles the result of the image picker and updates the selected image path.
     *
     * @param requestCode The request code of the activity result.
     * @param resultCode  The result code of the activity result.
     * @param data        The intent data containing the selected image URI.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURI(selectedImageUri); // Convert URI to file path
            Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.image_placeholder)
                    .into(itemPhotoImageView); // Display the selected image
        }
    }

    /**
     * Converts a URI to a real file path.
     *
     * @param uri The URI to convert.
     * @return The file path corresponding to the URI.
     */
    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    /**
     * Retrieves the list of categories for the category spinner.
     *
     * @return A list of category names.
     */
    private List<String> getCategories() {
        return Arrays.asList("Technology", "Books", "Home Appliances", "Sports", "Fashion","Toys");
    }

    /**
     * Retrieves the list of conditions for the condition spinner.
     *
     * @return A list of condition names.
     */
    private List<String> getConditions() {
        return Arrays.asList("New", "Like New", "Used", "Refurbished");
    }
}
