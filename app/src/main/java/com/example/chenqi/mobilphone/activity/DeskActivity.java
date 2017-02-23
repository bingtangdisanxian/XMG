package com.example.chenqi.mobilphone.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2017/2/19.
 * 描述:自定义一个桌面
 */
public class DeskActivity extends BaseActivity{

    private GridView mGvDeskContent;
    @Override
    protected void initView() {
        setContentView(R.layout.activity_desk);
        mGvDeskContent = (GridView) findViewById(R.id.gv_desk_content);
        PackageManager pm = getPackageManager();
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        final List<Drawable> icons=new ArrayList<>();
        final List<String> names=new ArrayList<>();
        final List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        for (ResolveInfo  info: infos) {
            Drawable drawable = info.activityInfo.loadIcon(pm);
            icons.add(drawable);
            String name = info.activityInfo.loadLabel(pm).toString();
            names.add(name);
        }
        mGvDeskContent.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return infos.size();
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Drawable drawable = icons.get(position);
                String name = names.get(position);
                ImageView iv=new ImageView(getApplicationContext());
                iv.setImageDrawable(drawable);
                TextView tv=new TextView(getApplicationContext());
                tv.setText(name);
                tv.setTextColor(Color.BLACK);
                LinearLayout linearLayout=new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(iv);
                linearLayout.addView(tv);
                return linearLayout;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }
        });
    }
}
