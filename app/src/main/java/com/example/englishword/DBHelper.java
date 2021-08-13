package com.example.englishword;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "language_word.db";

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tableName ( english TEXT, korean TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tableName");
        onCreate(db);
    }
}
