package com.example.chenqi.mobilphone.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.example.chenqi.mobilphone.receiver.MyPhoneStateListener;
import com.example.chenqi.mobilphone.receiver.SmsReceiver_2;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:在此服务中注册广播
 * 静态注册的广播不管样都能接收广播
 * 在服务中动态注册的广播可以在后台中长期存在而且可以手动关闭
 */

public class SmsService extends Service {

    private SmsReceiver_2 mReceiver2;
    private TelephonyManager mTm;
    private MyPhoneStateListener mListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //动态注册接收短信的广播
        registerSmsReceiver();
        interceptPhone();
    }

    private void registerSmsReceiver() {
        mReceiver2 = new SmsReceiver_2();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mReceiver2, filter);
    }

    private void interceptPhone() {
        //得到电话管理器
        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        /**
         * 参数1 电话打进来的监听器
         * 参数2 需要监听的状态状态
         */
        mListener = new MyPhoneStateListener(this);
        mTm.listen(mListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭服务时注销广播
        unregisterReceiver(mReceiver2);
        mReceiver2 = null;
        //关闭服务时取消电话状态的监听
        mTm.listen(mListener,PhoneStateListener.LISTEN_NONE);
        mListener=null;
    }
}
