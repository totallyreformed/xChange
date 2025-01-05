package com.example.xchange;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CounteroffersAdapter extends RecyclerView.Adapter<CounteroffersAdapter.CounterofferViewHolder> {

    private List<Counteroffer> counteroffers;
    private final OnCounterofferClickListener clickListener;

    public interface OnCounterofferClickListener {
        void onCounterofferClicked(Counteroffer counteroffer);
    }

    public CounteroffersAdapter(List<Counteroffer> counteroffers, OnCounterofferClickListener clickListener) {
        this.counteroffers = counteroffers;
        this.clickListener = clickListener;
    }

    public void setCounteroffers(List<Counteroffer> newCounteroffers) {
        this.counteroffers = newCounteroffers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CounterofferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.counteroffer_card, parent, false);
        return new CounterofferViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CounterofferViewHolder holder, int position) {
        Counteroffer counteroffer = counteroffers.get(position);

        holder.counteroffererTextView.setText("Counterofferer: " + counteroffer.getCounterofferer().getUsername());
        holder.counteroffereeTextView.setText("Counterofferee: " + counteroffer.getCounterofferee().getUsername());
        holder.requestedItemTextView.setText("Requested Item: " + counteroffer.getRequestedItem().getItemName());
        holder.offeredItemTextView.setText("Offered Item: " + counteroffer.getOfferedItem().getItemName());
        holder.activeStatusTextView.setText("Active: " + (counteroffer.isActive() ? "Yes" : "No"));

        // Set the click listener for the entire itemView
        holder.itemView.setOnClickListener(v -> clickListener.onCounterofferClicked(counteroffer));
    }

    @Override
    public int getItemCount() {
        return counteroffers == null ? 0 : counteroffers.size();
    }

    public static class CounterofferViewHolder extends RecyclerView.ViewHolder {
        TextView counteroffererTextView;
        TextView counteroffereeTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;
        TextView activeStatusTextView;

        public CounterofferViewHolder(@NonNull View itemView) {
            super(itemView);
            counteroffererTextView = itemView.findViewById(R.id.counteroffererTextView);
            counteroffereeTextView = itemView.findViewById(R.id.counteroffereeTextView);
            requestedItemTextView = itemView.findViewById(R.id.requestedItemTextView);
            offeredItemTextView = itemView.findViewById(R.id.offeredItemTextView);
            activeStatusTextView = itemView.findViewById(R.id.activeStatusTextView);
        }
    }
}
