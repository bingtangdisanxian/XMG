package com.example.chenqi.mobilphone.app_lock_unlock.recycler;

import android.content.Context;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:
 */
public class DensityUtils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context,float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
