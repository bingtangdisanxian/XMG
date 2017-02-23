package com.example.chenqi.mobilphone.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by chenqi on 2016/12/21.
 * 作用:
 */

public class AppInfo {
    /**
     * 字段1:软件的名称
     * 字段2:包名
     * 字段3:apk的大小
     * 字段4:软件的图标
     * 字段5:安装的位置是否是SD卡
     * 字段6:软件类型(用户和系统)
     */
    private String name;
    private String packgeName;
    private long size;
    private Drawable icon;
    private boolean IsSD;
    private boolean IsUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSD() {
        return IsSD;
    }

    public void setSD(boolean SD) {
        IsSD = SD;
    }

    public boolean isUser() {
        return IsUser;
    }

    public void setUser(boolean user) {
        IsUser = user;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", packgeName='" + packgeName + '\'' +
                ", size=" + size +
                ", IsSD=" + IsSD +
                ", IsUser=" + IsUser +
                ", icon=" + icon +
                '}';
    }
}
