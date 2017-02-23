package com.example.chenqi.mobilphone.activity;

import android.app.ActivityManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.ProcessInfo;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.IntentUtils;
import com.example.chenqi.mobilphone.utils.ProcessInfoUtils;
import com.example.chenqi.mobilphone.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.chenqi.mobilphone.R.id.tv_show_content;

/**
 * Created by chenqi on 2016/12/22.
 * 作用:进程管理
 */
public class ProcessActivity extends BaseActivity {
    @Bind(R.id.btn_all_select)
    Button mBtnAllSelect;
    @Bind(R.id.btn_negation)
    Button mBtnNegation;
    @Bind(R.id.btn_clean)
    Button mBtnClean;
    @Bind(R.id.btn_set)
    Button mBtnSet;
    private TextView tvProcessCounts, tvProcessMemSize, tvShowContent;
    private ArrayList<ProcessInfo> mUserList, mSystemList, killAllList;
    private ListView lvProcessItem;
    private LinearLayout llLoading;
    private List<ProcessInfo> mLists;
    private ProcessInfo mProcessInfo;
    private int mRunningProcess;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.GONE);
            if (mAdapter == null) {
                mAdapter = new ProcessAdapter();
            } else {
                mAdapter.notifyDataSetChanged();
            }
            lvProcessItem.setAdapter(mAdapter);
        }
    };
    private ProcessAdapter mAdapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_process_manager);
        tvProcessCounts = (TextView) findViewById(R.id.tv_process_counts);
        tvProcessMemSize = (TextView) findViewById(R.id.tv_process_memSize);
        lvProcessItem = (ListView) findViewById(R.id.lv_process_item);
        llLoading = (LinearLayout) findViewById(R.id.ll_loading);
        tvShowContent = (TextView) findViewById(tv_show_content);
    }

    @Override
    protected void initData() {
        String mem = Formatter.formatFileSize(this, ProcessInfoUtils.getUsableMemory(this));
        String total = Formatter.formatFileSize(this, ProcessInfoUtils.getTotalMemory(this));
        mRunningProcess = ProcessInfoUtils.getRunningProcess(this);
        tvProcessCounts.setText("运行中的进程" + mRunningProcess + "个");
        tvProcessMemSize.setText("剩余/总内存:" + mem + "/" + total);
        //for循环中的数据需要在子线程中请求
        llLoading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                mUserList = new ArrayList<>();
                mSystemList = new ArrayList<>();
                killAllList = new ArrayList<>();
                mLists = ProcessInfoUtils.getAllProcessInfo(ProcessActivity.this);
                //区分出系统进程和用户进程
                for (ProcessInfo info : mLists) {
                    if (info.isUser()) {
                        mUserList.add(info);
                    } else {
                        mSystemList.add(info);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
        ButterKnife.bind(this);
    }

    @Override
    protected void initListener() {
        lvProcessItem.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mUserList != null && mSystemList != null) {
                    if (firstVisibleItem <= mUserList.size()) {
                        tvShowContent.setText("用户进程:" + mUserList.size() + "个");
                    } else {
                        tvShowContent.setText("系统进程:" + mSystemList.size() + "个");
                    }
                }
            }
        });

        lvProcessItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //不给两个text标题设置监听事件
                if (position == 0) {
                    return;
                } else if (position == mUserList.size() + 1) {
                    return;
                } else if (position <= mUserList.size()) {
                    position = position - 1;
                    mProcessInfo = mUserList.get(position);
                } else {
                    position = position - 1 - 1 - mUserList.size();
                    mProcessInfo = mSystemList.get(position);
                }
                //判断为应用本身时不可被点击
                if (mProcessInfo.getPackgeName().equals(getPackageName())) {
                    return;
                }
                mProcessInfo.setChecked(!mProcessInfo.isChecked());
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick({R.id.btn_all_select, R.id.btn_negation, R.id.btn_clean, R.id.btn_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all_select:
                selectAll();
                break;
            case R.id.btn_negation:
                selectNegation();
                break;
            case R.id.btn_clean:
                killAll();
                break;
            case R.id.btn_set:
                setting();
                break;
        }
    }

    private void setting() {
        IntentUtils.startActivity(ProcessActivity.this,ProcessSettingActivity.class);
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);
    }

    private void killAll() {
        long conuntMem = 0;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ProcessInfo info : mUserList) {
            if (info.isChecked()) {
                //这个方法只能杀掉别人,不能杀掉自己
                am.killBackgroundProcesses(info.getPackgeName());
                //不能直接remove,因为正在被遍历的集合大小不能改变
                killAllList.add(info);
            }
        }
        for (ProcessInfo info : mSystemList) {
            if (info.isChecked()) {
                am.killBackgroundProcesses(info.getPackgeName());
                killAllList.add(info);
            }
        }
        for (ProcessInfo info : killAllList) {
            if (info.isUser()) {
                conuntMem+=info.getSize();
                mUserList.remove(info);
            } else {
                conuntMem+=info.getSize();
                mSystemList.remove(info);
            }
        }
        tvProcessCounts.setText("运行中的进程" + (mRunningProcess - killAllList.size()) + "个");
        String mem = Formatter.formatFileSize(this, ProcessInfoUtils.getUsableMemory(this)+conuntMem);
        String total = Formatter.formatFileSize(this, ProcessInfoUtils.getTotalMemory(this));
        tvProcessMemSize.setText("剩余/总内存:" + mem + "/" + total);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(ProcessActivity.this,"清理了"+killAllList.size()+"个进程,释放了"+Formatter.formatFileSize(this,conuntMem)+"内存",Toast.LENGTH_SHORT).show();
        killAllList.clear();
    }

    private void selectNegation() {
        for (ProcessInfo info : mUserList) {
            if (info.getPackgeName().equals(getPackageName())) {
                continue;//此次跳出循环
            }
            info.setChecked(!info.isChecked());
        }
        for (ProcessInfo info : mSystemList) {
            info.setChecked(!info.isChecked());
        }
        mAdapter.notifyDataSetChanged();
    }

    private void selectAll() {
        for (ProcessInfo info : mUserList) {
            if (info.getPackgeName().equals(getPackageName())) {
                continue;//此次跳出循环
            }
            info.setChecked(true);
        }
        for (ProcessInfo info : mSystemList) {
            info.setChecked(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }
        super.onStart();
    }

    class ProcessAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int count;
            boolean showsys = SpUtils.getBoolean(ProcessActivity.this, Constans.SHOWSYS);
            if (showsys){
                count = mUserList.size()+1+mSystemList.size()+1;
            }else {
                count = mUserList.size()+1;
            }
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView tv_user = new TextView(parent.getContext());
                tv_user.setTextSize(20);
//              tv_user.setText("用户进程"+ mUserList.size());
                tv_user.setBackgroundColor(Color.GRAY);
                tv_user.setTextColor(Color.WHITE);
                return tv_user;
            } else if (position == mUserList.size() + 1) {
                TextView tv_system = new TextView(parent.getContext());
                tv_system.setTextSize(20);
                tv_system.setText("系统进程:" + mSystemList.size() + "个");
                tv_system.setBackgroundColor(Color.GRAY);
                tv_system.setTextColor(Color.WHITE);
                return tv_system;
            } else if (position <= mUserList.size()) {
                position = position - 1;
                mProcessInfo = mUserList.get(position);
            } else {
                position = position - 1 - 1 - mUserList.size();
                mProcessInfo = mSystemList.get(position);
            }
            ViewHolder holder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.item_process_lv, null);
                holder.mIvProcessLogo = (ImageView) convertView.findViewById(R.id.iv_process_logo);
                holder.mTvProcessName = (TextView) convertView.findViewById(R.id.tv_process_name);
                holder.mTvProcessSize = (TextView) convertView.findViewById(R.id.tv_process_size);
                holder.mIsChecked = (CheckBox) convertView.findViewById(R.id.tv_isChecked);
                convertView.setTag(holder);
            }
            holder.mIvProcessLogo.setImageDrawable(mProcessInfo.getIcon());
            holder.mTvProcessName.setText(mProcessInfo.getName());
            holder.mTvProcessSize.setText("占用内存:" + Formatter.formatFileSize(parent.getContext(), mProcessInfo.getSize())+"空间");
            //判断当应用是自己时,隐藏选则框
            if (getPackageName().equals(mProcessInfo.getPackgeName())) {
                holder.mIsChecked.setVisibility(View.GONE);
            } else {
                holder.mIsChecked.setVisibility(View.VISIBLE);//防止缓存
                holder.mIsChecked.setChecked(mProcessInfo.isChecked());
            }
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            ImageView mIvProcessLogo;
            TextView mTvProcessName;
            TextView mTvProcessSize;
            CheckBox mIsChecked;
        }
    }
}

