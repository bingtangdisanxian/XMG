package com.example.chenqi.mobilphone.app_lock_unlock;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static com.example.chenqi.mobilphone.base.BaseApplication.getContext;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:加锁和未加锁的列表的适配器
 */
class AppLockAndUnLockAdapter extends RecyclerView.Adapter<AppLockAndUnLockAdapter.ViewHolder> {

    private boolean isLock;
    private AppInfo mAppInfo;
    private List<AppInfo> mList;
    private List<AppInfo> mUnlockList, mUnlockUserList, mUnlockSystemList;
    private List<AppInfo> mLockedList, mLockUserList, mLockSystemList;

    public AppLockAndUnLockAdapter(boolean b, List<AppInfo> appInfo) {
        LockAndUnLockDao lockDao = new LockAndUnLockDao(getContext());
        isLock = b;
        initList(appInfo);
        initData(lockDao);
    }

    private void initList(List<AppInfo> appInfo) {
        mList = appInfo;
        mUnlockList = new ArrayList<>();
        mUnlockUserList = new ArrayList<>();
        mUnlockSystemList = new ArrayList<>();
        mLockedList = new ArrayList<>();
        mLockUserList = new ArrayList<>();
        mLockSystemList = new ArrayList<>();
    }

    private void initData(LockAndUnLockDao lockDao) {
        if (mList != null) {
            for (AppInfo info : mList) {
                String appName = info.getPackgeName();
                boolean isFind = lockDao.insert(appName);
                if (isFind) {
                    mLockedList.add(info);
                } else {
                    mUnlockList.add(info);
                }
            }
            //区分未加锁的用户软件和系统软件
            for (AppInfo appInfo : mUnlockList) {
                if (appInfo.isUser()) {
                    mUnlockUserList.add(appInfo);
                } else {
                    mUnlockSystemList.add(appInfo);
                }
            }
            //区分已加锁的用户软件和系统软件
            for (AppInfo appInfo : mLockedList) {
                if (appInfo.isUser()) {
                    mLockUserList.add(appInfo);
                } else {
                    mLockSystemList.add(appInfo);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_app_unlock, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView tv = new TextView(getContext());
        tv.setTextSize(14);
        tv.setBackgroundColor(Color.GRAY);
        tv.setTextColor(Color.WHITE);
        if (position == 0) {
            tv.setText("用户软件" + mUnlockUserList.size() + "个");
        } else if (position == mUnlockUserList.size() + 1) {
            tv.setText("系统软件" + mUnlockSystemList.size() + "个");
        } else if (position <= mUnlockUserList.size()) {
            position = position - 1;
            mAppInfo = mUnlockUserList.get(position);
        } else {
            position = position - 2 - mUnlockUserList.size();
            mAppInfo = mUnlockSystemList.get(position);
        }
        holder.mIvAppIcon.setImageDrawable(mAppInfo.getIcon());
        holder.mTvAppName.setText(mAppInfo.getName());
        holder.mIvAppLock.setImageResource(R.drawable.list_button_lock_pressed);
    }

    //获取item的类型
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvAppIcon, mIvAppLock;
        TextView mTvAppName;

        ViewHolder(View itemView) {
            super(itemView);
            mIvAppIcon = (ImageView) itemView.findViewById(R.id.iv_app_icon);
            mTvAppName = (TextView) itemView.findViewById(R.id.tv_app_name);
            mIvAppLock = (ImageView) itemView.findViewById(R.id.iv_app_lock);
        }
    }
}
