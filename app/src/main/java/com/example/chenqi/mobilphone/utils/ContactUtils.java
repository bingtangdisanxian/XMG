package com.example.chenqi.mobilphone.utils;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.example.chenqi.mobilphone.bean.ContactInfoBean;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentProviderOperation.newInsert;

/**
 * Created by chenqi on 2016/12/15.
 * 用来获取和添加联系人的工具类
 */

public class ContactUtils {

    private static Cursor dataCursor;

    public static List<ContactInfoBean> getAllContacts(Context context) {
        List<ContactInfoBean> lists = new ArrayList<>();
        // 1获得内容解析器
        ContentResolver resolver = context.getContentResolver();
        //准备对应的uri"content://主机名/表名/路径(不写表示查询所有)"
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        /**
         * 参数1 查询的路径
         * 参数2 查询的字段
         * 参数3 查询的条件
         * 参数4 查询条件的值
         * 参数5 排序
         */
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            if (!TextUtils.isEmpty(contact_id)) {
                //要根据id查询对应的联系人数据
                ContactInfoBean infoBean = new ContactInfoBean();
                dataCursor = resolver.query(dataUri, new String[]{"mimetype", "data1"}, "contact_id=?", new String[]{contact_id}, null);
                while (dataCursor.moveToNext()) {
                    String mimetype = dataCursor.getString(0);
                    if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                        String data1 = dataCursor.getString(1);
                        infoBean.setPhone(data1);
                    } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        String data1 = dataCursor.getString(1);
                        infoBean.setName(data1);
                    }
                }
                lists.add(infoBean);
            }
        }
        dataCursor.close();
        cursor.close();
        return lists;
    }

    //用事务的方式添加联系人
    public void insertContact(Context context) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation operation1 =
                newInsert(Uri.parse("content://com.android.contacts/raw_contacts"))
                        .withValue("_id", null)
                        .build();
        operations.add(operation1);
        ContentProviderOperation operation2 =
                newInsert(Uri.parse("content://com.android.contacts/data"))
                        .withValueBackReference("raw_contact_id", 0) //
                        .withValue("data2", "ZZH")
                        .withValue("mimetype", "vnd.android.cursor.item/name")
                        .build();
        operations.add(operation2);
        ContentProviderOperation operation3 = ContentProviderOperation
                .newInsert(Uri.parse("content://com.android.contacts/data"))
                .withValueBackReference("raw_contact_id", 0) //
                .withValue("data1", "18612312312")
                .withValue("data2", "2")
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .build();
        operations.add(operation3);
        ContentProviderOperation operation4 =
                newInsert(Uri.parse("content://com.android.contacts/data"))
                        .withValueBackReference("raw_contact_id", 0) //
                        .withValue("data1", "zq@itcast.cn")
                        .withValue("data2", "2")
                        .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                        .build();
        operations.add(operation4);
        // 在事务中对多个操作批量执行
        resolver.applyBatch("com.android.contacts", operations);
    }
}
