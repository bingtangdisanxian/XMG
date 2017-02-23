package com.example.chenqi.mobilphone;

import android.test.AndroidTestCase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TestMD5 extends AndroidTestCase {

    public  void testMd5() throws NoSuchAlgorithmException {
        // 通过MD5开始密
        // 1 获得MD5 对象 也就是一个MD5的数字摘要器
        String password="123456";
        StringBuffer sb=new StringBuffer();
        MessageDigest digest=MessageDigest.getInstance("md5");
        byte[] bytes = digest.digest(password.getBytes());
        for (byte  b: bytes) {
            String str =Integer.toHexString(b&0xff);
           if(str.length()==1) {
               sb.append("0");
           }
            sb.append(str);
        }
        System.out.println("----"+"sf"+sb.toString()+"wer");



    }
}
