package com.example.xchange;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items;
    private User currentUser; // Προσθήκη του currentUser
    private OnItemClickListener listener;

    public ItemsAdapter(List<Item> items, User currentUser) {
        this.items = items;
        this.currentUser = currentUser; // Αποθήκευση του currentUser
    }

    public interface OnItemClickListener {
        void onItemClick(Long itemId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemDescriptionTextView;
        TextView itemCategoryTextView;
        TextView itemConditionTextView;
        TextView itemUsernameTextView;

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

