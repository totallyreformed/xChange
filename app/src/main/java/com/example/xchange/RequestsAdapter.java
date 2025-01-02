package com.example.xchange;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<Request> requests;
    private User currentUser;

    public RequestsAdapter(List<Request> requests, User currentUser) {
        this.requests = requests;
        this.currentUser = currentUser;
    }

    public void setRequests(List<Request> newRequests) {
        this.requests = newRequests;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_card, parent, false);
        return new RequestViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);

        // Debug log for binding
        holder.requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        holder.requesteeTextView.setText("Requestee: " + request.getRequestee().getUsername());
        holder.requestStatusTextView.setText("Status: " + request.getStatus());
        holder.requestedItemTextView.setText("Requested Item: " + request.getRequestedItem().getItemName());
        holder.offeredItemTextView.setText("Offered Item: " + request.getOfferedItem().getItemName());
    }

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView requesterTextView;
        TextView requesteeTextView;
        TextView requestStatusTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterTextView = itemView.findViewById(R.id.requesterTextView);
            requesteeTextView = itemView.findViewById(R.id.requesteeTextView);
            requestStatusTextView = itemView.findViewById(R.id.requestStatusTextView);
            requestedItemTextView = itemView.findViewById(R.id.requestedItemTextView);
            offeredItemTextView = itemView.findViewById(R.id.offeredItemTextView);
        }
    }
}
