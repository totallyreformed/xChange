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
import com.example.xchange.EditItemViewModelFactory;
import com.example.xchange.Image;
import com.example.xchange.Item;
import com.example.xchange.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditItemViewModel viewModel;
    private EditText itemNameEditText, itemDescriptionEditText;
    private Spinner categorySpinner, conditionSpinner;
    private ImageView itemPhotoImageView;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        initializeUIComponents();
        long itemId = getIntentItemId();
        if (itemId == -1) {
            showErrorAndExit("Invalid Item ID");
            return;
        }
        initializeViewModel(itemId);
        populateSpinners();
        observeItemData();
        setupUploadPhotoButton();
        setupSaveButton();
        setupBackButton();
    }

    private void initializeUIComponents() {
        itemNameEditText = findViewById(R.id.editItemNameEditText);
        itemDescriptionEditText = findViewById(R.id.editItemDescriptionEditText);
        categorySpinner = findViewById(R.id.editItemCategorySpinner);
        conditionSpinner = findViewById(R.id.editItemConditionSpinner);
        itemPhotoImageView = findViewById(R.id.editItemPhotoImageView);
    }

    private long getIntentItemId() {
        return getIntent().getLongExtra("ITEM_ID", -1);
    }

    private void showErrorAndExit(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initializeViewModel(long itemId) {
        viewModel = new ViewModelProvider(this, new EditItemViewModelFactory(getApplication(), itemId)).get(EditItemViewModel.class);
    }

    private void populateSpinners() {
        populateSpinner(categorySpinner, getCategories());
        populateSpinner(conditionSpinner, getConditions());
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private List<String> getCategories() {
        return Arrays.asList("Technology", "Books", "Home Appliances", "Sports", "Fashion", "Toys");
    }

    private List<String> getConditions() {
        return Arrays.asList("New", "Like New", "Used", "Refurbished");
    }

    private void observeItemData() {
        viewModel.getItem().observe(this, item -> {
            if (item != null) {
                populateItemDetails(item);
            }
        });
    }

    private void populateItemDetails(Item item) {
        itemNameEditText.setText(item.getItemName());
        itemDescriptionEditText.setText(item.getItemDescription());
        categorySpinner.setSelection(getCategories().indexOf(item.getItemCategory().getDisplayName()));
        conditionSpinner.setSelection(getConditions().indexOf(item.getItemCondition()));

        if (item.getFirstImage() != null && item.getFirstImage().getFilePath() != null) {
            selectedImagePath = item.getFirstImage().getFilePath();
            loadImage(selectedImagePath, itemPhotoImageView);
        }
    }

    private void setupUploadPhotoButton() {
        Button uploadPhotoButton = findViewById(R.id.uploadPhotoButton);
        uploadPhotoButton.setOnClickListener(v -> pickImageFromGallery());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURI(selectedImageUri);
            loadImage(selectedImageUri.toString(), itemPhotoImageView);
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

    private void loadImage(String filePath, ImageView imageView) {
        Glide.with(this)
                .load(filePath)
                .placeholder(R.drawable.image_placeholder)
                .into(imageView);
    }

    private void setupSaveButton() {
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveItemDetails());
    }

    private void saveItemDetails() {
        String updatedName = itemNameEditText.getText().toString();
        String updatedDescription = itemDescriptionEditText.getText().toString();
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        String selectedCondition = conditionSpinner.getSelectedItem().toString();

        ArrayList<Image> updatedImages = new ArrayList<>();
        if (selectedImagePath != null) {
            updatedImages.add(new Image(selectedImagePath, "Updated item image"));
        }

        viewModel.updateItem(updatedName, updatedDescription, selectedCondition, selectedCategory, updatedImages);
        Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupBackButton() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}
