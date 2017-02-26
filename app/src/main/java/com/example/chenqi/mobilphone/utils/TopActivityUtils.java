package com.example.chenqi.mobilphone.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by chenqi on 2017/2/24.
 * 描述:两种方式分别分别获取安卓5.0以下和5.0以上的topActivity的包名的工具类
 */
public class TopActivityUtils {

    public static String getTopActivity(Context context) {
        if (context == null) {
            return null;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT <= 20) {
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (tasks != null && !tasks.isEmpty()) {
                ComponentName componentName = tasks.get(0).topActivity;
                if (componentName != null) {
                    return componentName.getClassName();
                }
            }
        } else {
            ActivityManager.RunningAppProcessInfo currentInfo = null;
            Field field;
            int START_TASK_TO_FRONT = 2;
            String pkgName = null;
            try {
                field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            } catch (Exception e) {
                return null;
            }
            List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
            if (appList == null || appList.isEmpty()) {
                return null;
            }
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app != null && app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Integer state;
                    try {
                        state = field.getInt(app);
                    } catch (Exception e) {
                        return null;
                    }
                    if (state != null && state == START_TASK_TO_FRONT) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            if (currentInfo != null) {
                pkgName = currentInfo.processName;
            }
            return pkgName;
        }
        return null;
    }
}
