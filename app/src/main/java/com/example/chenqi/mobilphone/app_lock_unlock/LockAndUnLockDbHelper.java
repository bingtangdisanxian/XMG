package com.example.chenqi.mobilphone.app_lock_unlock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:程序锁数据库的帮助类
 */
class LockAndUnLockDbHelper extends SQLiteOpenHelper {

    LockAndUnLockDbHelper(Context context) {
        super(context, "LockAndUnLock.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table LockApp(_id integer primary key autoincrement,appName text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
