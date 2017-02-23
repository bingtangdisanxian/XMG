package com.example.chenqi.mobilphone.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chenqi.mobilphone.database.dbhlper.AppLockDbHelper;

/**
 * Created by chenqi on 2016/12/25.
 * 作用:
 */

public class AppLockDao {
    private AppLockDbHelper mHelper;
    private String table = "appLockInfo";

    public AppLockDao(Context context) {
        mHelper = new AppLockDbHelper(context);
    }
    //添加程序
    public boolean addApp(String name){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("appName",name);
        long insert = db.insert(table, null, values);
        db.close();
        return insert != -1;
    }
    //删除程序
    public  boolean delete(String name){
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete(table, "appName=?", new String[]{name});
        db.close();
        return delete!=0;
    }
    //查找程序
    public boolean findApp(String name){
        boolean flag ;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]{"appName"}, "appName=?", new String[]{name}, null, null, null);
        flag = cursor.moveToNext();
        cursor.close();
        db.close();
        return flag;
    }
}
