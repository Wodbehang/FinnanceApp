package com.idk.finnanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogInActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button login;
    Button signUp;
    CheckBox cbRememberMe;
    SQLiteUserHelperDB sqliteUserHelperDB;
    SharedPreferencesDB sharedPreferencesDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        sqliteUserHelperDB = new SQLiteUserHelperDB(this);
        sharedPreferencesDB = SharedPreferencesDB.getInstance(this);
        username = findViewById(R.id.ETUsernameLog);
        password = findViewById(R.id.ETPasswordLog);
        login = findViewById(R.id.BTNLogIn);
        signUp = findViewById(R.id.BTNNewAccount);
        cbRememberMe = findViewById(R.id.CBRememberMe);
        if (sharedPreferencesDB.isRememberMe()){
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        signUp.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          intentSignUp(view);}
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateCardentials(username.getText().toString().toLowerCase(), password.getText().toString())){
                    sharedPreferencesDB.setKeysUser(username.getText().toString(), password.getText().toString());
                    if (cbRememberMe.isChecked()){
                        sharedPreferencesDB.setRememberMe(true);
                        username.setText(sharedPreferencesDB.getUsername());
                        password.setText(sharedPreferencesDB.getPassword());
                    } else {
                        sharedPreferencesDB.setRememberMe(false);
                    }
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LogInActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public boolean validateCardentials(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (sqliteUserHelperDB.checkUser(email, password)){
            return true;
        }
        return false;
    }
    public void intentSignUp(View view){
        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

}