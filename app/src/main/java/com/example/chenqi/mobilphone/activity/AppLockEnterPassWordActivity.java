package com.example.chenqi.mobilphone.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.utils.ToastUtils;

/**
 * Created by chenqi on 2016/12/26.
 * 作用:输入密码的对话框界面
 */

public class AppLockEnterPassWordActivity extends BaseActivity {

    private ImageView iv_enter_icon;
    private TextView tv_enter_name;
    private EditText et_enter_password;
    private Button btn_ok;
    private String mPackageName;

    public void initView() {
        setContentView(R.layout.activity_app_lock_enter_password);
        iv_enter_icon = (ImageView) findViewById(R.id.iv_enter_icon);
        tv_enter_name = (TextView) findViewById(R.id.tv_enter_name);
        et_enter_password = (EditText) findViewById(R.id.et_enter_password);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    public void initData() {
        Intent intent = getIntent();
        mPackageName = intent.getStringExtra("packageName");
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(mPackageName, 0);
            Drawable icon = info.applicationInfo.loadIcon(pm);
            iv_enter_icon.setImageDrawable(icon);
            String name = info.applicationInfo.loadLabel(pm).toString();
            tv_enter_name.setText(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initListener() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1获得输入框的文本
                String password = et_enter_password.getText().toString().trim();
                // 2 非空判断
                if (TextUtils.isEmpty(password)) {
                    et_enter_password.setError("请输入密码");
                } else {
                    // 3比对输入是否正确
                    if ("123".equals(password)) {
                        Intent intent = new Intent();
                        intent.setAction("content://com.example.chenqi.mobilphone/ALLOW");
                        intent.putExtra("packageName", mPackageName);
                        //发送广播
                        sendBroadcast(intent);
                        finish();
                    } else {
                        ToastUtils.showShort("密码错误");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //回退键直接返回桌面
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
