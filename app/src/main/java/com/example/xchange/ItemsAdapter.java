package com.example.xchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items; // Use a custom Item class to represent the data
    private OnItemClickListener listener;

    public ItemsAdapter(List<Item> items) {
        this.items = items;
    }

    public interface OnItemClickListener {
        void onItemClick(Long itemId);
    }

    // Add a setter method for the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged(); // Notify RecyclerView to refresh
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.itemUsernameTextView.setText(item.getxChanger());
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemDescriptionTextView.setText(item.getItemDescription());
        holder.itemCategoryTextView.setText(item.getItemCategory().getDisplayName());
        holder.itemConditionTextView.setText(item.getItemCondition());
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemUsernameTextView;
        TextView itemNameTextView;
        TextView itemDescriptionTextView;
        TextView itemCategoryTextView;
        TextView itemConditionTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemUsernameTextView = itemView.findViewById(R.id.itemUsernameTextView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            itemCategoryTextView = itemView.findViewById(R.id.itemCategoryTextView);
            itemConditionTextView = itemView.findViewById(R.id.itemConditionTextView);

            // Set the click listener for the entire item view
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(items.get(position).getItemId()); // Pass itemId
                }
            });
        }
    }
}
