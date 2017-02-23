package com.example.chenqi.mobilphone.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chenqi on 2016/12/24.
 * 作用:锁屏清理进程的服务+广播
 */

public class ScreenLockService extends Service {

    private ScreenLockReceiver mReceiver;
    private TimerTask mTask;
    private Timer mTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //服务开启时注册广播
        mReceiver = new ScreenLockReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver,intentFilter);
        //演示定时器逻辑-->创建一个任务-->创建一个定时器
        mTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTask,5000,5000);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //服务结束时注销广播
        unregisterReceiver(mReceiver);
        mReceiver = null;
        mTimer.cancel();
        super.onDestroy();
    }

    class ScreenLockReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //屏幕锁住时清理所有进程
            ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
                manager.killBackgroundProcesses(runningAppProcess.processName);
            }
        }
    }
}
