package com.example.chenqi.mobilphone.activity;

import android.net.TrafficStats;
import android.text.format.Formatter;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.orhanobut.logger.Logger;

/**
 * Created by chenqi on 2017/2/24.
 * 描述:流量统计界面
 */
public class TrafficActivity extends BaseActivity{
    @Override
    protected void initView() {
        setContentView(R.layout.activity_traffic);
        long mobilerx = TrafficStats.getMobileRxBytes(); //获取手机(2g/3g/4g)下载的数据信息单位是byte
        long mobiletx = TrafficStats.getMobileTxBytes(); //获取手机 2g/3g/4g上传的数据信息

        long totalrx = TrafficStats.getTotalRxBytes();//获取全部端口下载的流量数据. 包括wifi和 2g/3g/4g的流量
        long totaltx = TrafficStats.getTotalTxBytes();//获取全部端口上传的流量数据. 包括wifi和 2g/3g/4g的流量

        long browserrx = TrafficStats.getUidRxBytes(10004);
        long browsertx =TrafficStats.getUidTxBytes(10004);

        Logger.v("手机上传:"+ Formatter.formatFileSize(this, mobiletx));
        Logger.v("手机下载:"+Formatter.formatFileSize(this, mobilerx));
        Logger.v("wifi上传:"+Formatter.formatFileSize(this, totaltx - mobiletx));
        Logger.v("wifi下载:"+Formatter.formatFileSize(this, totalrx- mobilerx));
        Logger.v("浏览器上传:"+Formatter.formatFileSize(this, browsertx));
        Logger.v("浏览器下载:"+Formatter.formatFileSize(this, browserrx));
    }
}
