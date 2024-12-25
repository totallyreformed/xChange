// File: ItemDetailActivity.java
package com.example.xchange.ItemDetail;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.xchange.Item;
import com.example.xchange.R;

import java.io.IOException;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailViewModel viewModel;
    private TextView itemNameTextView, itemDescriptionTextView, itemCategoryTextView, itemConditionTextView, itemXchangerTextView;
    private ImageView itemImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail); // Ensure this layout file exists

        // Initialize Views
        itemNameTextView = findViewById(R.id.detailItemNameTextView);
        itemDescriptionTextView = findViewById(R.id.detailItemDescriptionTextView);
        itemCategoryTextView = findViewById(R.id.detailItemCategoryTextView);
        itemConditionTextView = findViewById(R.id.detailItemConditionTextView);
        itemXchangerTextView = findViewById(R.id.detailItemXchangerTextView);
        itemImageView = findViewById(R.id.detailItemImageView);

        // Retrieve itemId from Intent
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
    }

    /**
     * Populates the UI with the item's details.
     *
     * @param item The Item object fetched from the database.
     */
    private void displayItemDetails(Item item) {
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        // Display the first image if available
        if (item.getItemImages() != null && !item.getItemImages().isEmpty()) {
            Uri imageUri = Uri.parse(item.getItemImages().get(0).getFilePath());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                itemImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                itemImageView.setImageResource(R.drawable.image_placeholder); // Ensure this drawable exists
            }
        } else {
            // If no image is available, display a placeholder
            itemImageView.setImageResource(R.drawable.image_placeholder); // Ensure this drawable exists
        }
    }
}
