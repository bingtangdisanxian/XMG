package com.example.chenqi.mobilphone.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.chenqi.mobilphone.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2016/12/21.
 * 作用:通过包管理器获取app相关信息的工具类(静态的)
 */

public class AppInfoUtils {

    public static List<AppInfo> getAllInfo(Context context) {
        List<AppInfo> list = new ArrayList<AppInfo>();
        //通过上下文获取包管理器-->获取包-->遍历
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            AppInfo appInfo = new AppInfo();
//          SystemClock.sleep(100);//模拟延迟
            String packageName = info.packageName;
            //保存包名
            appInfo.setPackgeName(packageName);
            //查看清单配置文件-->图片是存储在application标签下-->获取图片-->设置图片
            Drawable icon = info.applicationInfo.loadIcon(manager);
            appInfo.setIcon(icon);
            //设置app名称
            String name = info.applicationInfo.loadLabel(manager).toString();
            appInfo.setName(name);
            //获取apk的全路径-->创建文件-->获取文件大小-->设置文件大小
            String path = info.applicationInfo.sourceDir;
            File file = new File(path);
            long size = file.length();
            appInfo.setSize(size);
            //获取应用的标签-->获取系统应用的标签-->比对(相同则为系统应用,不同则为用户应用)
            int flags = info.applicationInfo.flags;
            int flagSystem = ApplicationInfo.FLAG_SYSTEM;
            if ((flags & flagSystem) == 1) {
                appInfo.setUser(false);
            } else {
                appInfo.setUser(true);
            }
            int flagExternalStorage = ApplicationInfo.FLAG_EXTERNAL_STORAGE;
            if ((flags & flagExternalStorage) == 1) {
                appInfo.setSD(true);
            } else {
                appInfo.setSD(false);
            }
            list.add(appInfo);
        }
        return list;
    }
}
