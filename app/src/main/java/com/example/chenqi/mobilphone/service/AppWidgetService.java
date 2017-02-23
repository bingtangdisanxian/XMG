package com.example.chenqi.mobilphone.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.receiver.MyAppWidget;
import com.example.chenqi.mobilphone.utils.ProcessInfoUtils;

import java.util.Timer;
import java.util.TimerTask;

public class AppWidgetService extends Service {
    private TimerTask mTimerTask;
    private Timer mTimer;
    private AppWidgetManager awm;//Widget管理器,保证Widget和外部应用的通讯规则
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //初始化AppWidgetManager
        awm=AppWidgetManager.getInstance(this);
        mTimer=new Timer();
        mTimerTask=new TimerTask() {
            @Override
            public void run() {
                ComponentName provider=new ComponentName(AppWidgetService.this,MyAppWidget.class );
                RemoteViews views=new RemoteViews(getPackageName(), R.layout.widget_app_widget_provider);
                int runProcessCount = ProcessInfoUtils.getRunningProcess(AppWidgetService.this);
                long availMem = ProcessInfoUtils.getUsableMemory(AppWidgetService.this);
                String memStr = Formatter.formatFileSize(AppWidgetService.this, availMem);
                views.setTextViewText(R.id.process_count,"正在运行的进程:"+runProcessCount+"个");
                views.setTextViewText(R.id.process_memory,"可用内存:"+memStr);
                //开启意图 发送广播---自定义广播
                Intent intent=new Intent();
                intent.setAction("com.example.chenqi.mobilphone.CLEAN_PROCESS");
                PendingIntent pendingIntent=PendingIntent.getBroadcast(AppWidgetService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.btn_clear,pendingIntent);
                awm.updateAppWidget(provider,views);
            }
        };
        mTimer.schedule(mTimerTask,0,5000);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mTimer!=null&&mTimerTask!=null) {
            mTimer.cancel();
            mTimerTask.cancel();
            mTimerTask=null;
            mTimer=null;
        }
        super.onDestroy();
    }
}
