package com.example.chenqi.mobilphone.activity;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseSetupActivity;
import com.example.chenqi.mobilphone.utils.IntentUtils;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:第一个设置界面
 */
public class GuardSetupActivity1 extends BaseSetupActivity {

    public void initView() {
        setContentView(R.layout.activity_setup_first);
    }

    @Override
    public void toNext() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity1.this,GuardSetupActivity2.class);
    }

    public void initBack() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity1.this,HomeActivity.class);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void toPre() {

    }
}
