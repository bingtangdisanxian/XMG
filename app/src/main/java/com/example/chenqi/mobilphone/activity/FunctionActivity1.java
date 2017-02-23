package com.example.chenqi.mobilphone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:手机防盗的功能页
 */
public class FunctionActivity1 extends Activity{
    private TextView tv_safeNumber;
    private ImageView iv_safe_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    public void initView() {
        setContentView(R.layout.activity_function_first);
        tv_safeNumber = (TextView) findViewById(R.id.tv_safeNumber);
        iv_safe_lock = (ImageView) findViewById(R.id.iv_safe_lock);
    }

    public void initData() {
        String safeNumber = SpUtils.getString(Constans.safeNumber, FunctionActivity1.this);
        tv_safeNumber.setText(safeNumber);
        //只有开启安全保护才能进入功能页面-->回显只会有锁住的状态
        boolean islocked = SpUtils.getBoolean(FunctionActivity1.this, Constans.ISCHECKED);
        iv_safe_lock.setImageResource(islocked?R.drawable.lock:R.drawable.unlock);
    }

    @Override
    public void onBackPressed() {
        IntentUtils.startActivityAndFinish(FunctionActivity1.this,HomeActivity.class);
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }

    public void next(View view) {
        IntentUtils.startActivityAndFinish(FunctionActivity1.this,GuardSetupActivity1.class);
        overridePendingTransition(R.anim.next_in,R.anim.next_out);
    }

}
