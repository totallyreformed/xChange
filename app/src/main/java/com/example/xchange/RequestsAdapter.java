package com.example.xchange;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.AcceptRequest.AcceptRequestActivity;

import java.util.List;

/**
 * Adapter class for displaying a list of {@link Request} objects in a {@link RecyclerView}.
 * <p>
 * This adapter binds {@link Request} data to a card layout and handles click events for individual requests.
 * </p>
 */
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<Request> requests;
    private User currentUser;
    private final OnRequestClickListener clickListener;
    private Context context;

    /**
     * Interface for handling click events on request cards.
     */
    public interface OnRequestClickListener {
        /**
         * Called when a {@link Request} card is clicked.
         *
         * @param request The clicked {@link Request}.
         */
        void onRequestClicked(Request request);
    }

    /**
     * Constructs a new {@link RequestsAdapter}.
     *
     * @param requests     The list of {@link Request} objects to display.
     * @param currentUser  The currently logged-in {@link User}.
     * @param clickListener The listener to handle request click events.
     * @param context      The context in which the adapter is used.
     */
    public RequestsAdapter(List<Request> requests, User currentUser, OnRequestClickListener clickListener, Context context) {
        this.requests = requests;
        this.currentUser = currentUser;
        this.clickListener = clickListener;
        this.context = context;
    }

    /**
     * Updates the list of requests displayed by the adapter.
     *
     * @param newRequests The new list of {@link Request} objects.
     */
    public void setRequests(List<Request> newRequests) {
        this.requests = newRequests;
        notifyDataSetChanged();
    }

    /**
     * Creates a new {@link RequestViewHolder}.
     *
     * @param parent   The parent {@link ViewGroup}.
     * @param viewType The view type of the new {@link RequestViewHolder}.
     * @return A new {@link RequestViewHolder}.
     */
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(view);
    }

    /**
     * Binds a {@link Request} to a {@link RequestViewHolder}.
     *
     * @param holder   The {@link RequestViewHolder} to bind data to.
     * @param position The position of the {@link Request} in the list.
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);

        holder.requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        holder.requesteeTextView.setText("Requestee: " + request.getRequestee().getUsername());
        holder.requestStatusTextView.setText("Status: " + request.getStatus());
        holder.requestedItemTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());
        holder.offeredItemTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());

        // Set the click listener for the entire itemView
        // Set the click listener for the entire itemView
        holder.itemView.setOnClickListener(v -> {
            // Start AcceptRequestActivity
            Intent intent = new Intent(context, AcceptRequestActivity.class);
            intent.putExtra("REQUEST", request);
            intent.putExtra("USER", currentUser);
            context.startActivity(intent);
        });
    }

    /**
     * Gets the number of requests in the list.
     *
     * @return The number of requests.
     */
    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    /**
     * ViewHolder class for {@link Request} objects.
     * <p>
     * This class holds references to the views that display the details of a {@link Request}.
     * </p>
     */
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestIdTextView;
        TextView requesterTextView;
        TextView requesteeTextView;
        TextView requestStatusTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;

        /**
         * Constructs a new {@link RequestViewHolder}.
         *
         * @param itemView The view representing a single {@link Request} card.
         */
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestIdTextView = itemView.findViewById(R.id.requestIdTextView);
            requesterTextView = itemView.findViewById(R.id.requesterTextView);
            requesteeTextView = itemView.findViewById(R.id.requesteeTextView);
            requestStatusTextView = itemView.findViewById(R.id.statusTextView);
            requestedItemTextView = itemView.findViewById(R.id.requestedItemTextView);
            offeredItemTextView = itemView.findViewById(R.id.offeredItemTextView);
        }
    }
}
