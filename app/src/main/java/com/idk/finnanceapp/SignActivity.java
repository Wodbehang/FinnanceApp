package com.idk.finnanceapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignActivity extends AppCompatActivity {
    static SQLiteUserHelperDB sqliteUserHelperDB;
    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etRePassword;
    Button btnSignUP;

    SQLiteDatabase userDB = sqliteUserHelperDB.getWritableDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign);
        etUsername = findViewById(R.id.ETUsername);
        etEmail = findViewById(R.id.ETEmail);
        etPassword = findViewById(R.id.ETPassword);
        etRePassword = findViewById(R.id.ETRePassword);
        btnSignUP = findViewById(R.id.BTNSignUp);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public boolean saveUser(){
        sqliteUserHelperDB = new SQLiteUserHelperDB(this);
        return true;
    }
}