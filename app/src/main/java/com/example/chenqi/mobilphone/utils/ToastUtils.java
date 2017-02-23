package com.example.chenqi.mobilphone.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.example.chenqi.mobilphone.base.BaseApplication;

/**
 * Created by chenqi on 2017/2/12.
 * 描述:弹出吐司的工具类
 */
public class ToastUtils {

    private static Context mContext = BaseApplication.getContext();

    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static void check() {
        if (mContext == null) {
            //必须先去你的清单文件中注册你的BaseApplication
            throw new NullPointerException("Must initial call ToastUtils.register(Context context) in your " + "<? " + "extends Application class>");
        }
    }

    public static void showShort(String message) {
        check();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String message) {
        check();
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void showToastSafe(String message) {
        check();
        Looper.prepare();
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
