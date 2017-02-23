package com.example.chenqi.mobilphone.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 多表查询
 */

public class CommonPhoneDao {

    //放回最外层listView的个数
    public static int getGroupCount(SQLiteDatabase db) {
        int count = 0;
        Cursor cursor = db.rawQuery("select count(*) from classlist", null);
        cursor.moveToNext();
        count = cursor.getInt(0);
        return count;
    }


    /**
     * 根据传递过来的不同的外层listview的位置,查询每个位置的孩子个数
     *
     * @param groupPosition
     * @return 返回每个外层listview的孩子个数
     */
    public static int getChildrenCount(int groupPosition, SQLiteDatabase db) {
        int count = 0;
        int newgroupPosition = groupPosition + 1;
        String table = "table" + newgroupPosition;
        Cursor cursor = db.rawQuery("select count(*) from " + table, null);
        cursor.moveToNext();
        count = cursor.getInt(0);
        return count;
    }

    /**
     * 根据传递过来的最外层的listview的位置,得到每个位置的名称
     *
     * @param groupPosition
     * @return
     */
    public static String getGroupView(int groupPosition, SQLiteDatabase db) {
        String name = null;
        int newgroupPosition = groupPosition + 1;
        Cursor cursor = db.rawQuery("select name from classlist where idx=?", new String[]{String.valueOf(newgroupPosition)});
        cursor.moveToNext();
        name = cursor.getString(0);
        return name;
    }

    public static String getChildView(int groupPosition, int childPosition, SQLiteDatabase db) {
        String groupName = null;
        int newgroupPosition = groupPosition + 1;
        int newchildPosition = childPosition + 1;
        Cursor cursor = db.rawQuery("select name,number from table" + newgroupPosition + " where _id=?", new String[]{String.valueOf(newchildPosition)});
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String phone = cursor.getString(1);
            groupName = name + "\n" + phone;
        }
        return groupName;
    }
}
