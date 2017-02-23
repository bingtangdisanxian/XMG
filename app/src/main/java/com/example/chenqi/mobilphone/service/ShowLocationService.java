package com.example.chenqi.mobilphone.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.database.dao.AddressDao;
import com.example.chenqi.mobilphone.utils.SpUtils;

public class ShowLocationService extends Service {

    private TelephonyManager mTm;
    private MyListener mMyListener;
    private InnerCallOutReceiver mReceiver;
    private WindowManager mMWM;
    private View mView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mReceiver = new InnerCallOutReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(mReceiver, filter);

        mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mMyListener = new MyListener();
        mTm.listen(mMyListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    class MyListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE://空闲状态
                    dissmissToast();

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://接通

                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    String location = AddressDao.query(incomingNumber);
                    ShowMyToast(location);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    class InnerCallOutReceiver extends BroadcastReceiver {
        //只要电话打进来,下面这个方法回调
        @Override
        public void onReceive(Context context, Intent intent) {
            String phone = getResultData();
            String address = AddressDao.query(phone);
            ShowMyToast(address);
        }
    }

    private void ShowMyToast(String location) {
        mMWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        params.gravity = Gravity.LEFT + Gravity.TOP;
        params.x = SpUtils.getInt(getApplicationContext(), Constans.X);
        params.y = SpUtils.getInt(getApplicationContext(), Constans.Y);
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mView = View.inflate(getApplicationContext(), R.layout.view_toast, null);
        LinearLayout ll_bg = (LinearLayout) mView.findViewById(R.id.ll_toast_bg);
        int[] bgs = new int[]{
                R.drawable.call_locate_white,
                R.drawable.call_locate_orange,
                R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,
                R.drawable.call_locate_green};
        int which = SpUtils.getInt(getApplicationContext(), Constans.SWICH);
        ll_bg.setBackgroundResource(bgs[which]);
        final TextView tv = (TextView) mView.findViewById(R.id.tv_toast_address);
        tv.setText(location);

        mView.setOnTouchListener(new View.OnTouchListener() {
            private float mStartY;
            private float mStartX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 1 得到开始坐标
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //2 得到移动后的坐标
                        float newX = event.getRawX();
                        float newY = event.getRawY();
                        // 3 得到差值
                        int dx = (int) (newX - mStartX);
                        int dy = (int) (newY - mStartY);
                        // 4 布局开始移动
                        // mView.layout(mView.getLeft()+dx);这个方法只能在activity中使用
                        params.x += dx;
                        params.y += dy;
                        //保证x,y不能超出屏幕
                        if (params.x < 0)
                            params.x = 0;
                        if (params.y < 0)
                            params.y = 0;
                        if (params.x > mMWM.getDefaultDisplay().getWidth() - mView.getWidth()) {
                            params.x = mMWM.getDefaultDisplay().getWidth() - mView.getWidth();
                        }
                        if (params.y > mMWM.getDefaultDisplay().getHeight() - mView.getHeight()) {
                            params.y = mMWM.getDefaultDisplay().getHeight() - mView.getHeight();
                        }
                        //5 初始化控件的开始位置
                        mStartX = event.getRawX();
                        mStartY = event.getRawY();
                        //6 通知窗体管理器位置更新
                        mMWM.updateViewLayout(mView, params);
                        break;
                    case MotionEvent.ACTION_UP:
                        //手指抬起来的时候应该保持当前控件显示的坐标
                        SpUtils.PutInt(getApplicationContext(), Constans.X, params.x);
                        SpUtils.PutInt(getApplicationContext(), Constans.Y, params.y);
                        break;
                }
                return true;
            }
        });
        mMWM.addView(mView, params);
    }

    @Override
    public void onDestroy() {
        mTm.listen(mMyListener, PhoneStateListener.LISTEN_NONE);
        mTm = null;
        unregisterReceiver(mReceiver);
        dissmissToast();
        super.onDestroy();
    }

    public void dissmissToast() {
        if (mView != null && mMWM != null) {
            mMWM.removeView(mView);
            mMWM = null;
        }
    }
}
