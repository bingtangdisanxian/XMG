package com.example.chenqi.mobilphone.database.dbhlper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chenqi on 2016/12/20.
 * 作用:创建黑名单数据库的帮助类
 */

public class BlackNumberDbHelper extends SQLiteOpenHelper {
    /**
     *  context:上下文
     *  name:数据库名
     *  factory:游标(null为使用默认的)
     *  version:1(表示版本从1开始)
     * 为了增强代码可读性,后三个参数可写死传递给父类
     */
    public BlackNumberDbHelper(Context context) {
        super(context, "blackNumber.db ", null, 1);
    }
    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * _id:主键自增长
         * phone:拦截的号码
         * mode:拦截的模式:电话,短信,全部
         */
        db.execSQL("create table blackNumberInfo(_id integer primary key autoincrement,phone varchar(20),mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
