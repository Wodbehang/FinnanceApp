package com.idk.finnanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idk.finnanceapp.RecycleView.Adapter;
import com.idk.finnanceapp.RecycleView.Date;
import com.idk.finnanceapp.Transaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Button BTNAdd;
    ImageButton BTNSettings;
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
        BTNSettings = findViewById(R.id.BTNSettings);
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
        transactions.clear();
        transactions.addAll(db.getTransactions());
        transactions = sortByDate(transactions);

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
        BTNSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        if (!db.hasBalance()) {
            initialBalance = 0;
            setBalance(null);
        } else {
            initialBalance = db.getBalance();
        }

        List<Transaction> freshData = db.getTransactions();
        freshData = sortByDate(freshData);
        transactions.clear();
        transactions.addAll(freshData);
        Adapter adapter = (Adapter) RVTransatcions.getAdapter();
        if (adapter != null) {
            adapter.setFilteredList(transactions);
        }
        updateBalanceDisplay();
    }

    public Dialog openAddTransaction(View view,Adapter adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.add_transaction_dialog, null);
        builder.setView(dialogView);
        Dialog dialog = builder.create();
        dialog.show();
        Button BTNSaveTransactionDialog = dialogView.findViewById(R.id.BTNSaveTransactionDialog);
        Spinner spCatagories = dialogView.findViewById(R.id.SPCatagories);
        List<String> categories = getCategories();
        android.widget.ArrayAdapter<String> SPAdapter = new android.widget.ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, categories);
        spCatagories.setAdapter(SPAdapter);
        BTNSaveTransactionDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction newTransaction;
                CheckBox CBisExpense = dialogView.findViewById(R.id.CBIsExpenseDialog);
                EditText etTitle = dialogView.findViewById(R.id.TVShortDescriptionDialog);
                String desc = etTitle.getText().toString();
                EditText etCatagory = dialogView.findViewById(R.id.ETCatagories);
                String catagory = etCatagory.getText().toString();
                EditText etNote = dialogView.findViewById(R.id.TVNotes);
                String note = etNote.getText().toString();
                EditText etAmount = dialogView.findViewById(R.id.TVAmountDialog);
                String amount = etAmount.getText().toString();
                EditText etDate = dialogView.findViewById(R.id.TVDateDialog);
                String[] dateParts = etDate.getText().toString().split("\\.");
                Date date = null;
                if (dateParts.length >= 3) {
                    try {
                        date = new Date(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                    } catch (NumberFormatException ignored) {}
                }
                if (spCatagories.getSelectedItem() != null) {
                    catagory = spCatagories.getSelectedItem().toString();

                }
                else if (catagory.isEmpty()) {
                    catagory = "None";
                }

                String title = desc;
                boolean isExpense;
                if (CBisExpense.isChecked()) {
                    isExpense = true;
                } else {
                    isExpense = false;
                }
                if (title.isEmpty() || amount.isEmpty() || date == null) {
                    return;
                }
                if (note.isEmpty() && desc.isEmpty()){
                    newTransaction = new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0, "", "");
                }
                else if (note.isEmpty()){
                    newTransaction = new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0, catagory, "");
                }
                else if (catagory.isEmpty()){
                    newTransaction = new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0, "", note);
                }
                else {
                    newTransaction = new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0, catagory, note);
                }
                transactions.add(newTransaction);
                db.saveTransactions(transactions);
                Search(SVSearch.getQuery().toString());
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
                Search(SVSearch.getQuery().toString());
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

    public SearchView getSVSearch() {
        return SVSearch;
    }
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("None");
        categories.add("Food");
        categories.add("Transport");
        categories.add("Health");
        categories.add("Education");
        categories.add("Entertainment");
        categories.add("Other");
        for (Transaction transaction : transactions) {
            if (!categories.contains(transaction.getCategory())) {
                categories.add(transaction.getCategory());
            }
        }
        return categories;
    }
    public void Search(String str){
        List<Transaction> displayList;
        if (str == null || str.isEmpty()) {
            displayList = transactions;
        } else {
            displayList = new ArrayList<>();
            String query = str.toLowerCase();
            for (Transaction transaction : transactions) {
                if (transaction.getTitle() != null && transaction.getTitle().toLowerCase().contains(query)) {
                    displayList.add(transaction);
                }
                else if (transaction.getCategory() != null && transaction.getCategory().toLowerCase().contains(query)) {
                    displayList.add(transaction);
                }
                else if (transaction.getNote() != null && transaction.getNote().toLowerCase().contains(query)) {
                    displayList.add(transaction);
                }
            }
        }
        Adapter adapter = (Adapter) RVTransatcions.getAdapter();
        if (adapter != null) {
            adapter.setFilteredList(displayList);
        }
    }
    public List<Transaction> sortByDate(List<Transaction> transactions){
        if (transactions == null || transactions.isEmpty()) {
            return new ArrayList<>();
        }
        return transactions.stream()
                .sorted((t1, t2) -> {
                    Date date1 = t1.getDate();
                    Date date2 = t2.getDate();
                    if (date1 == null && date2 == null) {
                        return 0;
                    }
                    if (date1 == null) {
                        return 1;
                    }
                    if (date2 == null) {
                        return -1;
                    }
                    int yearDiff = date1.getYear() - date2.getYear();
                    if (yearDiff != 0) {
                        return yearDiff;
                    }
                    int monthDiff = date1.getMonth() - date2.getMonth();
                    if (monthDiff != 0) {
                        return monthDiff;
                    }
                    return Integer.compare(date1.getDay(), date2.getDay());

                }).collect(Collectors.toList());
    }

}