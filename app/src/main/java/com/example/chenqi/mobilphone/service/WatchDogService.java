package com.example.chenqi.mobilphone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.chenqi.mobilphone.activity.AppLockEnterPassWordActivity;
import com.example.chenqi.mobilphone.database.dao.AppLockDao;
import com.example.chenqi.mobilphone.utils.TopActivityUtils;

import java.util.List;

/**
 * Created by chenqi on 2016/12/26.
 * 作用:看门狗服务
 */

public class WatchDogService extends Service {

    private AppLockDao mLockDao;
    private InnerStopWatchDogReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private String stopName;
    private boolean flag;
    private String mPackageName;
    private Intent mIntent;
    private List<String> mLists;
    private InnerContentObserver mObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        register();
        mLockDao = new AppLockDao(this);
        mLists = mLockDao.findAll();
        fillStart();
        super.onCreate();
    }

    private void register() {
        //注册一个内容观察者--内容观察者方
        mObserver = new InnerContentObserver(new Handler());
        Uri uri = Uri.parse("content://com.example.chenqi.mobilphone/DATA_CHANGE");
        getContentResolver().registerContentObserver(uri, true, mObserver);//第二个参数:是否开启模糊匹配
        //注册密码通过的广播(停止看门狗服务)
        mReceiver = new InnerStopWatchDogReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("content://com.example.chenqi.mobilphone/ALLOW");
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mReceiver, mIntentFilter);
    }

    //内容观察者
    class InnerContentObserver extends ContentObserver {
        InnerContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            //当内容提供者方数据发生了改变
            mLists = mLockDao.findAll();
            super.onChange(selfChange);
        }
    }

    class InnerStopWatchDogReceiver extends BroadcastReceiver {
        //接收到密码输入正确后所发出的广播时回调
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("content://com.example.chenqi.mobilphone/ALLOW".equals(action)) {
                // 得到传递过来的数据
                stopName = intent.getStringExtra("packageName");
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                //停止保护
                stopName = null;
                flag = false;
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {
                //继续保护
                flag = true;
                fillStart();
            }
        }
    }

    private void fillStart() {
        new Thread() {
            public void run() {
                watch();
            }
        }.start();
    }

    private void watch() {
        mIntent = new Intent(WatchDogService.this, AppLockEnterPassWordActivity.class);
        while (!flag) {
            //一直监视某个动作
            try {
                mPackageName = TopActivityUtils.getTopActivity(getApplicationContext());
                // 1 通过包名查询加锁的数据库
                if (mLists.contains(mPackageName)) {
                    // 2如果查询到了这个app是加锁-->弹出输入密码界面
                    if (!TextUtils.isEmpty(stopName) && stopName.equals(mPackageName)) {

                    } else {
                        //传递包名
                        mIntent.putExtra("packageName", mPackageName);
                        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mIntent);
                    }
                }
                SystemClock.sleep(100);//CPU休息间隔
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        getContentResolver().unregisterContentObserver(mObserver);
        mReceiver = null;
        mObserver = null;
        super.onDestroy();
    }
}
