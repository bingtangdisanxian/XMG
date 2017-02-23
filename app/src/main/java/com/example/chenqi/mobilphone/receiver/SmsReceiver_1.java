package com.example.chenqi.mobilphone.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.service.LocationService;

/**
 * 短信防盗的广播,根据获取的内容执行对应的操作
 */
public class SmsReceiver_1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取管理员权限
        DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //参数封装
        ComponentName content = new ComponentName(context, MyAdminReceiver.class);
        // 1 获得短信的内容
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        //  java 习惯一般情况下数组或者集合获得后一般需要遍历
        for (Object obj : objs) {
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) obj);
            String body = msg.getMessageBody();
            if ("*#location#*".equals(body)) {
                //需要获得GPS信息
                // 收到短信后应该开启服务
                Intent intentService = new Intent(context, LocationService.class);
                context.startService(intentService);
                //将短信截断传递 截断广播
                abortBroadcast();
            } else if ("*#alarm#*".equals(body)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                //重复播放
                mediaPlayer.setLooping(true);
                //设置音频
                mediaPlayer.setVolume(1.0f, 1.0f);
                //开启音频
                mediaPlayer.start();
                //将短信截断传递 截断广播
                abortBroadcast();
            } else if ("*#wipedata#*".equals(body)) {
                //清除手机所有数据
                // 判断是否有激活高级API
                if (dpm.isAdminActive(content)) {
                    dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                }
                abortBroadcast();
            } else if ("*#lockscreen#*".equals(body)) {
                //立刻锁屏
                // 判断是否有激活高级API
                if (dpm.isAdminActive(content)) {
                    dpm.lockNow();
                    dpm.resetPassword("123", 0);
                }
                abortBroadcast();
            }
        }
    }
}
