package com.idk.finnanceapp.RecycleView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.idk.finnanceapp.R;
import com.idk.finnanceapp.Transaction;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Transaction> transactions;
    private Context context;
    public Adapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }
    public int getItemCount() {
        return transactions.size();
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }


    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TVTitle;
        private TextView TVAmount;
        private TextView TVDate;
        private TextView TVID;

        public ViewHolder(View itemView) {
            super(itemView);
            TVTitle = itemView.findViewById(R.id.TVTransactionShortDescription);
            TVAmount = itemView.findViewById(R.id.TVTransactionAmount);
            TVDate = itemView.findViewById(R.id.TVTransactionDate);
            TVID = itemView.findViewById(R.id.TVTranactionID);
        }

        public void bind(Transaction transaction) {
            TVID.setText(String.valueOf(transaction.getID()));
            TVTitle.setText(transaction.getTitle());
            if (transaction.getType() == 0) {
                TVAmount.setText("+ " + transaction.getAmount());
            } else {
                TVAmount.setText("- " + transaction.getAmount());
            }
            TVDate.setText(transaction.getDate());
        }
    }
    public void removeItem(int position) {
        transactions.remove(position);
        notifyItemRemoved(position);
    }
    public void setFilteredList(List<Transaction> filteredList) {
        this.transactions = filteredList;
        notifyDataSetChanged();
    }
    public int getItemViewCount(int position) {
        return transactions.size();
    }
}

