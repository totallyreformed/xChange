package com.example.xchange.Upload;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.example.xchange.Category;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.R;
import com.example.xchange.User;
import com.example.xchange.xChanger;
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

        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("USER");
        if (currentUser == null) {
            Toast.makeText(this, "User not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        xChanger xChanger=new xChanger(currentUser.getUsername(),currentUser.getEmail(),currentUser.getJoin_Date(),currentUser.getPassword(),currentUser.getLocation());
        viewModel = new ViewModelProvider(this, new UploadViewModelFactory(getApplication(), xChanger)).get(UploadViewModel.class);

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
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Item Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String filePath = getRealPathFromURI(selectedImageUri); // Μετατροπή του Uri σε πραγματική διαδρομή
            if (filePath != null) {
                try {
                    imageUri = selectedImageUri; // Αποθήκευση Uri για μελλοντική χρήση
                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(itemImageView); // Εμφάνιση εικόνας
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Unable to retrieve file path", Toast.LENGTH_SHORT).show();
            }
        }
    }
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



    private void finalizeUpload() {
        String itemName = itemNameEditText.getText().toString().trim();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        String selectedCondition = conditionSpinner.getSelectedItem().toString();
        String itemDescription = itemDescriptionEditText.getText().toString().trim();

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

        ArrayList<Image> images = new ArrayList<>();
        if (imageUri != null) {
            String filePath = getRealPathFromURI(imageUri); // Χρήση πραγματικής διαδρομής
            if (filePath != null) {
                images.add(new Image(filePath, "Uploaded item image"));
            }
        }
        viewModel.uploadItem(itemName, itemDescription, selectedCategory, selectedCondition, images, this::onUploadSuccess, this::onUploadFailure);
    }



    private void onUploadSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(UploadActivity.this, "Item uploaded successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity and return to browse
        });
    }

    private void onUploadFailure(String errorMessage) {
        runOnUiThread(() -> {
            Toast.makeText(UploadActivity.this, "Upload failed: " + errorMessage, Toast.LENGTH_SHORT).show();
        });
    }
}
