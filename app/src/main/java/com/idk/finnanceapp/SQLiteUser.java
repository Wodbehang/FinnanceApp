package com.idk.finnanceapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class SQLiteUser {
    //TODO implement table with usernames passwords paths and emails.

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";

        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PATH = "path";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserEntry.COLUMN_NAME_USERNAME + " TEXT NOT NULL," +
                    UserEntry.COLUMN_NAME_PATH + " TEXT," +
                    UserEntry.COLUMN_NAME_PASSWORD + " TEXT NOT NULL," +
                    UserEntry.COLUMN_NAME_EMAIL + " TEXT NOT NULL UNIQUE)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;


}
