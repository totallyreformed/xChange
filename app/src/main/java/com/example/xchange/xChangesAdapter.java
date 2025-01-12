package com.example.xchange;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter for displaying a list of {@link xChange} objects in a {@link RecyclerView}.
 * <p>
 * This adapter binds the data from a list of {@link xChange} objects to the corresponding UI elements
 * in the RecyclerView.
 * </p>
 */
public class xChangesAdapter extends RecyclerView.Adapter<xChangesAdapter.xChangeViewHolder> {

    private List<xChange> xChanges;

    /**
     * Constructs a new {@link xChangesAdapter}.
     *
     * @param xChanges The initial list of {@link xChange} objects to display.
     */
    public xChangesAdapter(List<xChange> xChanges) {
        this.xChanges = xChanges;
    }

    /**
     * Updates the list of {@link xChange} objects and refreshes the RecyclerView.
     *
     * @param newXChanges The new list of {@link xChange} objects to display.
     */
    public void setXChanges(List<xChange> newXChanges) {
        this.xChanges = newXChanges;
        notifyDataSetChanged();
    }

    /**
     * Creates a new {@link xChangeViewHolder} to represent a single {@link xChange} item.
     *
     * @param parent   The parent {@link ViewGroup}.
     * @param viewType The view type of the new {@link xChangeViewHolder}.
     * @return A new {@link xChangeViewHolder} for the RecyclerView.
     */
    @NonNull
    @Override
    public xChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xchange_card, parent, false);
        return new xChangeViewHolder(view);
    }

    /**
     * Binds the data from a {@link xChange} object to the corresponding UI elements in the ViewHolder.
     *
     * @param holder   The {@link xChangeViewHolder} that holds the UI elements for a single item.
     * @param position The position of the item in the dataset.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull xChangeViewHolder holder, int position) {
        xChange xchange = xChanges.get(position);

        // Handle null values to avoid crashes
        String offererName = xchange.getOfferer() != null ? xchange.getOfferer().getUsername() : "Unknown Offerer";
        String offereeName = xchange.getOfferee() != null ? xchange.getOfferee().getUsername() : "Unknown Offeree";
        String status = xchange.getDealStatus() != null ? xchange.getDealStatus() : "Unknown Status";
        String requestedItem = xchange.getRequestedItem() != null ? xchange.getRequestedItem().getItemName() : "Unknown Item";
        String offeredItem = xchange.getOfferedItem() != null ? xchange.getOfferedItem().getItemName() : "Unknown Item";

        holder.exchangeDetailsTextView.setText("Exchange Details");
        holder.requesterTextView.setText("Requester: " + offererName);
        holder.requesteeTextView.setText("Requestee: " + offereeName);
        holder.statusTextView.setText("Status: " + status);
        holder.requestedItemTextView.setText("Requested Item: " + requestedItem);
        holder.offeredItemTextView.setText("Offered Item: " + offeredItem);
    }

    /**
     * Returns the total number of {@link xChange} objects in the dataset.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return xChanges == null ? 0 : xChanges.size();
    }

    /**
     * ViewHolder for displaying a single {@link xChange} in the RecyclerView.
     */
    public static class xChangeViewHolder extends RecyclerView.ViewHolder {
        TextView exchangeDetailsTextView;
        TextView requesterTextView;
        TextView requesteeTextView;
        TextView statusTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;

        /**
         * Constructs a new {@link xChangeViewHolder}.
         *
         * @param itemView The root view of the individual item layout.
         */
        public xChangeViewHolder(@NonNull View itemView) {
            super(itemView);
            exchangeDetailsTextView = itemView.findViewById(R.id.exchangeDetailsTextView);
            requesterTextView = itemView.findViewById(R.id.requesterTextView);
            requesteeTextView = itemView.findViewById(R.id.requesteeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            requestedItemTextView = itemView.findViewById(R.id.requestedItemTextView);
            offeredItemTextView = itemView.findViewById(R.id.offeredItemTextView);
        }
    }
}