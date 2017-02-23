package com.example.chenqi.mobilphone.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.utils.ViewHolder;


/**
 * 适配器类
 */
public class HomeAdapter extends BaseAdapter {
    private String[] mNames, mDesc;
    private int[] mIcons;

    public HomeAdapter(String[] names, String[] desc, int[] icons) {
        mNames = names;
        mDesc = desc;
        mIcons = icons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_home_gv, parent, false);
        }
        ImageView iv_icon =  ViewHolder.get(convertView,R.id.iv_icon);
        iv_icon.setImageResource(mIcons[position]);
        TextView tv_title =  ViewHolder.get(convertView,R.id.tv_title);
        tv_title.setText(mNames[position]);
        TextView tv_desc = ViewHolder.get(convertView,R.id.tv_desc);
        tv_desc.setText(mDesc[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return mNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
