package com.example.xchange.ItemDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.xchange.AcceptRequest.AcceptRequestActivity;
import com.example.xchange.CounterOffer.CounterofferActivity;
import com.example.xchange.EditItem.EditItemActivity;
import com.example.xchange.Item;
import com.example.xchange.MainActivity.MainActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;
import com.example.xchange.database.UserRepository;
import com.example.xchange.request.RequestActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailActivity extends AppCompatActivity {

    private ItemDetailViewModel viewModel;
    private TextView itemNameTextView, itemDescriptionTextView, itemCategoryTextView, itemConditionTextView, itemXchangerTextView;
    private ImageView itemImageView;
    private Request requestToSend; // Request to pass to the new activity

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        // Initialize Views
        itemNameTextView = findViewById(R.id.detailItemNameTextView);
        itemDescriptionTextView = findViewById(R.id.detailItemDescriptionTextView);
        itemCategoryTextView = findViewById(R.id.detailItemCategoryTextView);
        itemConditionTextView = findViewById(R.id.detailItemConditionTextView);
        itemXchangerTextView = findViewById(R.id.detailItemXchangerTextView);
        itemImageView = findViewById(R.id.detailItemImageView);

        Button backButton = findViewById(R.id.backToMainButton);
        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);
        Button seeRequestCounterofferButton = findViewById(R.id.seeRequestCounterofferButton);
        Button cancelbutton=findViewById(R.id.cancelRequestButton);

        // Handle "Back to Main" Button
        backButton.setOnClickListener(v -> finish());

        // Get item ID and user data from Intent
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        User user = getIntent().getParcelableExtra("USER");
        if (user == null) {
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this, new ItemDetailViewModelFactory(getApplication())).get(ItemDetailViewModel.class);
        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                displayItemDetails(item, user);
            } else {
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Delete button functionality
        deleteButton.setOnClickListener(v -> {
            viewModel.deleteItemById(itemId);
            Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
        cancelbutton.setOnClickListener(v -> {
            viewModel.cancelRequest(itemId,user.getUsername());
            Toast.makeText(this, "Request Canceled", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Edit button functionality
        editButton.setOnClickListener(v -> {
            Intent editIntent = new Intent(this, EditItemActivity.class);
            editIntent.putExtra("ITEM_ID", itemId);
            startActivity(editIntent);
        });



        viewModel.checkToDisplayAcceptReject(itemId, user.getUsername(), (success, request) -> {
            runOnUiThread(() -> {
                Button acceptButton = findViewById(R.id.acceptButton);
                Button rejectButton = findViewById(R.id.rejectButton);
                Button counterofferButton = findViewById(R.id.counterofferButton);
                Button seeExtraButton = findViewById(R.id.seeRequestCounterofferButton);
                Button editButton1 = findViewById(R.id.editItemButton);
                TextView requestStatusTextView = findViewById(R.id.requestStatusTextView);
                Button requestItemButton = findViewById(R.id.requestItemButton);
                Button cancelRequestButton = findViewById(R.id.cancelRequestButton);

                if (success && request != null && request.isActive()) {
                    requestToSend = request;
                    acceptButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    counterofferButton.setVisibility(View.VISIBLE);
                    seeExtraButton.setVisibility(View.VISIBLE);
                    editButton1.setVisibility(View.GONE);
                    requestStatusTextView.setVisibility(View.GONE);
                    requestItemButton.setVisibility(View.GONE);
                    cancelRequestButton.setVisibility(View.GONE);

                    // Set Accept Button Click Listener
                    acceptButton.setOnClickListener(v -> {
                        Intent intent = new Intent(ItemDetailActivity.this, AcceptRequestActivity.class);
                        intent.putExtra("REQUEST", requestToSend);
                        intent.putExtra("USER", user);
                        startActivity(intent);
                    });

                    viewModel.checkIfRequesteeWithCounteroffer(itemId, user.getUsername(), counteroffer  -> {
                        runOnUiThread(() -> {
                            if (counteroffer!=null) {
                                TextView otherInfo=findViewById(R.id.requestStatusTextView);
                                otherInfo.setVisibility(View.VISIBLE);
                                otherInfo.setText("You counter-offered, see it below ");
                                seeExtraButton.setText("See Counteroffer");
                                seeExtraButton.setVisibility(View.VISIBLE);
                                acceptButton.setVisibility(View.GONE);
                                rejectButton.setVisibility(View.GONE);
                                counterofferButton.setVisibility(View.GONE);

                                // On button click, send the counteroffer
                                seeExtraButton.setOnClickListener(view -> {
                                    Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                                    intent.putExtra("COUNTEROFFER", counteroffer);
                                    intent.putExtra("HAS_COUNTEROFFER", true);// Pass extra data for counteroffer
                                    startActivity(intent);
                                });
                            } else {
                                seeExtraButton.setText("See Request");
                                seeExtraButton.setVisibility(View.VISIBLE);
                                seeExtraButton.setOnClickListener(view -> {
                                    Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                                    intent.putExtra("REQUEST", requestToSend);
                                    intent.putExtra("HAS_COUNTEROFFER", false); // No counteroffer
                                    startActivity(intent);
                                });
                            }
                        });
                    });
                } else {
                    // Request is inactive; hide action buttons
                    acceptButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    counterofferButton.setVisibility(View.GONE);
                    seeExtraButton.setVisibility(View.GONE); // Hide "See Request" button
                    editButton1.setVisibility(View.GONE);
                    requestStatusTextView.setVisibility(View.GONE);
                    requestItemButton.setVisibility(View.GONE);
                    cancelRequestButton.setVisibility(View.GONE);
                }

                viewModel.checkRequestToDisplay(itemId, user.getUsername(), result -> {
                    runOnUiThread(() -> {
                        viewModel.getItemById(itemId).observe(this, item -> {
                            if (item == null) {
                                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Ensures MainActivity is launched correctly
                                startActivity(intent);
                                finish(); // Close the current activity
                                return;
                            }
                            if (user.getUsername().trim().equals(item.getXchanger())) {
                                requestStatusTextView.setVisibility(View.GONE);
                                requestItemButton.setVisibility(View.GONE);
                                cancelRequestButton.setVisibility(View.GONE);
                            } else if (result) {
                                // Item already requested
                                requestStatusTextView.setVisibility(View.VISIBLE);
                                requestItemButton.setVisibility(View.GONE);
                                cancelRequestButton.setVisibility(View.VISIBLE);
                            } else {
                                // Item not requested
                                requestStatusTextView.setVisibility(View.GONE);
                                requestItemButton.setVisibility(View.VISIBLE);
                                cancelRequestButton.setVisibility(View.GONE);
                            }
                        });
                    });
                });
            });

            viewModel.checkIfRequesterWithCounterofferee(itemId,user.getUsername(), counteroffer  -> {
                runOnUiThread(() -> {
                    Button seeExtraButton=findViewById(R.id.seeRequestCounterofferButton);
                    Button acceptButton=findViewById(R.id.acceptButton);
                    Button rejectButton=findViewById(R.id.rejectButton);
                    TextView iteminfo=findViewById(R.id.requestStatusTextView);
                    if (counteroffer!=null) {
                        seeExtraButton.setText("See Counteroffer");
                        seeExtraButton.setVisibility(View.VISIBLE);
                        acceptButton.setVisibility(View.VISIBLE);
                        rejectButton.setVisibility(View.VISIBLE);
                        iteminfo.setText("Other xChanger came back with a counteroffer");
                        seeExtraButton.setOnClickListener(view -> {
                            Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                            intent.putExtra("COUNTEROFFER", counteroffer);
                            intent.putExtra("HAS_COUNTEROFFER", true);
                            startActivity(intent);
                        });
                    }
                });
            });
        });

        // Handle Counteroffer Button
        Button counterButton = findViewById(R.id.counterofferButton);
        counterButton.setOnClickListener(v -> {
            viewModel.findRequest(itemId, user.getUsername(), (success, request) -> {
                if (success && request != null) {
                    Intent intent = new Intent(this, CounterofferActivity.class);
                    intent.putExtra("REQUEST", request);

                    viewModel.findItemsByXChanger(request.getRequester().getUsername(), new UserRepository.UserItemsCallback() {
                        @Override
                        public void onSuccess(List<Item> items) {
                            if (items != null && !items.isEmpty()) {
                                intent.putParcelableArrayListExtra("XCHANGER_ITEMS", new ArrayList<>(items));
                            }
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(String message) {
                            startActivity(intent);
                        }
                    });
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get item ID and user data from Intent
        long itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid Item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        User user = getIntent().getParcelableExtra("USER");
        if (user == null) {
            Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Re-check the request status to update UI accordingly
        viewModel.checkToDisplayAcceptReject(itemId, user.getUsername(), (success, request) -> {
            runOnUiThread(() -> {
                Button acceptButton = findViewById(R.id.acceptButton);
                Button rejectButton = findViewById(R.id.rejectButton);
                Button counterofferButton = findViewById(R.id.counterofferButton);
                Button seeExtraButton = findViewById(R.id.seeRequestCounterofferButton);
                Button editButton1 = findViewById(R.id.editItemButton);
                TextView requestStatusTextView = findViewById(R.id.requestStatusTextView);
                Button requestItemButton = findViewById(R.id.requestItemButton);
                Button cancelRequestButton = findViewById(R.id.cancelRequestButton);

                if (success && request != null && request.isActive()) {
                    // Request is active; show action buttons
                    requestToSend = request;
                    acceptButton.setVisibility(View.VISIBLE);
                    rejectButton.setVisibility(View.VISIBLE);
                    counterofferButton.setVisibility(View.VISIBLE);
                    editButton1.setVisibility(View.GONE);
                    seeExtraButton.setVisibility(View.VISIBLE);
                    requestStatusTextView.setVisibility(View.GONE);
                    requestItemButton.setVisibility(View.GONE);
                    cancelRequestButton.setVisibility(View.GONE);

                    // Set Accept Button Click Listener
                    acceptButton.setOnClickListener(v -> {
                        Intent intent = new Intent(ItemDetailActivity.this, AcceptRequestActivity.class);
                        intent.putExtra("REQUEST", requestToSend);
                        intent.putExtra("USER", user);
                        startActivity(intent);
                    });

                    // Existing seeExtraButton logic...
                    viewModel.checkIfRequesteeWithCounteroffer(itemId, user.getUsername(), counteroffer -> {
                        runOnUiThread(() -> {
                            if (counteroffer != null) {
                                seeExtraButton.setText("See Counteroffer");
                                seeExtraButton.setVisibility(View.VISIBLE);

                                // On button click, navigate to CounterofferActivity
                                seeExtraButton.setOnClickListener(view -> {
                                    Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                                    intent.putExtra("COUNTEROFFER", counteroffer);
                                    intent.putExtra("HAS_COUNTEROFFER", true); // Indicates presence of counteroffer
                                    startActivity(intent);
                                });
                            } else {
                                seeExtraButton.setText("See Request");
                                seeExtraButton.setVisibility(View.VISIBLE);
                                seeExtraButton.setOnClickListener(view -> {
                                    Intent intent = new Intent(this, SeerequestsCounteroffersActivity.class);
                                    intent.putExtra("REQUEST", requestToSend);
                                    intent.putExtra("HAS_COUNTEROFFER", false); // No counteroffer
                                    startActivity(intent);
                                });
                            }
                        });
                    });
                } else {
                    // Request is inactive; hide action buttons
                    acceptButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    counterofferButton.setVisibility(View.GONE);
                    editButton1.setVisibility(View.GONE);
                    seeExtraButton.setVisibility(View.GONE); // Optionally hide if not needed
                    requestStatusTextView.setVisibility(View.GONE);
                    requestItemButton.setVisibility(View.GONE);
                    cancelRequestButton.setVisibility(View.GONE);
                }
            });
        });

        // Optionally, re-fetch the item details to ensure UI consistency
        viewModel.getItemById(itemId).observe(this, item -> {
            if (item != null) {
                displayItemDetails(item, user);
            } else {
                Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayItemDetails(Item item, User user) {
        // Display item details
        itemNameTextView.setText(item.getItemName());
        itemDescriptionTextView.setText(item.getItemDescription());
        itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        itemConditionTextView.setText("Condition: " + item.getItemCondition());
        itemXchangerTextView.setText("Posted by: " + item.getXchanger());

        // Display item image using Glide
        if (item.getFirstImage() != null) {
            String filePath = item.getFirstImage().getFilePath();
            if (filePath != null) {
                try {
                    int resourceId = Integer.parseInt(filePath);
                    Glide.with(this)
                            .load(resourceId)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(itemImageView);
                } catch (NumberFormatException e) {
                    Glide.with(this)
                            .load(filePath)
                            .placeholder(R.drawable.image_placeholder)
                            .error(R.drawable.image_placeholder)
                            .into(itemImageView);
                }
            } else {
                itemImageView.setImageResource(R.drawable.image_placeholder);
            }
        } else {
            itemImageView.setImageResource(R.drawable.image_placeholder);
        }

        // Show/hide buttons based on user
        Button deleteButton = findViewById(R.id.deleteItemButton);
        Button editButton = findViewById(R.id.editItemButton);
        Button requestButton = findViewById(R.id.requestItemButton);

        if (user.getUsername().trim().equals(item.getXchanger().trim())) {
            deleteButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            requestButton.setVisibility(View.GONE);
        } else {
            deleteButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            requestButton.setVisibility(View.VISIBLE);
        }

        // Handle Request Button
        requestButton.setOnClickListener(v -> {
            LiveData<User> ownerLiveData = viewModel.getUserByUsername(item.getXchanger());
            ownerLiveData.observe(this, owner -> {
                if (owner != null) {
                    Intent intent = new Intent(this, RequestActivity.class);
                    intent.putExtra("REQUESTED_ITEM", item);
                    intent.putExtra("USER", user);
                    intent.putExtra("ITEM_OWNER", owner);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Owner not found!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
