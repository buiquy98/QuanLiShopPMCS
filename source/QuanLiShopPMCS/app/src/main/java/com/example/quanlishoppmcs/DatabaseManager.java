package com.example.quanlishoppmcs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shop_manager";

    private static Context contextt;

    //constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        Log.d("DBManager", "DBManager: ");
        this.contextt = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
