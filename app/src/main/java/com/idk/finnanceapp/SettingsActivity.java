package com.idk.finnanceapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.idk.finnanceapp.RecycleView.DebugActivity;

import java.io.File;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    Button BTNClearData;
    Button BTNExport;
    Button BTNLogOut;
    Button BTNDeleteUser;
    Button BTNDebug;
    CSVHelper csvHelper;
    private SharedPreferencesDB db;
    SQLiteUserHelperDB sqLiteUserHelperDB;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        BTNClearData = findViewById(R.id.BTNClearData);
        BTNExport = findViewById(R.id.BTNExport);
        BTNLogOut = findViewById(R.id.BTNLogOut);
        BTNDeleteUser = findViewById(R.id.BTNDeleteUser);
        BTNDebug = findViewById(R.id.BTNDebug);
        csvHelper = new CSVHelper();
        sqLiteUserHelperDB = new SQLiteUserHelperDB(this);

        db = SharedPreferencesDB.getInstance(SettingsActivity.this);
        BTNDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DebugActivity.class);
                startActivity(intent);
            }
        });

        BTNClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                db.clear();
                db.removeBalance();*/
                String username = db.getUsername();
                if (username != null && !username.trim().isEmpty()) {
                    String filePath = sqLiteUserHelperDB.getFilePath(username.trim().toLowerCase());
                    if (filePath != null) {
                        csvHelper.saveCSV(filePath, 0.0, new ArrayList<>());
                    }
                }
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        BTNLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.setRememberMe(false);
                db.removeKeysUser();
                Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        BTNDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = db.getUsername();
                if (username != null && !username.trim().isEmpty()) {
                    String filePath = sqLiteUserHelperDB.getFilePath(username.trim().toLowerCase());
                    if (filePath != null) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    sqLiteUserHelperDB.deleteUser(username.trim().toLowerCase());
                }
                db.setRememberMe(false);
                db.removeKeysUser();
//                db.removeBalance();
                db.removeUser();
                Intent intent = new Intent(SettingsActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }});


        BTNExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = db.getUsername().trim().toLowerCase();

                File file = ExportTransactions();
                if (file != null && file.exists()) {
                    Uri uri = androidx.core.content.FileProvider.getUriForFile(SettingsActivity.this, "com.idk.finnanceapp.fileprovider", file);

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/csv");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Shere transactions csv file"));
                }

            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public File ExportTransactions() {
        try {
            String username = db.getUsername().trim().toLowerCase();
            String filePath = sqLiteUserHelperDB.getFilePath(username);

            if (filePath == null || filePath.isEmpty()) {
                sqLiteUserHelperDB.createPath(username);
                filePath = sqLiteUserHelperDB.getFilePath(username);
            }
            double balance = csvHelper.readBalanceFromCSV(filePath);
            java.util.List<Transaction> transactions = csvHelper.readTransactionsFromCSV(filePath);
            csvHelper.saveCSV(filePath, balance, transactions);

            return new File(filePath);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}