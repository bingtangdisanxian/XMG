package com.example.chenqi.mobilphone.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.ContactInfoBean;
import com.example.chenqi.mobilphone.utils.ViewHolder;

import java.util.List;

/**
 * Created by chenqi on 2016/12/19.
 * 作用:
 */

public class ContactAdapter extends BaseAdapter {
    private List<ContactInfoBean> mlists;
    public ContactAdapter(List<ContactInfoBean> lists) {
        mlists = lists;
    }

    @Override
    public int getCount() {
        return mlists.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_lv,parent,false);
        }
        TextView tv_name = ViewHolder.get(convertView,R.id.tv_name);
        tv_name.setText(mlists.get(position).getName());
        TextView tv_phone = ViewHolder.get(convertView,R.id.tv_phone);
        tv_phone.setText(mlists.get(position).getPhone());
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return mlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
