package com.example.chenqi.mobilphone.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 将字节输入流转为字符串
 */

public class StreamUtils {
    public static String InputSteam2String(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        while(line != null){
            sb.append(line);
            line = reader.readLine();
        }
        return sb.toString();
    }
}
