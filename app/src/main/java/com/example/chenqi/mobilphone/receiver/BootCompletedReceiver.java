package com.example.chenqi.mobilphone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.SpUtils;

/**
 * Created by chenqi on 2016/12/20.
 * 作用:Android系统重启的广播
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    //只要手机一重启,这个方法就会被回调
    @Override
    public void onReceive(Context context, Intent intent) {
        // 1 检查是否开启了防盗保护
        boolean protocol = SpUtils.getBoolean(context, Constans.ISCHECKED);
        if(protocol) {
            // 2获得当前 设备的SIM卡串号
            TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            // 3 比对以前保持的SIM卡的串号
            String newSim = tm.getSimSerialNumber();
            String oldSim = SpUtils.getString(Constans.SIM,context);
            if(!newSim.equals(oldSim)) {
                //sim卡串号改变,不相等
                // 4 如果串号发生了改变,就给安全号码发动一条短信
                String phone = SpUtils.getString(Constans.safeNumber,context);
                SmsManager.getDefault().sendTextMessage(phone,null,"sim changer",null,null);
            }
        }
    }
}
