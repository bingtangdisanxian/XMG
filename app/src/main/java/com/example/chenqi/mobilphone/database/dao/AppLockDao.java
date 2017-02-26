package com.example.chenqi.mobilphone.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.chenqi.mobilphone.database.dbhlper.AppLockDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2016/12/25.
 * 作用:操作程序锁数据库的增删改查
 */

public class AppLockDao {
    private AppLockDbHelper mHelper;
    private String table = "appLockInfo";
    private Context mContext;

    public AppLockDao(Context context) {
        mContext = context;
        mHelper = new AppLockDbHelper(context);
    }

    //添加程序
    public boolean addApp(String name) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("appName", name);
        long insert = db.insert(table, null, values);
        db.close();
        if(insert!=-1) {
            //增加以下代码就相当于成为了一个内容提供者---提供者方--notifyChange()起唤醒观察者的作用
            Uri uri=Uri.parse("content://com.example.chenqi.mobilphone/DATA_CHANGE");
            mContext.getContentResolver().notifyChange(uri,null);
            return true;
        }else {
            return false;
        }
    }

    //删除程序
    public boolean delete(String name) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delete = db.delete(table, "appName=?", new String[]{name});
        db.close();
        if(delete!=0) {
            //只要有数据删除成功一定会走到这里
            Uri uri=Uri.parse("content://com.example.chenqi.mobilphone/DATA_CHANGE");
            mContext.getContentResolver().notifyChange(uri,null);
            return true;
        }else {
            return false;
        }
    }

    //查找指定程序
    public boolean findApp(String name) {
        boolean flag;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]{"appName"}, "appName=?", new String[]{name}, null, null, null);
        flag = cursor.moveToNext();
        cursor.close();
        db.close();
        return flag;
    }

    //查找所有程序
    public List<String> findAll(){
        List<String> lists = new ArrayList<>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(table, new String[]{"appName"}, null, null, null, null, null);
        while (cursor.moveToNext()){
            String packageName = cursor.getString(cursor.getColumnIndex("appName"));
            lists.add(packageName);
        }
        return lists;
    }
}
