package com.idk.finnanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idk.finnanceapp.RecycleView.Adapter;
import com.idk.finnanceapp.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //TODO: add settings
    Button BTNAdd;
    RecyclerView RVTransatcions;
    TextView TVBalanceNumber;
    private List<Transaction> transactions = new ArrayList<>();
    private double initialBalance = 0;
    private SharedPreferencesDB db;
    SearchView SVSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SVSearch = findViewById(R.id.SVSearch);
        BTNAdd = findViewById(R.id.BTNAdd);
        RVTransatcions = findViewById(R.id.RVTransatcions);
        TVBalanceNumber = findViewById(R.id.TVBalanceNumber);
        TVBalanceNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBalance(v);
            }
        });
        db = SharedPreferencesDB.getInstance(this);
        if (!db.hasBalance()) {
            setBalance(null);
        } else {
            initialBalance = db.getBalance();
            updateBalanceDisplay();
        }
        transactions = db.getTransactions();

        RVTransatcions.setLayoutManager(new LinearLayoutManager(this));
        Adapter adapter = new Adapter(transactions, this);
        RVTransatcions.setAdapter(adapter);
        BTNAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTransaction(v, adapter);
                RVTransatcions.scrollToPosition(transactions.size() - 1);
            }
        });
        SVSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Search(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                Search(query);
                return false;
            }
        });
        adapter.setOnItemLongClickListener(new Adapter.onItemLongClickListener() {
            @Override
            public void onItemLongClick(Transaction transaction) {
                DeleteTransaction(transaction, adapter);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        updateBalanceDisplay();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.saveTransactions(transactions);
    }

    public Dialog openAddTransaction(View view,Adapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_transaction_dialog, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
        Button BTNSaveTransactionDialog = dialogView.findViewById(R.id.BTNSaveTransactionDialog);
        BTNSaveTransactionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox CBisExpense = dialogView.findViewById(R.id.CBIsExpenseDialog);
                EditText etTitle = dialogView.findViewById(R.id.TVShortDescriptionDialog);
                String desc = etTitle.getText().toString();
                EditText etAmount = dialogView.findViewById(R.id.TVAmountDialog);
                String amount = etAmount.getText().toString();
                EditText etDate = dialogView.findViewById(R.id.TVDateDialog);
                String date = etDate.getText().toString();
                String title = desc;
                boolean isExpense;
                if (CBisExpense.isChecked()) {
                    isExpense = true;
                } else {
                    isExpense = false;
                }
                if (title.isEmpty() || amount.isEmpty() || date.isEmpty()) {
                    return;
                }
                transactions.add(new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0));
                db.saveTransactions(transactions);
                adapter.notifyDataSetChanged();
                updateBalanceDisplay();
                dialog.dismiss();

            }
        });
        return dialog;
    }
    public Dialog DeleteTransaction(Transaction transaction, Adapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.remove_dialog, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
        Button BTNDenyRDialog = dialogView.findViewById(R.id.BTNDenyRDialog);
        Button BTNConfirmRDialog = dialogView.findViewById(R.id.BTNConfirmRDialog);
        BTNDenyRDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        BTNConfirmRDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTransaction(transaction);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        return dialog;
    }
    public Dialog setBalance(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.set_balance_dialog, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
        Button BTNSaveBalance = dialogView.findViewById(R.id.BTNSaveBalance);
        BTNSaveBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etBalance = dialogView.findViewById(R.id.EDBalance);
                String balanceStr = etBalance.getText().toString();
                if (balanceStr.isEmpty()) {
                    return;
                }
                initialBalance = Double.parseDouble(balanceStr);
                db.setBalance(initialBalance);
                updateBalanceDisplay();
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
        db.saveTransactions(transactions);
        updateBalanceDisplay();
    }

    private void updateBalanceDisplay() {
        double totalTransactions = 0;
        for (Transaction t : transactions) {
            double amt = Double.parseDouble(t.getAmount());
            if (t.getType() == 0) {
                totalTransactions += amt;
            } else {
                totalTransactions -= amt;
            }
        }
        double currentBalance = initialBalance + totalTransactions;
        TVBalanceNumber.setText(String.format("%.2f (%.2f)", currentBalance, initialBalance));
    }
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public SearchView getSVSearch() {
        return SVSearch;
    }
    public void Search(String str){
        List<Transaction> filteredList = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getTitle().toLowerCase().contains(str.toLowerCase())) {
                filteredList.add(transaction);
            }
        }
        if (filteredList.isEmpty()) {
            return;
        }
        Adapter adapter = (Adapter) RVTransatcions.getAdapter();
        adapter.setFilteredList(filteredList);
    }
}