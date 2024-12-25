package com.example.xchange.Upload;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
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
    private static final int MANAGE_ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE = 2296;

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
            // Check for MANAGE_EXTERNAL_STORAGE permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11 and above
                if (!Environment.isExternalStorageManager()) {
                    requestManageExternalStoragePermission();
                } else {
                    openImagePicker();
                }
            } else {
                // For Android versions below 11, handle accordingly if needed
                Toast.makeText(this, "Storage access is not supported on your device.", Toast.LENGTH_SHORT).show();
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
     * Requests the MANAGE_EXTERNAL_STORAGE permission by directing the user to system settings.
     */
    private void requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11 and above
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MANAGE_ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, MANAGE_ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE);
            }
        }
    }

    /**
     * Opens the image picker intent.
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Item Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // This block can be used for handling older permissions if needed
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle MANAGE_EXTERNAL_STORAGE permission result
        if (requestCode == MANAGE_ALL_FILES_ACCESS_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // Android 11 and above
                if (Environment.isExternalStorageManager()) {
                    // Permission granted, proceed with image upload
                    openImagePicker();
                } else {
                    // Permission denied, inform the user
                    Snackbar.make(findViewById(R.id.uploadConstraintLayout),
                            "Permission denied to manage all files", Snackbar.LENGTH_LONG).show();
                }
            }
        }

        // Handle Image Picker Result
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Display selected image in ImageView without using Glide
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

        // Image upload is optional; no need to validate imageUri

        // Create Item object
        Item newItem = new Item(
                currentUser.getUsername(),
                itemName,
                itemDescription,
                selectedCategory,
                selectedCondition,
                null // Image URI will be handled separately
        );

        // Initialize image list
        newItem.setItemImages(new ArrayList<>());

        // If imageUri is available, add it to the item's image list
        if (imageUri != null) {
            newItem.addItemImage(new Image(imageUri.toString(), "Item Image"));
        }

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
