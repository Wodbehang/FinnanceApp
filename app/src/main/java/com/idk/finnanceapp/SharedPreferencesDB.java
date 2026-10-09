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
    private final SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "FinnanceAppPrefs";
    private static final String KEY_BALANCE = "balance";
    private static final String KEY_TRANSACTIONS = "transactions";
    private final Gson gson;

    private SharedPreferencesDB(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }
    public static SharedPreferencesDB getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesDB(context.getApplicationContext());
        }
        return instance;
    }

    public void setBalance(double balance) {
        editor.putFloat(KEY_BALANCE, (float) balance);
        editor.apply();
    }
    public double getBalance() {
        return sharedPreferences.getFloat(KEY_BALANCE, 0f);
    }
    public void clear() {
        editor.clear();
        editor.apply();
    }
    public void removeBalance() {
        editor.remove(KEY_BALANCE);
        editor.apply();
    }
    public boolean hasBalance() {
        return sharedPreferences.contains(KEY_BALANCE);
    }

    public void saveTransactions(List<Transaction> transactions) {
        String json = gson.toJson(transactions);
        editor.putString(KEY_TRANSACTIONS, json);
        editor.apply();
    }
    public List<Transaction> getTransactions() {
        String json = sharedPreferences.getString(KEY_TRANSACTIONS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        return gson.fromJson(json, type);
    }
}
