// File: ItemDetailActivity.java
package com.example.xchange.ItemDetail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.User;

import java.io.IOException;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailViewModel viewModel;
    private TextView itemNameTextView, itemDescriptionTextView, itemCategoryTextView, itemConditionTextView, itemXchangerTextView;
    private ImageView itemImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Button backButton = findViewById(R.id.backToMainButton);
        Button deleteButton = findViewById(R.id.deleteItemButton); // Νέο κουμπί διαγραφής

        backButton.setOnClickListener(v -> finish());

        // Initialize Views
        itemNameTextView = findViewById(R.id.detailItemNameTextView);
        itemDescriptionTextView = findViewById(R.id.detailItemDescriptionTextView);
        itemCategoryTextView = findViewById(R.id.detailItemCategoryTextView);
        itemConditionTextView = findViewById(R.id.detailItemConditionTextView);
        itemXchangerTextView = findViewById(R.id.detailItemXchangerTextView);
        itemImageView = findViewById(R.id.detailItemImageView);

        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new ItemDetailViewModelFactory(getApplication())).get(ItemDetailViewModel.class);

        // Observe LiveData for the Item
        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                displayItemDetails(item);
            } else {
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Ορισμός της λειτουργίας του κουμπιού διαγραφής
        deleteButton.setOnClickListener(v -> {
            viewModel.deleteItemById(itemId); // Υλοποίησε αυτήν τη μέθοδο στο ViewModel
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            finish(); // Επιστροφή στην προηγούμενη δραστηριότητα
        });
    }
    private void displayItemDetails(Item item) {
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("USER");
        assert user != null;
        Button deleteButton = findViewById(R.id.deleteItemButton);
        if (user.getUsername().equals(item.getXchanger())) {
            deleteButton.setVisibility(View.VISIBLE); // Εμφάνιση του κουμπιού διαγραφής
        }

        // Display the first image if available
        if (item.getItemImages() != null && !item.getItemImages().isEmpty()) {
            Uri imageUri = Uri.parse(item.getItemImages().get(0).getFilePath());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                itemImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                itemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            // If no image is available, display a placeholder
            itemImageView.setImageResource(R.drawable.image_placeholder);
        }
    }
}
