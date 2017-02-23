package com.example.chenqi.mobilphone.app_lock_unlock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:程序锁数据库的操作层
 */
public class LockAndUnLockDao {

    private final SQLiteDatabase mDb;
    private String table = "LockApp";

    public LockAndUnLockDao(Context context) {
        LockAndUnLockDbHelper helper = new LockAndUnLockDbHelper(context);
        mDb = helper.getWritableDatabase();
    }

    public boolean insert(String name) {
        ContentValues values = new ContentValues();
        values.put("appName", name);
        long insert = mDb.insert(table, null, values);
        return insert != 0;
    }

    public boolean delete(String name) {
        int delete = mDb.delete(table, "appName=?", new String[]{name});
        return delete != 0;
    }

    //查找是否有XX程序
    public boolean search(String name) {
        Cursor cursor = mDb.query(table, new String[]{"appName"}, "name=?", new String[]{name}, null, null, null);
        cursor.close();
        return cursor.moveToNext();
    }
}
