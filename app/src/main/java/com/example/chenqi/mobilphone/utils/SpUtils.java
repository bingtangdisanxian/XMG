package com.example.chenqi.mobilphone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.chenqi.mobilphone.config.Constans;


/**
 * 专门用来保存和获取SharedPreferences中的值
 */

public class SpUtils {
    //获取到SharedPreferences文件

    /**
     * 参数1:上下文-->用来获取SharedPreferences文件
     */
    private static SharedPreferences getSharedPreferences(Context context) {
        /**
         * 参数1:保存的xml文件的名称xx.xml
         * 参数2:文件的读取模式,一般为私有的
         */
        SharedPreferences sp = context.getSharedPreferences(Constans.CONFIG,
                Context.MODE_PRIVATE);
        return sp;
    }
    //保存boolean值的方法
    public static void putBoolean(Context context,String name ,boolean flag){
        SharedPreferences sp = getSharedPreferences(context);
        //获取编辑器
        SharedPreferences.Editor edit = sp.edit();
        //将传入的名称和Boolean值保存起来并提交
        edit.putBoolean(name,flag).apply();
    }

    //获取boolean值的方法
    public static boolean getBoolean(Context Context,String name){
        //获取SharedPreferences
        SharedPreferences sp = getSharedPreferences(Context);
        //返回根据保存的名称来获取boolean值
        return sp.getBoolean(name,false);
    }

    public static void putString(String password, String value,Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(password,value).apply();
    }

    public static String getString(String password,Context context) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(password,null);
    }

    //获得保持在Sp中的String
    public static int getInt(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key,0);
    }
    //保持一个String到sp
    public static void PutInt(Context context,String key,int value){
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putInt(key,value).commit();
    }
}
