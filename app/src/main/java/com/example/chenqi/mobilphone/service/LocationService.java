package com.example.chenqi.mobilphone.service;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;

import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.SpUtils;

public class LocationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 1 获得定位的管理器
        final LocationManager lm= (LocationManager) getSystemService(LOCATION_SERVICE);
       /* List<String> providers = lm.getAllProviders();
        for (String  privder: providers) {
            System.out.println("----"+privder);
        }*/
        //当前最好的定位方式
        Criteria criteria=new Criteria();
        //设置定位的精度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 耗电量
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String bestProvider = lm.getBestProvider(criteria, true);
        //通过这种定位方式得到经纬度
        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            //位置发生改变的时候的回调
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String phone = SpUtils.getString(Constans.safeNumber, getApplicationContext());
                String text="j"+longitude+"w="+latitude;
                SmsManager.getDefault().sendTextMessage(phone,null,text,null,null);
                //停止监听
                lm.removeUpdates(this);
                stopSelf();
            }
            //状态发生改变的时候的回调
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            //可以定位的情况发生改变
            @Override
            public void onProviderEnabled(String provider) {

            }
            //不可以定位的时候的回调
            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }
}
