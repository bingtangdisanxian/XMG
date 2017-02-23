package com.example.chenqi.mobilphone.adpter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.BlackNumberInfo;
import com.example.chenqi.mobilphone.utils.ViewHolder;

import java.util.List;

/**
 * Created by chenqi on 2016/12/20.
 * 作用:黑名单数据的适配器
 */

public class BlackNumberAdapter extends BaseAdapter {
    private List<BlackNumberInfo> mList;
    public BlackNumberAdapter(List<BlackNumberInfo> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_black_number_lv,parent,false);
        }
        TextView tv_blackNumber = ViewHolder.get(convertView,R.id.tv_blackNumber);
        tv_blackNumber.setText(mList.get(position).getPhone());
        TextView tv_delete_blackNumber = ViewHolder.get(convertView,R.id.tv_delete_blackNumber);
        switch (Integer.valueOf(mList.get(position).getMode())){
            case 0:
                tv_delete_blackNumber.setText("短信拦截");
                break;
            case 1:
                tv_delete_blackNumber.setText("电话拦截");
                break;
            case 2:
                tv_delete_blackNumber.setText("全部拦截");
                break;
        }

        ImageView view = ViewHolder.get(convertView, R.id.iv_delete_blackNumber);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                builder.setTitle("删除此黑名单");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mList.remove(position);
                        notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
}
