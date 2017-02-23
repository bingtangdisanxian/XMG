package com.example.chenqi.mobilphone.activity;

import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.adpter.CommonAdapter;
import com.example.chenqi.mobilphone.base.BaseActivity;

/**
 * Created by chenqi on 2017/2/18.
 * 描述:常用号码展示界面
 */
public class CommonNumberActivity extends BaseActivity {

    private ExpandableListView mElv;
    private SQLiteDatabase mDb;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_common);
        mElv = (ExpandableListView) findViewById(R.id.elv);
    }

    @Override
    protected void initData() {
        mDb = SQLiteDatabase.openDatabase(getFilesDir().getAbsolutePath() + "/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
        CommonAdapter adapter = new CommonAdapter(mDb, getApplicationContext());
        mElv.setAdapter(adapter);
        super.initData();
    }

    @Override
    protected void initListener() {
        mElv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(CommonNumberActivity.this, "被点击的位置" + groupPosition + "  " + childPosition, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        super.initListener();
    }

    @Override
    protected void onDestroy() {
        //将数据库放到Activity中关闭,可以优化性能(防止高频查询数据库时,频繁开关数据库)
        mDb.close();
        super.onDestroy();
    }
}
