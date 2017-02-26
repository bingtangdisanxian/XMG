package com.example.chenqi.mobilphone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.service.ShowLocationService;
import com.example.chenqi.mobilphone.service.SmsService;
import com.example.chenqi.mobilphone.service.WatchDogService;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.ServiceUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;
import com.example.chenqi.mobilphone.view.MyRelativeLayout;
import com.example.chenqi.mobilphone.view.SetRelativeBg;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:设置的界面
 */

public class SettingActivity extends BaseActivity {
    private MyRelativeLayout myrl_update;
    private MyRelativeLayout myrl_black;
    private MyRelativeLayout myrl_address;
    private MyRelativeLayout myrl_lock;
    private SetRelativeBg srb_color;//显示不同的归属地风格
    private String[] names = {"半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿"};

    public void initView() {
        setContentView(R.layout.activity_setting);
        myrl_update = (MyRelativeLayout) findViewById(R.id.myrl_update);
        myrl_black = (MyRelativeLayout) findViewById(R.id.myrl_black);
        myrl_address = (MyRelativeLayout) findViewById(R.id.myrl_address);
        myrl_lock = (MyRelativeLayout) findViewById(R.id.myrl_lock);
        srb_color = (SetRelativeBg) findViewById(R.id.srb_color);

    }

    @Override
    protected void initData() {
        boolean update = SpUtils.getBoolean(SettingActivity.this, Constans.AUTOUPDATE);
        myrl_update.setImage(update);
    }

    @Override
    protected void initListener() {
        myrl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获取原始状态的值
                boolean update = SpUtils.getBoolean(SettingActivity.this, Constans.AUTOUPDATE);
                //然后将值取反设置进去
                SpUtils.putBoolean(SettingActivity.this, Constans.AUTOUPDATE, !update);
                //根据不同的值设置不同的图片
                myrl_update.setImage(!update);
            }
        });

        myrl_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //需要从当前的手机状态去判断服务是否是开启的(而不是通过sp的保存值去判断)
                boolean serviceRunning = ServiceUtils.isServiceRunning(getApplicationContext(), "com.example.chenqi.mobilphone.service.SmsService");
                if (!serviceRunning) {
                    IntentUtils.startService(SettingActivity.this, SmsService.class);
                } else {
                    IntentUtils.stoptService(SettingActivity.this, SmsService.class);
                }
                myrl_black.setImage(!serviceRunning);
            }
        });

        myrl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean serviceRunnig = ServiceUtils.isServiceRunning(getApplicationContext(), "com.example.chenqi.mobilphone.service.ShowLocationService");
                if (serviceRunnig) {
                    //关闭服务
                    Intent service = new Intent(SettingActivity.this, ShowLocationService.class);
                    stopService(service);
                } else {
                    //打开服务
                    Intent service = new Intent(SettingActivity.this, ShowLocationService.class);
                    startService(service);
                }
                // 切换状态的图片效果
                myrl_address.setImage(!serviceRunnig);
            }
        });

        srb_color.setOnClickListener(new View.OnClickListener() {
            private AlertDialog mDialog;
            @Override
            public void onClick(View v) {
                //弹出一个对话框,给用户选择
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("归属地风格选择");
                /**
                 * 参数一 选择的文字的数组
                 * 参数二 是默认显示的第一文本
                 */
                builder.setSingleChoiceItems(names, SpUtils.getInt(getApplicationContext(), Constans.SWICH), new DialogInterface.OnClickListener() {
                    /**
                     *
                     * @param dialog
                     * @param which 代表点击的选择框中的某个位置
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtils.PutInt(getApplicationContext(), Constans.SWICH, which);
                        //点击后应该拿到当前的小的title的文本
                        String name = names[which];
                        srb_color.setBgColor(name);
                        mDialog.dismiss();
                    }
                });
                mDialog = builder.show();
            }
        });

        myrl_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean watchDog = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.chenqi.mobilphone.service.WatchDogService");
                if (watchDog) {
                    IntentUtils.stoptService(SettingActivity.this, WatchDogService.class);
                } else {
                    IntentUtils.startService(SettingActivity.this, WatchDogService.class);
                }
                myrl_lock.setImage(!watchDog);
            }
        });
    }

    @Override
    protected void onResume() {
        //1 获得状态值
        boolean serviceLocation = ServiceUtils.isServiceRunning(getApplicationContext(), "com.example.chenqi.mobilphone.service.ShowLocationService");
        //2根据状态值显示不同的图片
        myrl_address.setImage(serviceLocation);

        //弹出显示风格的背景回显
        int position = SpUtils.getInt(getApplicationContext(), Constans.SWICH);
        String name = names[position];
        srb_color.setBgColor(name);

        boolean watchDog = ServiceUtils.isServiceRunning(SettingActivity.this, "com.example.chenqi.mobilphone.service.WatchDogService");
        myrl_lock.setImage(watchDog);
        super.onResume();
    }
}
