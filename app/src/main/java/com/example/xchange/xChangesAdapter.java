package com.example.xchange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class xChangesAdapter extends RecyclerView.Adapter<xChangesAdapter.xChangeViewHolder> {

    private final List<xChange> xChanges;

    public xChangesAdapter(List<xChange> xChanges) {
        this.xChanges = xChanges;
    }

    @NonNull
    @Override
    public xChangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.xchange_card, parent, false);
        return new xChangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull xChangeViewHolder holder, int position) {
        xChange xchange = xChanges.get(position);
        holder.bind(xchange);
    }

    @Override
    public int getItemCount() {
        return xChanges.size();
    }

    static class xChangeViewHolder extends RecyclerView.ViewHolder {

        private final TextView exchangeDetailsTextView;

        public xChangeViewHolder(@NonNull View itemView) {
            super(itemView);
            exchangeDetailsTextView = itemView.findViewById(R.id.exchangeDetailsTextView);
        }

        public void bind(xChange xchange) {
            String details = "Offerer: " + xchange.getOfferer().getUsername() + "\n"
                    + "Offeree: " + xchange.getOfferee().getUsername() + "\n"
                    + "Date: " + xchange.getDateFinalized().toString();
            exchangeDetailsTextView.setText(details);
        }
    }
}
