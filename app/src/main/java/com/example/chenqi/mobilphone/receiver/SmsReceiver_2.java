package com.example.chenqi.mobilphone.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.example.chenqi.mobilphone.database.dao.BlackNumberDao;

/**
 * Created by chenqi on 2017/2/16.
 * 描述:短信拦截的广播,拦截黑名单所发送的短信
 */
public class SmsReceiver_2 extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        BlackNumberDao blackNumberDao = new BlackNumberDao(context);
        // 拦截黑名单的短信 先得到号码判断是否是黑明
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            // 得到号码的前提是先得到发送过来的短信
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            // 得到发动过来的短信后,得到发送的号码
            String phone = sms.getOriginatingAddress();
            String body = sms.getMessageBody();
            // 根据号码查询黑名单数据库
            String mode = blackNumberDao.findBlackNumber(phone);
            // 如果是一个短信拦截的mode,就拦截短信
            if("0".equals(mode)||"2".equals(mode)) {
                //拦截短信
                abortBroadcast();
                return;
            }
            //是否包含了某些关键字 分词算法
            if(body.contains("fapiao")) {
                abortBroadcast();
                return;
            }
        }
    }
}
