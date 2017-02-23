package com.example.chenqi.mobilphone.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.orhanobut.logger.Logger;

/**
 * Created by chenqi on 2017/2/12.
 * 描述:自定义Application类
 */
public class BaseApplication extends Application {

    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;    //上下文
    private static Thread mMainThread;  //主线程
    private static long mMainThreadId;  //主线程id
    private static Looper mMainLooper;  //循环队列
    private static Handler mHandler;    //主线程Handler

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("pipixia");
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mMainLooper = getMainLooper();
        mHandler = new Handler();
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        BaseApplication.mContext = mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        BaseApplication.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        BaseApplication.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainLooper(Looper mMainLooper) {
        BaseApplication.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        BaseApplication.mHandler = mHandler;
    }
}
