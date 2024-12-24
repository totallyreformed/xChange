package com.example.xchange.Upload;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.User;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private EditText itemNameEditText;
    private Spinner categorySpinner;
    private Spinner conditionSpinner;
    private EditText itemDescriptionEditText;
    private ImageView itemImageView;
    private Button finalizeUploadButton;
    private Button cancelUploadButton;

    private Uri imageUri;

    private UploadViewModel viewModel;

    private User currentUser; // Assuming you pass the current user via Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Initialize Views
        itemNameEditText = findViewById(R.id.uploadItemNameEditText);
        categorySpinner = findViewById(R.id.uploadCategorySpinner);
        conditionSpinner = findViewById(R.id.uploadConditionSpinner);
        itemDescriptionEditText = findViewById(R.id.uploadItemDescriptionEditText);
        itemImageView = findViewById(R.id.uploadItemImageView);
        finalizeUploadButton = findViewById(R.id.finalizeUploadButton);
        cancelUploadButton = findViewById(R.id.cancelUploadButton);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new UploadViewModelFactory(getApplication())).get(UploadViewModel.class);

        // Retrieve User from Intent
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        if (currentUser == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up Category Spinner
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Category.values());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set up Condition Spinner (Assuming predefined conditions)
        String[] conditions = {"New", "Like New", "Used", "Refurbished"};
        ArrayAdapter<String> conditionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, conditions);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(conditionAdapter);

        // Set up ImageView Click Listener to select image
        itemImageView.setOnClickListener(v -> {
            // Check for storage permission
            if (ContextCompat.checkSelfPermission(UploadActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                openImagePicker();
            }
        });

        // Finalize Upload Button Click Listener
        finalizeUploadButton.setOnClickListener(v -> {
            finalizeUpload();
        });

        // Cancel Upload Button Click Listener
        cancelUploadButton.setOnClickListener(v -> {
            new AlertDialog.Builder(UploadActivity.this)
                    .setTitle("Cancel Upload")
                    .setMessage("Are you sure you want to cancel the upload?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    /**
     * Opens the image picker intent.
     */
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Item Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Snackbar.make(findViewById(R.id.uploadConstraintLayout),
                        "Permission denied to access storage", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle Image Picker Result
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Display selected image in ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                itemImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates input fields and uploads the item.
     */
    private void finalizeUpload() {
        String itemName = itemNameEditText.getText().toString().trim();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        String selectedCondition = conditionSpinner.getSelectedItem().toString();
        String itemDescription = itemDescriptionEditText.getText().toString().trim();

        // Validate required fields
        if (itemName.isEmpty()) {
            itemNameEditText.setError("Item name is required");
            itemNameEditText.requestFocus();
            return;
        }

        if (selectedCategory == null || selectedCategory == Category.ALL) {
            Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedCondition.isEmpty()) {
            Toast.makeText(this, "Please select item condition", Toast.LENGTH_SHORT).show();
            return;
        }

        if (itemDescription.isEmpty()) {
            itemDescriptionEditText.setError("Item description is required");
            itemDescriptionEditText.requestFocus();
            return;
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please upload an image of the item", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Item object
        Item newItem = new Item(
                currentUser.getUsername(),
                itemName,
                itemDescription,
                selectedCategory,
                selectedCondition,
                null // We'll handle image uploading separately
        );

        // Optionally, handle image uploading here (e.g., upload to server or save locally)
        // For simplicity, we'll store the image URI as a string in the Item
        newItem.setItemImages(new ArrayList<>()); // Initialize list
        newItem.addItemImage(new Image(imageUri.toString(), "Item Image"));

        // Upload Item via ViewModel
        viewModel.uploadItem(newItem, this::onUploadSuccess, this::onUploadFailure);
    }

    /**
     * Callback for successful upload.
     */
    private void onUploadSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(UploadActivity.this, "Item uploaded successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to browse
        });
    }

    /**
     * Callback for failed upload.
     *
     * @param errorMessage The error message to display.
     */
    private void onUploadFailure(String errorMessage) {
        runOnUiThread(() -> {
            Toast.makeText(UploadActivity.this, "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
        });
    }
}
