package com.idk.finnanceapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.regex.Pattern;

public class SignActivity extends AppCompatActivity {
    SQLiteUserHelperDB sqliteUserHelperDB;
    EditText etUsername;
    EditText etEmail;
    EditText etPassword;
    EditText etRePassword;
    Button btnSignUP;
    SQLiteDatabase userDB;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#?&]{8,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign);
        sqliteUserHelperDB = new SQLiteUserHelperDB(this);
        userDB = sqliteUserHelperDB.getWritableDatabase();

        etUsername = findViewById(R.id.ETUsername);
        etEmail = findViewById(R.id.ETEmail);
        etPassword = findViewById(R.id.ETPassword);
        etRePassword = findViewById(R.id.ETRePassword);
        btnSignUP = findViewById(R.id.BTNSignUp);
        btnSignUP.setOnClickListener(v -> {
            String username = etUsername.getText().toString().toLowerCase();
            String email = etEmail.getText().toString().toLowerCase();
            String password = etPassword.getText().toString();
            String rePassword = etRePassword.getText().toString();
            validateAndCreateUser(username, email, password, rePassword);
                });


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
    public void validateAndCreateUser(String username, String email,String password, String rePassword){
        if (validateUsername(username) && validateEmail(email) && validatePassword(password, rePassword)){
            sqliteUserHelperDB.newUser(username, email, password);
            SharedPreferencesDB sharedPreferencesDB = SharedPreferencesDB.getInstance(this);
            sharedPreferencesDB.setKeysUser(username, password);
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            intentMain();
            finish();
            }
        else{
            Toast.makeText(this, "User registration failed", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean validateUsername(String usermame) {
        if (usermame == null || usermame.isEmpty()){
            Toast.makeText(this,"Username most be at least 4 caracters long", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
    public boolean validateEmail(String email){
        if (email == null || email.isEmpty()){
            Toast.makeText(this, "Email must not be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public boolean validatePassword(String password,String rePassword){
        if (!(password == null && password.isEmpty())){
            if (rePassword == null || rePassword.isEmpty()){
                Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (password.equals(rePassword)) {
                return PASSWORD_PATTERN.matcher(password).matches();
            }
            else{
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        return false;

    }
    public void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}