package com.idk.finnanceapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesDB {
    private static SharedPreferencesDB instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS_NAME = "FinnanceAppPrefs";
    private static final String KEY_BALANCE = "balance";

    private SharedPreferencesDB(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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

}
