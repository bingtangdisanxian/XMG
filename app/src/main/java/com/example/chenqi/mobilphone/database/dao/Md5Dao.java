package com.example.chenqi.mobilphone.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chenqi.mobilphone.bean.UpdateInfoBean;

/**
 * Created by chenqi on 2017/2/24.
 * 描述:操作病毒数据库
 */
public class Md5Dao {

    public static String query(String md5) {
        String table = "datable";
        String desc = null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chenqi.mobilphone/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.query(table, new String[]{"desc"}, "md5=?", new String[]{md5}, null, null, null);
        while (cursor.moveToNext()) {
            desc = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }

    public static int getVersionCode() {
        int version = 0;
        String table = "version";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chenqi.mobilphone/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.query(table, new String[]{"subcnt"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            version = cursor.getInt(0);
        }
        return version;
    }

    public static boolean  updateVersionCode(String subcnt) {
        String table = "version";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chenqi.mobilphone/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values=new ContentValues();
        values.put("subcnt",subcnt);
        int update = db.update(table, values,null,null);
        if(update!=0) {
            return true;
        }else {
            return false;
        }

    }

    public static boolean insertMd5(UpdateInfoBean updataBean) {
        String table = "datable";
        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chenqi.mobilphone/files/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues values = new ContentValues();
        values.put("md5", updataBean.md5);
        values.put("type", updataBean.type);
        values.put("name", updataBean.name);
        values.put("desc", updataBean.desc);
        long insert = db.insert(table, null, values);
        if(insert!=-1) {
            return true;
        }else {
            return false;
        }
    }
}
