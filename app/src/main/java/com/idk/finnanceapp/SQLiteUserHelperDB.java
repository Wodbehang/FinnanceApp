package com.idk.finnanceapp;

import static com.idk.finnanceapp.SQLiteUser.SQL_CREATE_ENTRIES;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.UUID;

public class SQLiteUserHelperDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    Context context;

    public static final String DATABASE_NAME = "SQLUser.db";

    public SQLiteUserHelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public static String stopDuplicateEmail(String email) {
        if (email == null) return "";
        String trimmedEmail = email.trim().toLowerCase();
        int atIndex = trimmedEmail.indexOf('@');
        if (atIndex != -1) {
            String localPart = trimmedEmail.substring(0, atIndex).replace(".", "");
            String domainPart = trimmedEmail.substring(atIndex);
            return localPart + domainPart;
        }
        return trimmedEmail.replace(".", "");
    }

    public void newUser(String username, String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String normalizedUsername = username != null ? username.trim().toLowerCase() : "";
        String normalizedEmail = stopDuplicateEmail(email);
        String path = createPath(normalizedUsername);
        values.put(SQLiteUser.UserEntry.COLUMN_NAME_USERNAME, normalizedUsername);
        values.put(SQLiteUser.UserEntry.COLUMN_NAME_EMAIL, normalizedEmail);
        values.put(SQLiteUser.UserEntry.COLUMN_NAME_PASSWORD, password);
        values.put(SQLiteUser.UserEntry.COLUMN_NAME_PATH, path);
        db.insert(SQLiteUser.UserEntry.TABLE_NAME, null, values);
        db.close();
    }
    public String createPath(String username){
        File folder = new File(context.getFilesDir(), "user_csv_data");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String cleanUsername = username.replaceAll("[^a-zA-Z0-9_]", "_");
        String uniquePath = UUID.randomUUID().toString().substring(0, 8);
        String path = folder.getAbsolutePath() + File.separator + cleanUsername + "_" + uniquePath + ".csv";
        return path;
    }
    public void deleteUser(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = SQLiteUser.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = { username };
        db.delete(SQLiteUser.UserEntry.TABLE_NAME, selection, selectionArgs);
    }
    public String getFilePath(String username){
        if (username == null) return null;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { SQLiteUser.UserEntry.COLUMN_NAME_PATH };
        String selection = SQLiteUser.UserEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = { username };
        Cursor cursor = db.query(SQLiteUser.UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(SQLiteUser.UserEntry.COLUMN_NAME_PATH);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }
        return null;
    }
    public boolean checkUser(String input, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { SQLiteUser.UserEntry._ID };
        
        String trimmedInput = input != null ? input.trim().toLowerCase() : "";
        String normalizedInput = stopDuplicateEmail(input);

        String selection = "(" +
                "LOWER(" + SQLiteUser.UserEntry.COLUMN_NAME_EMAIL + ") = ? OR " +
                "LOWER(" + SQLiteUser.UserEntry.COLUMN_NAME_EMAIL + ") = ? OR " +
                "LOWER(" + SQLiteUser.UserEntry.COLUMN_NAME_USERNAME + ") = ?" +
                ") AND " + SQLiteUser.UserEntry.COLUMN_NAME_PASSWORD + " = ?";

        String[] selectionArgs = {
                normalizedInput,
                trimmedInput,
                trimmedInput,
                password
        };
        Cursor cursor = db.query(SQLiteUser.UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

}
