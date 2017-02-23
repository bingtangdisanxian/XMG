package com.example.chenqi.mobilphone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;


/**
 * 设置界面中能公用的自定义相对布局
 */

public class MyRelativeLayout extends RelativeLayout {

    private RelativeLayout mRl;
    private ImageView mIv;
    private TextView mTv;

    //方便在代码在创建该类的实例
    public MyRelativeLayout(Context context) {
        this(context,null);
    }
    //用来完成对属性的设置
    //AttributeSet:属性集
    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(isInEditMode()){
            return;
        }
        initView(context);
        //通过属性集来获取属性值
        /**
         * 参数1:xmlns
         * 参数2:属性名(attr name)
         */
        String mytext = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mytext");
        mTv.setText(mytext);
        String mybcakground = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mybackground");
        switch (Integer.valueOf(mybcakground)){
            case 0 :mRl.setBackgroundResource(R.drawable.setting_item_first);
                break;
            case 1 :mRl.setBackgroundResource(R.drawable.setting_item_middle);
                break;
            case 2 :mRl.setBackgroundResource(R.drawable.setting_item_last);
                break;
        }
    }
    //供外界调用来给不同状态下的图片设置值
    public void setImage(boolean flag){
        mIv.setImageResource(flag?R.drawable.on: R.drawable.off);
    }
    private void initView(Context context) {
        //创建一个公用的RelativeLayout布局并转化为一个View
        //this表示把这个公共的布局依附在MyRelativeLayout上
        View.inflate(context, R.layout.item_setting_public_rl, this);
        mRl = (RelativeLayout) findViewById(R.id.rl_root_public);
        mIv = (ImageView) findViewById(R.id.iv_off_on);
        mTv = (TextView) findViewById(R.id.tv_public);
    }
}
