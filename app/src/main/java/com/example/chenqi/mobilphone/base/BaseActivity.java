package com.example.chenqi.mobilphone.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by chenqi on 2016/12/19.
 * 作用:可以作为任何activity的基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    protected abstract void initView();

    protected void initData(){}

    protected void initListener(){}
}
