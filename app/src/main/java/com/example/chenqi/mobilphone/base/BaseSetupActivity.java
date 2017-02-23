package com.example.chenqi.mobilphone.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.chenqi.mobilphone.R;

/**
 * Created by chenqi on 2016/12/19.
 * 作用:
 */

public abstract class BaseSetupActivity extends Activity {
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //钩子方法
        //父类帮子类实现onCreate()-->回调抽象的initView(),因为子类需要设置自己各自的布局
        initView();
        initData();
        initListener();
        //在基类中实现滑动跳转事件-->创建一个手指事件识别器
        /**
         * e1:手指碰到屏幕的事件
         * e2:手指离开屏幕的事件
         * velocityX:x轴方向的分量
         */
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX()-e2.getRawX()>0){
                    toNext();
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                }
                if (e1.getRawX()-e2.getRawX()<0){
                    toPre();
                    overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
                }
                //当两种条件都不成立时,由父类来完成
                return super.onFling(e1,e2,velocityX,velocityY);
            }
        });
    }

    //屏幕触摸事件接收器
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过手指事件识别器把事件传递给屏幕
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void next(View view){
        toNext();
        overridePendingTransition(R.anim.next_in,R.anim.next_out);
    }

    public void pre(View view){
        toPre();
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }
    public abstract void initView();

    public abstract void initListener();

    public abstract void initData();

    public abstract void toNext();

    public abstract void toPre();

    public abstract void initBack();

    @Override
    public void onBackPressed() {
        initBack();
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }
}
