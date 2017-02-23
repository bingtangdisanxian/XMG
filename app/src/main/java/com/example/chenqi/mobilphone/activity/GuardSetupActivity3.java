package com.example.chenqi.mobilphone.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseSetupActivity;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:选择安全号码
 */
public class GuardSetupActivity3 extends BaseSetupActivity {
    private EditText et_enter_safeNumber;
    private Button btn_choose_safeNumber;

    public void initView() {
        setContentView(R.layout.activity_setup_third);
        btn_choose_safeNumber = (Button) findViewById(R.id.btn_choose_safeNumber);
        et_enter_safeNumber = (EditText) findViewById(R.id.et_enter_safeNumber);
    }

    @Override
    public void initListener() {
        btn_choose_safeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到选则联系人界面
                IntentUtils.startActivityAndForResult(GuardSetupActivity3.this,ContactsActivity.class);
                //选中联系人后回回调onActivityResult()
            }
        });
    }
    //在此方法中完成显示选中的联系人
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            String phoneNumber = data.getStringExtra("phoneNumber").replace("-","").replace(" ","").trim();
            et_enter_safeNumber.setText(phoneNumber);
            //让数据回显时光标靠右
            et_enter_safeNumber.setSelection(et_enter_safeNumber.getText().length());
        }
    }

    @Override
    public void initData() {
        //回显输入框中的数据
        String sn = SpUtils.getString(Constans.safeNumber, GuardSetupActivity3.this);
        et_enter_safeNumber.setText(TextUtils.isEmpty(sn)?"":sn);
        et_enter_safeNumber.setSelection(et_enter_safeNumber.getText().length());
    }

    @Override
    public void toPre() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity3.this,GuardSetupActivity2.class);
    }

    @Override
    public void toNext() {
        String number = et_enter_safeNumber.getText().toString();
        if (TextUtils.isEmpty(number)){
            Toast.makeText(GuardSetupActivity3.this,"请输入安全号码",Toast.LENGTH_SHORT).show();
        }else {
            SpUtils.putString(Constans.safeNumber,number,GuardSetupActivity3.this);
            IntentUtils.startActivityAndFinish(GuardSetupActivity3.this,GuardSetupActivity4.class);
        }
    }

    @Override
    public void initBack() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity3.this,GuardSetupActivity2.class);
    }
}
