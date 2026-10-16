package com.idk.finnanceapp;

import static com.idk.finnanceapp.SQLiteUser.SQL_CREATE_ENTRIES;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteUserHelperDB extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public static final String DATABASE_NAME = "SQLUser.db";

    public SQLiteUserHelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
