package com.example.chenqi.mobilphone.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5utils {

    public static String getMd5Str(String text) throws NoSuchAlgorithmException {
        // 通过MD5开始密
        // 1 获得MD5 对象 也就是一个MD5的数字摘要器

        StringBuffer sb=new StringBuffer();
        MessageDigest digest=MessageDigest.getInstance("md5");
        byte[] bytes = digest.digest(text.getBytes());
        for (byte  b: bytes) {
            String str =Integer.toHexString(b&0xff);
            if(str.length()==1) {
                sb.append("0");
            }
            sb.append(str);
        }
        return sb.toString();

    }
}
