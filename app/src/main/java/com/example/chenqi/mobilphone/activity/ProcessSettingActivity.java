package com.example.chenqi.mobilphone.activity;

import android.view.View;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.service.ScreenLockService;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.ServiceUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;
import com.example.chenqi.mobilphone.view.MyRelativeLayout;

/**
 * Created by chenqi on 2016/12/24.
 * 作用:进程相关功能的设置界面
 */

public class ProcessSettingActivity extends BaseActivity {
    private MyRelativeLayout myrl_show_sys,myrl_lock_clean;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_process_set);
        myrl_show_sys = (MyRelativeLayout) findViewById(R.id.myrl_show_sys);
        myrl_lock_clean = (MyRelativeLayout) findViewById(R.id.myrl_lock_clean);
    }

    @Override
    protected void initData() {
        //显示系统进程的状态的回显
        boolean showsys = SpUtils.getBoolean(ProcessSettingActivity.this, Constans.SHOWSYS);
        myrl_show_sys.setImage(showsys);
    }

    @Override
    protected void initListener() {
        myrl_show_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean showsys = SpUtils.getBoolean(ProcessSettingActivity.this, Constans.SHOWSYS);
                myrl_show_sys.setImage(!showsys);
                SpUtils.putBoolean(ProcessSettingActivity.this,Constans.SHOWSYS,!showsys);
            }
        });
        myrl_lock_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serviceRunning = ServiceUtils.isServiceRunning(ProcessSettingActivity.this, "com.example.chenqi.mobilphone.serveice.ScreenLockService");
                if (serviceRunning){
                    IntentUtils.stoptService(ProcessSettingActivity.this, ScreenLockService.class);
                }else {
                    IntentUtils.startService(ProcessSettingActivity.this,ScreenLockService.class);
                }
                myrl_lock_clean.setImage(!serviceRunning);
            }
        });
    }

    @Override
    protected void onStart() {
        //是否开启了锁屏服务的状态回显
        boolean serviceRunning = ServiceUtils.isServiceRunning(ProcessSettingActivity.this, "com.example.chenqi.mobilphone.serveice.ScreenLockService");
        myrl_lock_clean.setImage(serviceRunning);
        super.onStart();
    }
}


