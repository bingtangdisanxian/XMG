package com.example.chenqi.mobilphone.bean;

/**
 * Created by xmg on 2016/12/15.
 */

public class ContactInfoBean {
    private String name;
    private String phone;//alt+insert

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ContactInfoBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
