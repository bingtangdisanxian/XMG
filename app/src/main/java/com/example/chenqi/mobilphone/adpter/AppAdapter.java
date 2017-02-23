package com.example.chenqi.mobilphone.adpter;

import android.graphics.Color;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.AppInfo;

import java.util.List;

/**
 * Created by chenqi on 2016/12/21.
 * 作用:应用展示界面的适配器
 */

public class AppAdapter extends BaseAdapter {
    private List<AppInfo> mUserList, mSystemList;
    private AppInfo mAppInfo;

    public AppAdapter(List<AppInfo> userList, List<AppInfo> systemList) {
        mUserList = userList;
        mSystemList = systemList;
    }

    @Override
    public int getCount() {
        return mUserList.size() + mSystemList.size() + 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            TextView tv_user = new TextView(parent.getContext());
            tv_user.setTextSize(20);
            tv_user.setText("用户软件" + mUserList.size() + "个");
            tv_user.setBackgroundColor(Color.GRAY);
            tv_user.setTextColor(Color.WHITE);
            return tv_user;
        } else if (position == mUserList.size() + 1) {
            TextView tv_system = new TextView(parent.getContext());
            tv_system.setTextSize(20);
            tv_system.setText("系统软件" + mSystemList.size() + "个");
            tv_system.setBackgroundColor(Color.GRAY);
            tv_system.setTextColor(Color.WHITE);
            return tv_system;
        } else if (position <= mUserList.size()) {
            position = position - 1;
            mAppInfo = mUserList.get(position);
        } else {
            position = position - 1 - 1 - mUserList.size();
            mAppInfo = mSystemList.get(position);
        }
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.item_app_lv, null);
            holder.iv_app_logo = (ImageView) convertView.findViewById(R.id.iv_app_logo);
            holder.tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            holder.tv_app_flag = (TextView) convertView.findViewById(R.id.tv_app_flag);
            holder.tv_app_size = (TextView) convertView.findViewById(R.id.tv_app_size);
            convertView.setTag(holder);
        }
        holder.iv_app_logo.setImageDrawable(mAppInfo.getIcon());
        holder.tv_app_name.setText(mAppInfo.getName());
        holder.tv_app_flag.setText(mAppInfo.isSD() ? "SD卡" : "手机内存");
        holder.tv_app_size.setText(Formatter.formatFileSize(parent.getContext(), mAppInfo.getSize()));
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

    public class ViewHolder {
        ImageView iv_app_logo;
        TextView tv_app_name;
        TextView tv_app_flag;
        TextView tv_app_size;
    }
}