package com.idk.finnanceapp.RecycleView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.idk.finnanceapp.CSVHelper;
import com.idk.finnanceapp.LogInActivity;
import com.idk.finnanceapp.MainActivity;
import com.idk.finnanceapp.R;
import com.idk.finnanceapp.SQLiteUserHelperDB;
import com.idk.finnanceapp.SettingsActivity;
import com.idk.finnanceapp.SharedPreferencesDB;
import com.idk.finnanceapp.SignActivity;
import com.idk.finnanceapp.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebugActivity extends AppCompatActivity {
    Button BTNIntentDebug;
    Button BTNIntentMain;
    Button BTNIntentSettings;
    Button BTNIntentLogIn;
    Button BTNIntentSignUp;
    Button BTNRandomTransactions;
    SharedPreferencesDB db;
    CSVHelper csvHelper;

    SQLiteUserHelperDB sqLiteUserHelperDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debug);
        BTNIntentMain = findViewById(R.id.BTNIntentMain);
        BTNIntentSettings = findViewById(R.id.BTNIntentSettings);
        BTNIntentLogIn = findViewById(R.id.BTNIntentLogIn);
        BTNIntentSignUp = findViewById(R.id.BTNIntentSignUp);
        BTNIntentDebug = findViewById(R.id.BTNIntentDebug);
        BTNRandomTransactions = findViewById(R.id.BTNRandomTransactions);
        db = SharedPreferencesDB.getInstance(this);
        csvHelper = new CSVHelper();

        sqLiteUserHelperDB = new SQLiteUserHelperDB(this);

        BTNIntentMain.setOnClickListener(v -> intentMain());
        BTNIntentSettings.setOnClickListener(v -> intentSettings());
        BTNIntentLogIn.setOnClickListener(v -> intentLogIn());
        BTNIntentSignUp.setOnClickListener(v -> intentSignUp());
        BTNIntentDebug.setOnClickListener(v -> intentDebug());
        BTNRandomTransactions.setOnClickListener(v -> testTransactions());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private String getUserFilePath() {
        String username = db.getUsername();
        if (username != null && !username.trim().isEmpty()) {
            username = username.trim().toLowerCase();
            String filePath = sqLiteUserHelperDB.getFilePath(username);
            if (filePath != null && !filePath.isEmpty()) {
                return filePath;
            } else {
                return sqLiteUserHelperDB.createPath(username);
            }
        }
        File defaultFolder = new File(getFilesDir(), "user_csv_data");
        if (!defaultFolder.exists()) {
            defaultFolder.mkdirs();
        }
        return new File(defaultFolder, "default_user.csv").getAbsolutePath();
    }
    public void testTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Transaction newTransaction;
            String filePath = getUserFilePath();
            transactions.addAll(csvHelper.readTransactionsFromCSV(filePath));
            boolean isExpense = Math.random() < 0.5;
            String title = "Transaction " + i;
            String amount = String.valueOf(Math.random() * 1000);
            Date date = new Date((i * i/10), i ^ i*10, i * i*10);
            String catagory = "Category " + i;
            String note = "Note " + i;
            newTransaction = new Transaction(transactions.size() + 1, title, amount, date, isExpense ? 1 : 0, catagory, note);
            transactions.add(newTransaction);
            csvHelper.saveCSV(filePath, 0.0, transactions);
        }
    }
    public void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void intentLogIn(){
        db = SharedPreferencesDB.getInstance(this);
        db.setRememberMe(false);
        db.removeKeysUser();
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
    public void intentSignUp(){
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }
    public void intentSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void intentDebug(){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
        //I know this seems kinda counterintuitive im thinking this one is just to refresh if theres some very wierd issues tied to this activity
    }




}