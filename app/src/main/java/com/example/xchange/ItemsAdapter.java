package com.example.xchange;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.database.AppDatabase;
import com.example.xchange.database.dao.UserDao;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Adapter class for managing and displaying a list of {@link Item} objects in a {@link RecyclerView}.
 * <p>
 * This adapter handles the display of item details in a card layout and manages click events for individual items.
 * </p>
 */
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items;
    private User currentUser; // Προσθήκη του currentUser
    private OnItemClickListener listener;

    /**
     * Constructs a new {@link ItemsAdapter}.
     *
     * @param items       The list of {@link Item} objects to display.
     * @param currentUser The currently logged-in {@link User}.
     */
    public ItemsAdapter(List<Item> items, User currentUser) {
        this.items = items;
        this.currentUser = currentUser; // Αποθήκευση του currentUser
    }

    /**
     * Interface for handling click events on item cards.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item card is clicked.
         *
         * @param itemId The ID of the clicked item.
         */
        void onItemClick(Long itemId);
    }

    /**
     * Sets the click listener for item cards.
     *
     * @param listener The listener to handle item click events.
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the list of items displayed by the adapter.
     *
     * @param newItems The new list of {@link Item} objects.
     */
    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    /**
     * Creates a new {@link ItemViewHolder}.
     *
     * @param parent   The parent {@link ViewGroup}.
     * @param viewType The view type of the new {@link ItemViewHolder}.
     * @return A new {@link ItemViewHolder}.
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }


    /**
     * Binds an {@link Item} to an {@link ItemViewHolder}.
     *
     * @param holder   The {@link ItemViewHolder} to bind data to.
     * @param position The position of the {@link Item} in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);

        // Ρύθμιση δεδομένων στα TextView
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemDescriptionTextView.setText(item.getItemDescription());
        holder.itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName());
        holder.itemConditionTextView.setText("Condition: " + item.getItemCondition());
        holder.itemUsernameTextView.setText("Posted by: " + item.getXchanger());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
            intent.putExtra("ITEM_ID", item.getItemId());
            intent.putExtra("USER", currentUser); // Χρήση του currentUser
            holder.itemView.getContext().startActivity(intent);
        });
    }

    /**
     * Gets the number of items in the list.
     *
     * @return The number of items.
     */
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * ViewHolder class for {@link Item} objects.
     * <p>
     * This class holds references to the views that display the details of an {@link Item}.
     * </p>
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemDescriptionTextView;
        TextView itemCategoryTextView;
        TextView itemConditionTextView;
        TextView itemUsernameTextView;

        /**
         * Constructs a new {@link ItemViewHolder}.
         *
         * @param itemView The view representing a single {@link Item} card.
         */
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            itemCategoryTextView = itemView.findViewById(R.id.itemCategoryTextView);
            itemConditionTextView = itemView.findViewById(R.id.itemConditionTextView);
            itemUsernameTextView = itemView.findViewById(R.id.itemUsernameTextView);
        }
    }
}

