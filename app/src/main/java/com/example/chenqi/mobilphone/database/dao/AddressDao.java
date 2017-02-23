package com.example.chenqi.mobilphone.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

    public static String query(String phone) {
        String address = phone;
        //区分是否是手机号码
        if (phone.matches("^1[3587]\\d{9}$")) {
            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.example.chenqi.mobilphone/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("select  location from data2 where id=(select outkey from data1 where id=?)", new String[]{phone.substring(0, 7)});
            while (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            return address != null ? address : "";
        } else {
            switch (phone.length()) {
                case 3:
                    if ("110".equals(phone)) {
                        return "匪警";
                    } else if ("120".equals(phone)) {
                        return "急救";
                    } else if ("119".equals(phone)) {
                        return "火警";
                    }
                    break;
                case 4:
                    return "模拟器";
                case 5:
                    return "客服";
                case 7:
                    if (!phone.startsWith("0") && !phone.startsWith("1")) {
                        return "本地座机";
                    }
                case 8:
                    if (!phone.startsWith("0") && !phone.startsWith("1")) {
                        return "本地座机";
                    }
                default:
                    if (phone.startsWith("0") && phone.length() >= 10) {
                        SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.m520it.mobilsafe/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
                        Cursor cursor = db.rawQuery("select  location from data2 where area=?", new String[]{phone.substring(1, 3)});
                        while (cursor.moveToNext()) {
                            //说明有数据
                            address = cursor.getString(0);
                            return address;
                        }
                        cursor = db.rawQuery("select  location from data2 where area=?", new String[]{phone.substring(1, 4)});
                        while (cursor.moveToNext()) {
                            //说明有数据
                            address = cursor.getString(0);
                            return address;
                        }
                    }
            }
        }
        return address;
    }
}
