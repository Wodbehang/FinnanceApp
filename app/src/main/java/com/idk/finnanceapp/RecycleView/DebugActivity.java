package com.idk.finnanceapp.RecycleView;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.idk.finnanceapp.LogInActivity;
import com.idk.finnanceapp.MainActivity;
import com.idk.finnanceapp.R;
import com.idk.finnanceapp.SettingsActivity;
import com.idk.finnanceapp.SignActivity;
import com.idk.finnanceapp.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_debug);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public boolean testTransactions(){
        List<Transaction> transactions = new ArrayList<>();
        //TODO implement a randomly generated list of 10 transactions to test Transactions and save them to the CSV file
        for (int i = 0; i < 10; i++) {

        }
        return true;
    }
    public void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void intentLogIn(){
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