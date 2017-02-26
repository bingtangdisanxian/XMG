package com.example.chenqi.mobilphone;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//获取dsgsdfasfasfasfasf.xlsx文件的MD5或者SHA1的特征码

public class TestMD52 extends AndroidTestCase {
    public void testMd5() throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        InputStream in = getContext().getAssets().open("dsgsdfasfasfasfasf.xlsx");
        int len;
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            digest.update(buffer, 0, len);
        }
        byte[] bytes = digest.digest();
        for (byte b : bytes) {
            String str = Integer.toHexString(b & 0xff);
            if (str.length() == 1) {
                sb.append("0");
            }
            sb.append(str);
        }
        String s = sb.toString();
        System.out.println("特征码=" + s);

    }
}
