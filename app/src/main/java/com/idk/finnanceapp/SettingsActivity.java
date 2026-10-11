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

import java.io.File;

public class SettingsActivity extends AppCompatActivity {
    Button BTNClearData;
    Button BTNExport;
    Utilitis utilitis;
    private SharedPreferencesDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        BTNClearData = findViewById(R.id.BTNClearData);
        BTNExport = findViewById(R.id.BTNExport);
        utilitis = new Utilitis();
        db = SharedPreferencesDB.getInstance(this);

        BTNClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesDB db = SharedPreferencesDB.getInstance(SettingsActivity.this);
                db.clear();
                db.removeBalance();
                finish();
            }
        });
        BTNExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ExportTransactions()) {
                    File file = new File(getExternalFilesDir(null) + "/transactions.csv");
                    Uri uri = androidx.core.content.FileProvider.getUriForFile(SettingsActivity.this, "com.idk.finnanceapp.fileprovider", new File(getExternalFilesDir(null) + "/transactions.csv"));

                    Intent shereUntent = new Intent(Intent.ACTION_SEND);
                    shereUntent.setType("text/csv");
                    shereUntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shereUntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(shereUntent);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public boolean ExportTransactions() {
        try {
            utilitis.saveTransactionsToCVS(db.getTransactions(), getExternalFilesDir(null) + "/transactions.csv");
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

}