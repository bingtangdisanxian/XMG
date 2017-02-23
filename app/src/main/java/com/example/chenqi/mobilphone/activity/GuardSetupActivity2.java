package com.example.chenqi.mobilphone.activity;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseSetupActivity;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:sim的绑定与解绑
 */
public class GuardSetupActivity2 extends BaseSetupActivity {
    private RelativeLayout rl_bindSim;
    private ImageView iv_lock_unlock;
    private String mSim;

    public void initView() {
        setContentView(R.layout.activity_setup_second);
        rl_bindSim = (RelativeLayout) findViewById(R.id.rl_bindSim);
        iv_lock_unlock = (ImageView) findViewById(R.id.iv_lock_unlock);
    }

    @Override
    public void initData() {
        //锁状态的回显
        String sim = SpUtils.getString(Constans.SIM, GuardSetupActivity2.this);
        if (TextUtils.isEmpty(sim)){
            iv_lock_unlock.setImageResource(R.drawable.unlock);
        }else{
            iv_lock_unlock.setImageResource(R.drawable.lock);
        }
        //通过电话管理者获取SIM串号
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mSim = manager.getSimSerialNumber();

    }

    @Override
    public void initListener() {
        rl_bindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sim = SpUtils.getString(Constans.SIM, GuardSetupActivity2.this);
                if (TextUtils.isEmpty(sim)){
                    //获取sim串号,并保存
                    SpUtils.putString(Constans.SIM,mSim,GuardSetupActivity2.this);
                    //更换图片
                    iv_lock_unlock.setImageResource(R.drawable.lock);
                }else {
                    SpUtils.putString(Constans.SIM,"",GuardSetupActivity2.this);
                    iv_lock_unlock.setImageResource(R.drawable.unlock);
                }
            }
        });
    }

    @Override
    public void toNext() {
        //判定当绑定了sim卡之后才能进入到下一页
        String sim = SpUtils.getString(Constans.SIM, GuardSetupActivity2.this);
        if (!TextUtils.isEmpty(sim)){
            IntentUtils.startActivityAndFinish(GuardSetupActivity2.this,GuardSetupActivity3.class);
        }else {
            Toast.makeText(GuardSetupActivity2.this,"请绑定sim卡",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void toPre() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity2.this,GuardSetupActivity1.class);

    }

    //点击回退按钮就会调用此事件
    @Override
    public void initBack() {
        IntentUtils.startActivityAndFinish(GuardSetupActivity2.this,GuardSetupActivity1.class);

    }
}
