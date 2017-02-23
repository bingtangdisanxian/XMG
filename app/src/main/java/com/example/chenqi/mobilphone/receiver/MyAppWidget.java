package com.example.chenqi.mobilphone.receiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.example.chenqi.mobilphone.service.AppWidgetService;
import com.orhanobut.logger.Logger;

/**
 * Created by chenqi on 2016/12/24.
 * 作用:桌面小部件--间接继承于广播接收者
 */

public class MyAppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.v("onReceive");
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        Logger.v("onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Logger.v("onUpdate");
        // 完成初始化操作
        Intent intent=new Intent(context, AppWidgetService.class);
        context.startService(intent);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Logger.v("onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Logger.v("onDisabled");
        //做结尾的操作
        Intent intent=new Intent(context, AppWidgetService.class);
        context.stopService(intent);
        super.onDisabled(context);
    }
}
