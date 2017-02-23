package com.example.chenqi.mobilphone.receiver;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.example.chenqi.mobilphone.database.dao.BlackNumberDao;
import com.example.chenqi.mobilphone.service.SmsService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by chenqi on 2017/2/16.
 * 描述:电话状态的监听器
 */
public class MyPhoneStateListener extends PhoneStateListener {

    private final BlackNumberDao mDao;
    private Context mContext;

    public MyPhoneStateListener(Context context) {
        mContext = context;
        mDao = new BlackNumberDao(context);
    }

    @Override
    public void onCallStateChanged(int state, final String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //空闲
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //接通
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //响铃
                String mode = mDao.findBlackNumber(incomingNumber);
                //判断拦截模式
                if ("1".equals(mode) || "2".equals(mode)) {
                    //拦截打进来的号码
                    //    mTm.endCall();
                    endCall();
                    //删除通话记录,通过内容观察者观察数据库的变化,只要发生了变化,这个事件就会被发现
                    Uri uri = Uri.parse("content://call_log/calls");
                    mContext.getContentResolver().registerContentObserver(uri, true, new ContentObserver(new Handler()) {
                        //观察的uri 内容发生了改变,这个方法就会被回调
                        @Override
                        public void onChange(boolean selfChange) {
                            deleteCallLog(incomingNumber);
                            super.onChange(selfChange);
                        }
                    });
                }
                break;
        }
    }

    //删除通话记录的方法
    private void deleteCallLog(String phone) {
        ContentResolver resolver = mContext.getContentResolver();
        Uri uri=Uri.parse("content://call_log/calls");
        resolver.delete(uri,"number=?",new String[]{phone});

    }

    private void endCall() {
        //IBinder b = ServiceManager.getService(ACCOUNT_SERVICE);
        //IAccountManager service = IAccountManager.Stub.asInterface(b);
        // 1 先获得ServiceManager
        try {
            // 得到类的字节码
            Class<?> clazz = SmsService.class.getClassLoader().loadClass("android.os.ServiceManager");
            //通过类的字节码得到方法
            Method method = clazz.getDeclaredMethod("getService", String.class);//第二个参数为getService(String str)的参数类型
            //调用这个方法
            IBinder b = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(b);
            iTelephony.endCall();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
