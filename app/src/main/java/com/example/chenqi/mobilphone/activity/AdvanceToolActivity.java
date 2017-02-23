package com.example.chenqi.mobilphone.activity;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.app_lock_unlock.recycler.RecyclerActivity;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.database.dao.AddressDao;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SmsUtils;
import com.example.chenqi.mobilphone.utils.ToastUtils;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenqi on 2016/12/25.
 * 作用:高级工具界面
 */

public class AdvanceToolActivity extends BaseActivity {
    @Bind(R.id.btn_advance_tool)
    Button mBtnAdvanceTool;
    @Bind(R.id.btn_query)
    Button mBtnQuery;
    @Bind(R.id.et_enter_query_number)
    EditText mEtEnterQueryNumber;
    @Bind(R.id.btn_query_address)
    Button mBtnQueryAddress;
    @Bind(R.id.ll_enter_number)
    LinearLayout mLlEnterNumber;
    @Bind(R.id.tv_address)
    TextView mTvAddress;
    @Bind(R.id.btn_common_number)
    Button mBtnCommonNumber;
    @Bind(R.id.btn_sms_backup)
    Button mBtnSmsBackup;
    @Bind(R.id.btn_app_lock)
    Button mBtnAppLock;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_advance_tool);
        ButterKnife.bind(this);
        mLlEnterNumber.setVisibility(View.GONE);
    }

    @OnClick({R.id.btn_advance_tool, R.id.btn_query, R.id.btn_query_address, R.id.btn_common_number, R.id.btn_sms_backup,R.id.btn_app_lock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_advance_tool:
                IntentUtils.startActivityAndFinish(AdvanceToolActivity.this, AppLockActivity.class);
                overridePendingTransition(R.anim.next_in, R.anim.next_out);
                break;
            case R.id.btn_query:
                mLlEnterNumber.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_query_address:
                // 1 先获得et_phone 内容
                String phone = mEtEnterQueryNumber.getText().toString().trim();
                // 2 做非空判断
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShort("号码不能为空");
                    Animation shake = AnimationUtils.loadAnimation(AdvanceToolActivity.this, R.anim.shake);
                    mEtEnterQueryNumber.startAnimation(shake);
                } else {
                    String address = AddressDao.query(phone);
                    mTvAddress.setText("查询结果:" + address);
                }
                break;
            case R.id.btn_common_number:
                IntentUtils.startActivity(AdvanceToolActivity.this, CommonNumberActivity.class);
                break;
            case R.id.btn_sms_backup:
                final ProgressDialog progressDialog = new ProgressDialog(AdvanceToolActivity.this);
                progressDialog.setTitle("备份");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                new Thread() {
                    public void run() {
                        try {
                            SmsUtils.smsBackup(getApplicationContext(), new SmsUtils.SmsBackup() {
                                @Override
                                public void beforSmsBacuUp(int max) {
                                    progressDialog.setMax(max);
                                }

                                @Override
                                public void smsBackup(int process) {
                                    progressDialog.setProgress(process);
                                }
                            }, "backup.xml");
                            progressDialog.dismiss();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start(); //备份短信
                progressDialog.show();
                break;
            case R.id.btn_app_lock:
                IntentUtils.startActivity(AdvanceToolActivity.this, RecyclerActivity.class);
                finish();
                break;
        }
    }
}
