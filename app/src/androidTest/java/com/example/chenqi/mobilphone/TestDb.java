package com.example.chenqi.mobilphone;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.chenqi.mobilphone.database.dao.BlackNumberDao;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentProviderOperation.newInsert;

/**
 * Created by chenqi on 2016/12/20.
 * 作用:相关数据库的测试类
 */

public class TestDb extends AndroidTestCase {

    public void testAddBlackNumber() {
        //添加黑名单
        BlackNumberDao dao = new BlackNumberDao(getContext());
        Random random = new Random();
        for (int i = 0; i < 3000; i++) {
            dao.addBlackNumber("1388888" + i, random.nextInt(3) + "");
        }
    }

    //读取通讯录的全部的联系人
    //需要先在raw_contact表中遍历id，并根据id到data表中获取数据
    public void testReadAll(){
        //uri = content://com.android.contacts/contacts
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
        ContentResolver resolver = this.getContext().getContentResolver();
        //获得_id属性
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while(cursor.moveToNext()){
            StringBuilder buf = new StringBuilder();
            //获得id并且在data中寻找数据
            int id = cursor.getInt(0);
            buf.append("id="+id);
            uri = Uri.parse("content://com.android.contacts/contacts/"+id+"/data");
            //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
            Cursor cursor2 = resolver.query(uri, new String[]{"data1","data2"}, null,null, null);
            while(cursor2.moveToNext()){
                String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){       //如果是名字
                    buf.append(",name="+data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")){  //如果是电话
                    buf.append(",phone="+data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")){  //如果是email
                    buf.append(",email="+data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")){ //如果是地址
                    buf.append(",address="+data);
                }
                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")){  //如果是组织
                    buf.append(",organization="+data);
                }
            }
            String str = buf.toString();
            Log.i("Contacts", str);
        }
    }

    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
    public void testReadNameByPhone(){
        String phone = "12345678";
        //uri=  content://com.android.contacts/data/phones/filter/#
        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+phone);
        ContentResolver resolver = this.getContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"display_name"}, null, null, null); //从raw_contact表中返回display_name
        if(cursor.moveToFirst()){
            Log.i("Contacts", "name="+cursor.getString(0));
        }
    }

    //注意：对某个联系人插入姓名、电话等记录时必须要插入Data.MIMETYPE（或者是"mimetype"）属性,而不是插入"mimetype_id"!
    //比如：values.put(Data.MIMETYPE,"vnd.android.cursor.item/phone_v2")
    public void testDb1() {
        //添加联系人
        ContentResolver resolver = getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentValues values = new ContentValues();
        //向raw_contacts插入一条除了ID之外, 其他全部为NULL的记录, ID是自动生成的
        long id = ContentUris.parseId(resolver.insert(uri, values));
        //添加联系人姓名
        uri = Uri.parse("content://com.android.contacts/data");
        values.put("raw_contact_id", id);
        values.put("data2", "FHM");
        values.put("mimetype", "vnd.android.cursor.item/name");
        resolver.insert(uri, values);
        //添加联系人电话
        values.clear(); // 清空上次的数据
        values.put("raw_contact_id", id);
        values.put("data1", "18600000000");
        values.put("data2", "2");
        values.put("mimetype", "vnd.android.cursor.item/phone_v2");
        resolver.insert(uri, values);
        //添加联系人邮箱
        values.clear();
        values.put("raw_contact_id", id);
        values.put("data1", "zxx@itcast.cn");
        values.put("data2", "1");
        values.put("mimetype", "vnd.android.cursor.item/email_v2");
        resolver.insert(uri, values);
    }

    public void testDb2() {
        for (int i = 0; i < 3000; i++) {
            testDb1();
        }
    }

    //批量添加
    public void testInsertContact() throws Exception {
        //添加联系人
        ContentResolver resolver = getContext().getContentResolver();
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

    //核心思想：
    //(1)先在raw_contacts表根据姓名(此处的姓名为name记录的data2的数据而不是data1的数据)查出id；
    //(2)在data表中只要raw_contact_id匹配的都删除；
    public void testDelete() throws Exception {
        String name = "FHM";
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = this.getContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, "display_name=?", new String[]{name}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, "display_name=?", new String[]{name});
            uri = Uri.parse("content://com.android.contacts/data");
            resolver.delete(uri, "raw_contact_id=?", new String[]{id + ""});
        }
    }

    //核心思想：
    //(1)不需要更新raw_contacts，只需要更新data表；
    //(2)uri=content://com.android.contacts/data 表示对data表进行操作；
    public void testUpdate() throws Exception {
        int id = 1;
        String phone = "999999";
        Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
        ContentResolver resolver = this.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put("data1", phone);
        resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2", id + ""});
    }
}
