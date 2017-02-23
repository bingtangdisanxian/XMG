package com.example.chenqi.mobilphone.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by chenqi on 2016/12/21.
 * 作用:
 */

public class ProcessInfo {
    /**
     * 字段1:软件的名称
     * 字段2:包名
     * 字段3:apk的大小
     * 字段4:软件的图标
     * 字段5:安装的位置是否被选中
     * 字段6:软件类型(用户和系统)
     */
    private String name;
    private String packgeName;
    private long size;
    private Drawable icon;
    private boolean IsChecked;
    private boolean IsUser;

    public boolean isUser() {
        return IsUser;
    }

    public void setUser(boolean user) {
        IsUser = user;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", packgeName='" + packgeName + '\'' +
                ", size=" + size +
                ", IsChecked=" + IsChecked +
                ", icon=" + icon +
                ", IsUser=" + IsUser +
                '}';
    }

}
