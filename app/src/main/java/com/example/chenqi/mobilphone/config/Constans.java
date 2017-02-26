package com.example.chenqi.mobilphone.config;

import static com.example.chenqi.mobilphone.config.Constans.URL.BASEURL;

/**
 * 常量类:用来存储一些项目中需要用到的常量
 */
public class Constans {
    //SharePreferences中的文件名:config
    public static final String CONFIG = "config";
    public static final String AUTOUPDATE = "autoupdate";
    public static final String PASSWORD = "password";
    public static final String FINISHSETUP = "finishsetup";
    public static final String SIM = "sim";
    public static final String ISCHECKED = "ischecked";
    public static final String SHOWSYS = "showsys";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String SWICH = "swich";
    public static String safeNumber ="safenumber";
    public static final String URL_UPDATE_DB =BASEURL+"update.txt";

    public class URL{
        public static final String BASEURL ="http://192.168.53.1:8080/";
        public static final String UPDATEURL =BASEURL+"info.json";
    }
}
