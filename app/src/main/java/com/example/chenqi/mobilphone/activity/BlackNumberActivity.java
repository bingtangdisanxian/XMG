package com.example.chenqi.mobilphone.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.adpter.BlackNumberAdapter;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.BlackNumberInfo;
import com.example.chenqi.mobilphone.database.dao.BlackNumberDao;
import com.example.chenqi.mobilphone.utils.IntentUtils;

import java.util.List;


/**
 * Created by chenqi on 2016/12/18.
 * 作用:操作黑名单的界面
 */

public class BlackNumberActivity extends BaseActivity {
    private ListView lv_blackNumber;
    private TextView tv_add_blackNumber;
    private BlackNumberDao mDao;
    private BlackNumberAdapter mAdapter;
    private LinearLayout mLl;
    private List<BlackNumberInfo> mLists;
    private EditText mEt_black_number;
    private AlertDialog mDialog;//是AlertDialog不是dialog
    private int start = 0;
    private int count = 20;
    private int mCount = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAdapter == null) {
                mLl.setVisibility(View.GONE);
                mAdapter = new BlackNumberAdapter((List<BlackNumberInfo>) msg.obj);
                lv_blackNumber.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_black_number);
        lv_blackNumber = (ListView) findViewById(R.id.lv_blackNumber);
        tv_add_blackNumber = (TextView) findViewById(R.id.tv_add_blackNumber);
        mLl = (LinearLayout) findViewById(R.id.ll_progressBar);
        mLl.setVisibility(View.VISIBLE);
        mDao = new BlackNumberDao(BlackNumberActivity.this);
    }

    @Override
    protected void initData() {
        mCount = mDao.getCount();
        Log.v("+mCount",""+mCount);
        new Thread() {
            @Override
            public void run() {
                if (mLists == null) {
                    //第一次添加count条数据
                    mLists = mDao.findPart(start, count);
                } else {
                    //以后每次在此前的基础上添加count条数据
                    mLists.addAll(mDao.findPart(start, count));
                }
                Message msg = Message.obtain();
                msg.obj = mLists;
                SystemClock.sleep(1000);//模拟加载数据耗时
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void initListener() {
        tv_add_blackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBlackNumber();
            }
        });

        lv_blackNumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING://滑翔
                        break;
                    case SCROLL_STATE_IDLE:
                        //必须是静止且滑动到底部,得到最后可见条目的位置
                        int position = lv_blackNumber.getLastVisiblePosition();
                        //得到当前listView的总条目等于最后可见的位置时,说明拖动到最底部了
                        if (mLists.size() - 1 == position) {
                            //加载新的数据
                            start += count;
                            //判断start这个数是否小于总数
                            if (start < mCount) {
                                initData();
                            } else {
                                Toast.makeText(BlackNumberActivity.this, "没有更多的数据了", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://触摸滑动
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void addBlackNumber() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BlackNumberActivity.this);
        final View dialogView = View.inflate(getApplicationContext(), R.layout.view_black_number_dialog, null);
        TextView view = (TextView) dialogView.findViewById(R.id.tv_choose_number);
        mEt_black_number = (EditText) dialogView.findViewById(R.id.et_black_number);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.rg_black_group);
        Button btn_ok = (Button) dialogView.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = mEt_black_number.getText().toString().trim();
                String mode = "0";
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_sms:
                        mode = "0";
                        break;
                    case R.id.rb_phone:
                        mode = "1";
                        break;
                    case R.id.rb_all:
                        mode = "2";
                        break;
                }
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(BlackNumberActivity.this, "号码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = mDao.addBlackNumber(phone, mode);//是否添加到了数据库
                    if (result) {
                        BlackNumberInfo bean = new BlackNumberInfo();
                        bean.setMode(mode);
                        bean.setPhone(phone);
                        mLists.add(0, bean);//新添加进的添加在集合最前面
                        mAdapter.notifyDataSetChanged();
                    }
                    mDialog.dismiss();
                }
            }
        });
        mDialog = builder.create();
        mDialog.setView(dialogView);
        mDialog.show();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivityAndForResult(BlackNumberActivity.this, ContactsActivity.class);
                mDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String phoneNumber = data.getStringExtra("phoneNumber").replace("-", "").replace(" ", "").trim();
        mEt_black_number.setText(phoneNumber);
        mEt_black_number.setSelection(mEt_black_number.getText().length());
        mDialog.show();
    }
}
