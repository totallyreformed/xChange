package com.example.xchange;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class for managing and displaying a list of {@link Counteroffer} objects in a {@link RecyclerView}.
 */
public class CounteroffersAdapter extends RecyclerView.Adapter<CounteroffersAdapter.CounterofferViewHolder> {

    private List<Counteroffer> counteroffers;
    private final OnCounterofferClickListener clickListener;

    /**
     * Interface for handling click events on counteroffer items.
     */
    public interface OnCounterofferClickListener {
        /**
         * Called when a {@link Counteroffer} is clicked.
         *
         * @param counteroffer The clicked {@link Counteroffer}.
         */
        void onCounterofferClicked(Counteroffer counteroffer);
    }

    /**
     * Constructs a new {@link CounteroffersAdapter}.
     *
     * @param counteroffers The initial list of {@link Counteroffer} objects to display.
     * @param clickListener The listener to handle item click events.
     */
    public CounteroffersAdapter(List<Counteroffer> counteroffers, OnCounterofferClickListener clickListener) {
        this.counteroffers = counteroffers;
        this.clickListener = clickListener;
    }

    /**
     * Updates the list of {@link Counteroffer} objects and notifies the adapter of the change.
     *
     * @param newCounteroffers The new list of {@link Counteroffer} objects.
     */
    public void setCounteroffers(List<Counteroffer> newCounteroffers) {
        this.counteroffers = newCounteroffers;
        notifyDataSetChanged();
    }

    /**
     * Creates a new {@link CounterofferViewHolder}.
     *
     * @param parent   The parent {@link ViewGroup}.
     * @param viewType The view type of the new {@link CounterofferViewHolder}.
     * @return A new {@link CounterofferViewHolder}.
     */
    @NonNull
    @Override
    public CounterofferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.counteroffer_card, parent, false);
        return new CounterofferViewHolder(view);
    }

    /**
     * Binds a {@link Counteroffer} to a {@link CounterofferViewHolder}.
     *
     * @param holder   The {@link CounterofferViewHolder} to bind data to.
     * @param position The position of the {@link Counteroffer} in the list.
     */
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

    /**
     * Gets the number of {@link Counteroffer} objects in the list.
     *
     * @return The number of {@link Counteroffer} objects.
     */
    @Override
    public int getItemCount() {
        return counteroffers == null ? 0 : counteroffers.size();
    }

    /**
     * ViewHolder class for {@link Counteroffer} items.
     */
    public static class CounterofferViewHolder extends RecyclerView.ViewHolder {
        TextView counteroffererTextView;
        TextView counteroffereeTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;
        TextView activeStatusTextView;

        /**
         * Constructs a new {@link CounterofferViewHolder}.
         *
         * @param itemView The view for a single {@link Counteroffer} item.
         */
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
