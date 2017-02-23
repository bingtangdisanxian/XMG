package com.example.chenqi.mobilphone.database.dbhlper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenqi on 2016/12/25.
 * 作用:
 */

public class AppLockDbHelper extends SQLiteOpenHelper {

    public AppLockDbHelper(Context context) {
        super(context, "AppLock.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table appLockInfo(_id integer primary key autoincrement,appName varchar(20))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
