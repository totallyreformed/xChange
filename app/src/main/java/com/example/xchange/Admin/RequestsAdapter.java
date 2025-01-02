package com.example.xchange.Admin;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xchange.ItemDetail.ItemDetailActivity;
import com.example.xchange.R;
import com.example.xchange.Request;
import com.example.xchange.User;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private List<Request> requests;
    private User currentUser; // Can be null if not needed
    private OnItemClickListener listener;

    public RequestsAdapter(List<Request> requests, User currentUser) {
        this.requests = requests;
        this.currentUser = currentUser;
    }

    public interface OnItemClickListener {
        void onItemClick(Long requestId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requests.get(position);

        // Set data to TextViews
        holder.requestIdTextView.setText("Request ID: " + request.getRequestId());
        holder.requesterTextView.setText("Requester: " + request.getRequester().getUsername());
        holder.requesteeTextView.setText("Requestee: " + request.getRequestee().getUsername());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && request.getRequestId() != null) {
                listener.onItemClick(request.getRequestId());
            }
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
        TextView statusTextView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requestIdTextView = itemView.findViewById(R.id.requestIdTextView);
            requesterTextView = itemView.findViewById(R.id.requesterTextView);
            requesteeTextView = itemView.findViewById(R.id.requesteeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }
    }
}