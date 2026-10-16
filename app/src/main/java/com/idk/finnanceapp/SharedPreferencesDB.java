package com.idk.finnanceapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesDB {
    private static SharedPreferencesDB instance;
    private final SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FinnanceAppPrefs";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_TRANSACTIONS = "transactions";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER_ME = "remember_me";
    private final Gson gson;

    private SharedPreferencesDB(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }
    public static SharedPreferencesDB getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesDB(context.getApplicationContext());
        }
        return instance;
    }

    public void setBalance(double balance) {
        sharedPreferences.edit().putFloat(KEY_BALANCE, (float) balance).apply();
    }
    public double getBalance() {
        return sharedPreferences.getFloat(KEY_BALANCE, 0f);
    }
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
    public void removeBalance() {
        sharedPreferences.edit().remove(KEY_BALANCE).apply();
    }
    public boolean hasBalance() {
        return sharedPreferences.contains(KEY_BALANCE);
    }

    public void saveTransactions(List<Transaction> transactions) {
        String json = gson.toJson(transactions);
        sharedPreferences.edit().putString(KEY_TRANSACTIONS, json).apply();
    }
    public List<Transaction> getTransactions() {
        String json = sharedPreferences.getString(KEY_TRANSACTIONS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setKeysUser(String username, String password){
        sharedPreferences.edit().putString(KEY_USERNAME, username).apply();
        sharedPreferences.edit().putString(KEY_PASSWORD, password).apply();
    }
    public void setRememberMe(boolean rememberMe){
        sharedPreferences.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply();
    }
    public boolean isRememberMe(){
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }
    public String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, null);
    }
    public String getPassword(){
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }
    public void removeKeysUser() {
        sharedPreferences.edit().remove(KEY_USERNAME).apply();
        sharedPreferences.edit().remove(KEY_PASSWORD).apply();
    }
    public void removeUser() {
        sharedPreferences.edit().remove(KEY_USERNAME).apply();
        sharedPreferences.edit().remove(KEY_PASSWORD).apply();
        sharedPreferences.edit().remove(KEY_REMEMBER_ME).apply();
    }
}
