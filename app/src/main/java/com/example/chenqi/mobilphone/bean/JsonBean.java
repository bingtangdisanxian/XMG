package com.example.chenqi.mobilphone.bean;

/**
 * 解析json数据对应的bean
 */

public class JsonBean {

    private String version;
    private String downloadurl;
    private String desc;
    public JsonBean(){

    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadurl() {
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "JsonBean{" +
                "version='" + version + '\'' +
                ", downloadurl='" + downloadurl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
