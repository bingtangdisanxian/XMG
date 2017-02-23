package com.example.chenqi.mobilphone.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.adpter.ContactAdapter;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.ContactInfoBean;
import com.example.chenqi.mobilphone.utils.ContactUtils;

import java.util.List;

/**
 * Created by chenqi on 2016/12/19.
 * 作用:联系人界面
 */

public class ContactsActivity extends BaseActivity {
    private ListView lv_select_contacts;
    private ContactAdapter mAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mAdapter = new ContactAdapter((List<ContactInfoBean>) msg.obj);
            lv_select_contacts.setAdapter(mAdapter);
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_contacts);
        lv_select_contacts = (ListView) findViewById(R.id.lv_select_contacts);
    }

    @Override
    protected void initData() {
        //请求数据的操作都最好在子线程中完成
        new Thread() {
            @Override
            public void run() {
                List<ContactInfoBean> list = ContactUtils.getAllContacts(ContactsActivity.this);
                Message msg = Message.obtain();
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void initListener() {
        lv_select_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //从adapter类中通过getItem()获取bean对象
                ContactInfoBean contactInfoBean = (ContactInfoBean) mAdapter.getItem(position);
                String phone = contactInfoBean.getPhone();
                Intent intent = new Intent();
                intent.putExtra("phoneNumber",phone);
                setResult(0,intent);
                //关闭页面
                finish();
            }
        });
    }
}
