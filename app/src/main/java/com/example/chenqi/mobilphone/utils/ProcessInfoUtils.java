package com.example.chenqi.mobilphone.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2016/12/22.
 * 作用:通过activity管理器获取进程的管理信息(动态的)
 */

public class ProcessInfoUtils {
    //获取当前设备的进程个数
    public static int getRunningProcess(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> rap = manager.getRunningAppProcesses();
        return rap.size();
    }

    //得到可用的剩余内存
    public static long getUsableMemory(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        long availMem = memoryInfo.availMem;
        return availMem;
    }

    //得到总内存
    public static long getTotalMemory(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        long availMem = memoryInfo.totalMem;
        return availMem;
    }

    //获取所有的进程信息
    //获取activity管理器-->获取进程-->遍历进程-->获取进程名(包名)
    //获取package管理器-->
    public static List<ProcessInfo> getAllProcessInfo(Context context) {
        List<ProcessInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> rap = am.getRunningAppProcesses();//获取进程
        for (ActivityManager.RunningAppProcessInfo process : rap) {
            ProcessInfo processInfo = new ProcessInfo();
            String packageName = process.processName;
            processInfo.setPackgeName(packageName);
            //内存是每个进程都有的所以在try外面设置
            long memSize = am.getProcessMemoryInfo(new int[]{process.pid})[0].getTotalPrivateDirty() * 1024;
            processInfo.setSize(memSize);
            try {
                //packageInfo:是包含了一些信息的基类
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                //ApplicationInfo是从一个特定的应用得到的信息。这些信息是从相对应的Androdimanifest.xml的<application>标签中收集到的
                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                processInfo.setName(appName);
                Drawable icon = packageInfo.applicationInfo.loadIcon(pm);
                processInfo.setIcon(icon);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //系统进程
                    processInfo.setUser(false);
                } else {
                    //用户进程
                    processInfo.setUser(true);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                //有些进程会信息不全,这时获取packageInfo就会有异常,所以需要设置一些默认值
                processInfo.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));
                processInfo.setName(packageName);
            }
            list.add(processInfo);
        }
        return list;
    }
}
