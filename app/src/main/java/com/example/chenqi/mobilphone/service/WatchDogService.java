package com.example.chenqi.mobilphone.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.chenqi.mobilphone.activity.AppLockEnterPassWordActivity;
import com.example.chenqi.mobilphone.database.dao.AppLockDao;

import java.util.List;

/**
 * Created by chenqi on 2016/12/26.
 * 作用:
 */

public class WatchDogService extends Service{

    private AppLockDao mDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mDao = new AppLockDao(this);
        new Thread(){
            @Override
            public void run() {
                while (true){
                    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);//参数:拿到正在运行任务的最大值
                    String packageName = infos.get(0).topActivity.getPackageName();//拿到栈顶的程序的包名
                    //去数据库中比对,看是否锁定
                    Log.v("android",packageName);
                    boolean isFind = mDao.findApp(packageName);
                    Log.v("android",""+isFind);
                    if (isFind){
                        //弹出输入密码的页面,在服务中开启一个activity需要flag
                        Intent intent = new Intent(WatchDogService.this, AppLockEnterPassWordActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else {

                    }
                    SystemClock.sleep(5000);
                }
            }
        }.start();
        super.onCreate();
        Log.v("android","开启服务");
    }

    @Override
    public void onDestroy() {
        Log.v("android","关闭服务");
        super.onDestroy();
    }
}
