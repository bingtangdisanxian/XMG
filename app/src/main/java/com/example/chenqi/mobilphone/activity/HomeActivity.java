package com.example.chenqi.mobilphone.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.adpter.HomeAdapter;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

import static com.example.chenqi.mobilphone.R.drawable.b;
import static com.example.chenqi.mobilphone.R.drawable.d;
import static com.example.chenqi.mobilphone.R.drawable.e;

/**
 * Created by chenqi on 2016/12/18.
 * 作用:应用的主页面
 */

public class HomeActivity extends Activity {
    private ImageView iv_logo, iv_setting;
    private GridView gv_home_tools;
    private EditText mEtFirst, mEtSecond, mEtInput;
    private Button mBtnConfirm, mBtnCancel, inputBtn_confirm, inputBtn_cancel;
    private AlertDialog dialog;
    private String[] names = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀", "缓存清理", "高级工具"};
    private String[] desc = {"手机丢失好找", "防骚扰监听", "方便管理软件", "保持手机通畅", "注意流量超标", "手机安全保障", "手机快步如飞", "特性处理"};
    private int[] icons = {R.drawable.a, b, R.drawable.c, d,
            e, R.drawable.f, R.drawable.j, R.drawable.h};
    private AlertDialog.Builder mAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        startAnimation();
        initData();
        setListener();
    }

    private void setListener() {
        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivity(HomeActivity.this, SettingActivity.class);
            }
        });
        gv_home_tools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://手机防盗
                        String password = SpUtils.getString(Constans.PASSWORD, getApplicationContext());
                        //判断用户是否设置过防盗密码
                        if (TextUtils.isEmpty(password)) {
                            //弹出设置密码的对话框
                            showSetPasswordDialog();
                        } else {
                            //弹出输入密码的对话框
                            showWritePasswordDialog();
                        }
                        break;
                    case 1://通讯卫士
                        IntentUtils.startActivity(HomeActivity.this,BlackNumberActivity.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                        break;
                    case 2://软件管家
                        IntentUtils.startActivity(HomeActivity.this,AppManagerActivity.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                        break;
                    case 3://进程管理
                        IntentUtils.startActivity(HomeActivity.this,ProcessActivity.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                        break;
                    case 7://高级工具
                        IntentUtils.startActivity(HomeActivity.this,AdvanceToolActivity.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                        break;
                }
            }
        });
    }

    //设置密码的对话框
    private void showSetPasswordDialog() {
        //对话框的构建者
        mAb = new AlertDialog.Builder(this);
        View view = View.inflate(HomeActivity.this, R.layout.dialog_set_password, null);
        //找控件,当生成view的第三个参数为null时,就要通过view来找控件
        mEtFirst = (EditText) view.findViewById(R.id.et_inputPasswordFirst);
        mEtSecond = (EditText) view.findViewById(R.id.et_inputPasswordSecond);
        mBtnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消设置密码,则对话框消失(当button能被点击时,就说明dialog已经显示了,所以不会为空)
                dialog.dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先获取两次输入的密码
                String first = mEtFirst.getText().toString();
                String second = mEtSecond.getText().toString();
                //判断两次输入不为空
                if (TextUtils.isEmpty(first) || TextUtils.isEmpty(second)) {
                    Toast.makeText(HomeActivity.this, "请按要求输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!first.equals(second)) {
                    Toast.makeText(HomeActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    //保存密码
                    SpUtils.putString(Constans.PASSWORD, first, HomeActivity.this);
                    Toast.makeText(HomeActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    IntentUtils.startActivity(HomeActivity.this, GuardSetupActivity1.class);
                    overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    dialog.dismiss();
                }
            }
        });
        mAb.setView(view);
        dialog = mAb.show();
    }

    //输入密码的对话框
    private void showWritePasswordDialog() {
        mAb = new AlertDialog.Builder(this);
        View view = View.inflate(HomeActivity.this, R.layout.dialog_input_password, null);
        mEtInput = (EditText) view.findViewById(R.id.et_inputPassword);
        inputBtn_confirm = (Button) view.findViewById(R.id.inputBtn_confirm);
        inputBtn_cancel = (Button) view.findViewById(R.id.inputBtn_cancel);
        //取消
        inputBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        inputBtn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先获取此前保存的密码(即第一次设置的密码)
                String oldPassword = SpUtils.getString(Constans.PASSWORD, HomeActivity.this);
                //获取输入的密码
                String newPassword = mEtInput.getText().toString();
                if (!oldPassword.equals(newPassword)) {
                    Toast.makeText(HomeActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                } else {
                    //如果相等,则判断是否完成了所有设置-->获取状态
                    boolean finishSetup = SpUtils.getBoolean(HomeActivity.this, Constans.FINISHSETUP);
                    if (finishSetup) {
                        //如果完成了设置-->跳到功能页
                        IntentUtils.startActivity(HomeActivity.this, FunctionActivity1.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    } else {
                        //没有完成设置-->跳到第一个设置页
                        IntentUtils.startActivity(HomeActivity.this, GuardSetupActivity1.class);
                        overridePendingTransition(R.anim.next_in,R.anim.next_out);
                    }
                    //最后需要使对话框消失
                    dialog.dismiss();
                }
            }
        });
        mAb.setView(view);
        dialog = mAb.show();
    }

    private void initData() {
        HomeAdapter homeAdapter = new HomeAdapter(names, desc, icons);
        gv_home_tools.setAdapter(homeAdapter);
    }

    private void startAnimation() {
        ObjectAnimator oa = ObjectAnimator.ofFloat(iv_logo, "rotationY", 0, 60, 120, 180, 240, 300, 360);
        oa.setDuration(10000);
        oa.setRepeatCount(ObjectAnimator.INFINITE);
        oa.start();
    }

    private void initView() {
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        gv_home_tools = (GridView) findViewById(R.id.gv_home_tools);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
    }
}
