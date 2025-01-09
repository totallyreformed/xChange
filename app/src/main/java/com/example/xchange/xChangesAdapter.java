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

public class xChangesAdapter extends RecyclerView.Adapter<xChangesAdapter.xChangeViewHolder> {

    private List<xChange> xChanges;

    public xChangesAdapter(List<xChange> xChanges) {
        this.xChanges = xChanges;
    }

    public void setXChanges(List<xChange> newXChanges) {
        this.xChanges = newXChanges;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public xChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xchange_card, parent, false);
        return new xChangeViewHolder(view);
    }

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

    @Override
    public int getItemCount() {
        return xChanges == null ? 0 : xChanges.size();
    }

    public static class xChangeViewHolder extends RecyclerView.ViewHolder {
        TextView exchangeDetailsTextView;
        TextView requesterTextView;
        TextView requesteeTextView;
        TextView statusTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;

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