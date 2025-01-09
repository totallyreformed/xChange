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

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<Request> requests;
    private User currentUser;
    private final OnRequestClickListener clickListener;
    private Context context;

    public interface OnRequestClickListener {
        void onRequestClicked(Request request);
    }

    public RequestsAdapter(List<Request> requests, User currentUser, OnRequestClickListener clickListener, Context context) {
        this.requests = requests;
        this.currentUser = currentUser;
        this.clickListener = clickListener;
        this.context = context;
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

    @Override
    public int getItemCount() {
        return requests == null ? 0 : requests.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView requestIdTextView;
        TextView requesterTextView;
        TextView requesteeTextView;
        TextView requestStatusTextView;
        TextView requestedItemTextView;
        TextView offeredItemTextView;

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
