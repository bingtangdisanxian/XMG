package com.example.chenqi.mobilphone.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseSetupActivity;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.receiver.MyAdminReceiver;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:设置界面四
 */
public class GuardSetupActivity4 extends BaseSetupActivity {
    private CheckBox cb_open_protect;
    private TextView tv_open_close;
    private ComponentName mWho;
    private DevicePolicyManager mDpm;

    public void initView() {
        setContentView(R.layout.activity_setup_foruth);
        cb_open_protect = (CheckBox) findViewById(R.id.cb_open_protect);
        tv_open_close = (TextView) findViewById(R.id.tv_open_close);
        mDpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mWho = new ComponentName(this, MyAdminReceiver.class);
    }

    @Override
    public void initListener() {
        cb_open_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //激活高级管理员权限
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mWho);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"点我激活,不然罚款");
                    startActivity(intent);
                }else{
                    //取消高级管理员权限
                    mDpm.removeActiveAdmin(mWho);
                }
                tv_open_close.setText(isChecked?"防盗保护已经开启":"防盗保护已经关闭");
                SpUtils.putBoolean(GuardSetupActivity4.this, Constans.ISCHECKED,isChecked);
            }
        });
    }

    @Override
    public void initData() {
        boolean isCheck = SpUtils.getBoolean(GuardSetupActivity4.this, Constans.ISCHECKED);
        cb_open_protect.setChecked(isCheck);
    }

    @Override
    public void toPre() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity4.this, GuardSetupActivity3.class);
    }

    @Override
    public void toNext() {
        boolean checked = cb_open_protect.isChecked();
        if (checked){
            IntentUtils.startActivityAndFinish(GuardSetupActivity4.this, FunctionActivity1.class);
        }else {
            Toast.makeText(GuardSetupActivity4.this,"请先开启保护状态",Toast.LENGTH_SHORT).show();
        }
        SpUtils.putBoolean(GuardSetupActivity4.this,Constans.FINISHSETUP,true);
    }

    @Override
    public void initBack() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity4.this, GuardSetupActivity3.class);
    }
}
