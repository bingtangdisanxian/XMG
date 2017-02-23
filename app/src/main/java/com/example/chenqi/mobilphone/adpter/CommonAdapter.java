package com.example.chenqi.mobilphone.adpter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.chenqi.mobilphone.database.dao.CommonPhoneDao;

/**
 * Created by chenqi on 2017/2/18.
 * 描述:双层listView的适配器
 */
public class CommonAdapter extends BaseExpandableListAdapter {

    private SQLiteDatabase mDb;
    private Context mContext;

    public CommonAdapter(SQLiteDatabase db, Context context) {
        mDb = db;
        mContext = context;
    }

    /**
     * 最外层listView的个数
     *
     * @return
     */
    @Override
    public int getGroupCount() {
        return CommonPhoneDao.getGroupCount(mDb);
    }

    /**
     * 得到每个外层listview的item的孩子个数
     *
     * @param groupPosition 当前的外层listview 的item的位置
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return CommonPhoneDao.getChildrenCount(groupPosition, mDb);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView tv = null;
        if (convertView == null) {
            tv = new TextView(mContext);
        } else {
            tv = (TextView) convertView;
        }
        tv.setTextColor(Color.BLUE);
        tv.setTextSize(24);
        String name = CommonPhoneDao.getGroupView(groupPosition, mDb);
        tv.setText("       " + name);
        return tv;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tv = new TextView(mContext);
        String name = CommonPhoneDao.getChildView(groupPosition, childPosition, mDb);
        tv.setText(name);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(24);
        return tv;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
