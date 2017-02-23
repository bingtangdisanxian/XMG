package com.example.chenqi.mobilphone.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chenqi.mobilphone.bean.BlackNumberInfo;
import com.example.chenqi.mobilphone.database.dbhlper.BlackNumberDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2016/12/20.
 * 作用:操作数据库的结构层
 */

public class BlackNumberDao {

    private final BlackNumberDbHelper mHelper;
    private String table = "blackNumberInfo";

    public BlackNumberDao(Context context) {
        //在构造dao对象的同时初始化helper对象
        mHelper = new BlackNumberDbHelper(context);
    }

    public boolean addBlackNumber(String phoneNumber, String mode) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phoneNumber);
        values.put("mode", mode);
        //表名,游标工厂(默认设为null),contentValues(map集合)
        long insert = db.insert(table, null, values);
        db.close();
        return insert != -1;
    }

    public boolean deleteBlackNumber(String phoneNumber) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        // 表名,要删除的列,删除列的绑定值
        int delete = db.delete(table, "phone=?", new String[]{phoneNumber});
        db.close();
        return delete != 0;
    }

    public boolean updateMode(String phoneNumber, String mode) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        //表名,contentValues(map集合),修改条件,条件绑定值
        int update = db.update(table, values, "phone=?", new String[]{phoneNumber});
        db.close();
        return update != 0;
    }

    public String findBlackNumber(String phoneNumber) {
        String mode = null;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        //表名,查询的列(不写表示所有),条件,条件绑定值,分组条件,绑定值,排序方式
        Cursor cursor = db.query(table, new String[]{mode}, "phone=?", new String[]{phoneNumber}, null, null, null);
        while (cursor.moveToNext()) {
            mode = cursor.getString(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;//如果返回null则说明黑名单号码
    }

    //查询所有
    public List<BlackNumberInfo> findAll() {
        List<BlackNumberInfo> list = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(table, new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            BlackNumberInfo bean = new BlackNumberInfo();
            String phone = cursor.getString(0);
            bean.setPhone(phone);
            String mode = cursor.getString(1);
            bean.setMode(mode);
            list.add(bean);
        }
        cursor.close();
        db.close();
        return list;
    }

    /**
     * @param start 从哪里开始查询
     * @param count 每次查询多少个
     * @return
     */
    public List<BlackNumberInfo> findPart(int start, int count) {
        List<BlackNumberInfo> lists = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode,phone from blackNumberInfo order by _id desc limit ? offset ?", new String[]{String.valueOf(count), String.valueOf(start)});
        while (cursor.moveToNext()) {
            BlackNumberInfo bean = new BlackNumberInfo();
            bean.setMode(cursor.getString(cursor.getColumnIndex("mode")));
            bean.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            lists.add(bean);
        }
        cursor.close();
        db.close();
        return lists;
    }

    //查询联系人的总条数
    public int getCount(){
        int count=0;
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blackNumberInfo", null);
        while (cursor.moveToNext()){
            count= cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
}
