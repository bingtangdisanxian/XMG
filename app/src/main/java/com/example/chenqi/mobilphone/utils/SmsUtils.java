package com.example.chenqi.mobilphone.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

//备份短信的工具

public class SmsUtils {

    public  interface  SmsBackup{
        //短信备份前调用
        void beforSmsBacuUp(int max);
        //短信备份中调用
        void smsBackup(int process);
    }

    public static boolean smsBackup(Context context,SmsBackup backup ,String name) throws IOException {
        ContentResolver resolver = context.getContentResolver();
        Uri uri=Uri.parse("content://sms/");
        XmlSerializer xmlSerializer = Xml.newSerializer();
        // 1得到文件
        File file=new File(Environment.getExternalStorageDirectory(),name);
        FileOutputStream fos=new FileOutputStream(file);
        xmlSerializer.setOutput(fos,"utf-8");
        xmlSerializer.startDocument("utf-8",true);
        xmlSerializer.startTag(null,"info");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type"}, null, null, null);
       //备份的总数
        backup.beforSmsBacuUp(cursor.getCount());
        int process=0;
        while (cursor.moveToNext()){
            xmlSerializer.startTag(null,"sms");
            xmlSerializer.startTag(null,"address");
            String address = cursor.getString(0);
            xmlSerializer.text(address);
            xmlSerializer.endTag(null,"address");
            xmlSerializer.startTag(null,"date");
            String date = cursor.getString(1);
            xmlSerializer.text(date);
            xmlSerializer.endTag(null,"date");
            xmlSerializer.startTag(null,"type");
            String type = cursor.getString(2);
            xmlSerializer.text(type);
            xmlSerializer.endTag(null,"type");
            xmlSerializer.endTag(null,"sms");
            SystemClock.sleep(800);
            process++;
            backup.smsBackup(process);
        }
        xmlSerializer.endTag(null,"info");
        xmlSerializer.endDocument();
        return true;
    }
}
