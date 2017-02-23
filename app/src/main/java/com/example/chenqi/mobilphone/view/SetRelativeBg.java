package com.example.chenqi.mobilphone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;

public class SetRelativeBg extends RelativeLayout {

    private TextView mTv_big_title;
    private TextView mTv_small_title;

    public SetRelativeBg(Context context) {
        this(context, null);
    }

    public SetRelativeBg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(attrs);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.view_rl_bg, this);
        mTv_big_title = (TextView) findViewById(R.id.tv_big_title);
        mTv_small_title = (TextView) findViewById(R.id.tv_small_title);
    }

    private void initData(AttributeSet attrs) {
        //获取在xml中设置给属性的值
        String mybig_title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mybig_title");
        String mysmall_title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "mysmall_title");
        mTv_big_title.setText(mybig_title);
        mTv_small_title.setText(mysmall_title);
    }

    public void setBgColor(String text) {
        mTv_small_title.setText(text);
    }
}
