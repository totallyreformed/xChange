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

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items;
    private UserDao userDao; // UserDao για αναζήτηση χρηστών
    private OnItemClickListener listener;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ItemsAdapter(List<Item> items) {
        this.items = items;
        this.userDao = AppDatabase.getUserDao();
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
        holder.itemCategoryTextView.setText("Category: " + item.getItemCategory().getDisplayName()); // Εμφάνιση κατηγορίας
        holder.itemConditionTextView.setText("Condition: " + item.getItemCondition()); // Εμφάνιση συνθήκης
        holder.itemUsernameTextView.setText("Posted by: " + item.getXchanger()); // Εμφάνιση ονόματος χρήστη

        holder.itemView.setOnClickListener(v -> {
            String name = item.getXchanger();
            Log.d("Name", name);

            executor.execute(() -> {
                User user = userDao.findByUsername_initial(name);
                if (user != null) {
                    Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                    intent.putExtra("ITEM_ID", item.getItemId());
                    intent.putExtra("USER", user); // Περάστε τον User στο Intent
                    holder.itemView.getContext().startActivity(intent);
                } else {
                    holder.itemView.post(() ->
                            Toast.makeText(holder.itemView.getContext(), "User not found", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        TextView itemDescriptionTextView;
        TextView itemCategoryTextView; // Category
        TextView itemConditionTextView; // Condition
        TextView itemUsernameTextView; // Username

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemDescriptionTextView = itemView.findViewById(R.id.itemDescriptionTextView);
            itemCategoryTextView = itemView.findViewById(R.id.itemCategoryTextView); // Σύνδεση για Category
            itemConditionTextView = itemView.findViewById(R.id.itemConditionTextView); // Σύνδεση για Condition
            itemUsernameTextView = itemView.findViewById(R.id.itemUsernameTextView); // Σύνδεση για Username
        }
    }
}
