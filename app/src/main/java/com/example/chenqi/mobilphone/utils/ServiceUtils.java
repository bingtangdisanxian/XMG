package com.example.chenqi.mobilphone.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 动态的获得服务开启的状态
 */

public class ServiceUtils {
    public static boolean isServiceRunning(Context context, String name) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得所有正在运行的服务,一般不会超过100个
        List<ActivityManager.RunningServiceInfo> services = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo service : services) {
            String className = service.service.getClassName();
            if (name.equals(className)) {
                return true;
            }
        }
        return false;
    }
}
