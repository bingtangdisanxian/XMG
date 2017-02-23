package com.example.chenqi.mobilphone.activity;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.adpter.AppAdapter;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.AppInfo;
import com.example.chenqi.mobilphone.receiver.MyAdminReceiver;
import com.example.chenqi.mobilphone.utils.AppInfoUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2016/12/21.
 * 作用:
 */

public class AppManagerActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_dataSize, tv_sdkSize, tv_show_content;
    private List<AppInfo> mUserList, mSystemList;
    private ApkUninstallReceiver mReceiver;
    private PopupWindow mPopupWindow;
    private ListView lv_app_manager;
    private AppAdapter mAdapter;
    private AppInfo mAppInfo;
    private DevicePolicyManager mDpm;
    private ComponentName mWho;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //表示数据加载完成-->让progressbar不可见
            findViewById(R.id.ll_loading).setVisibility(View.GONE);
            mAdapter = new AppAdapter(mUserList, mSystemList);
            lv_app_manager.setAdapter(mAdapter);
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_app_manager);
        tv_sdkSize = (TextView) findViewById(R.id.tv_sdkSize);
        tv_dataSize = (TextView) findViewById(R.id.tv_dataSize);
        lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
        tv_show_content = (TextView) findViewById(R.id.tv_show_content);
        //注册广播
        mReceiver = new ApkUninstallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");//以包的形式删除
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void initData() {
        mDpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mWho = new ComponentName(this, MyAdminReceiver.class);
        //获得本地硬盘的剩余空间:获取内存文件-->获取剩余空间-->格式转换(android.text.format.Formatter)-->设置UI
        File dataFile = Environment.getDataDirectory();
        long dataSpace = dataFile.getFreeSpace();
        String data = Formatter.formatFileSize(this, dataSpace);
        tv_dataSize.setText("手机内存" + data);

        //获取SDK的剩余空间:获取SDK文件-->获取剩余空间
        File sdkFile = Environment.getExternalStorageDirectory();
        long sdkSpace = sdkFile.getFreeSpace();
        tv_sdkSize.setText("sd卡内存" + Formatter.formatFileSize(this, sdkSpace));

        //在子线程中请求数据
        new Thread() {
            @Override
            public void run() {
                mUserList = new ArrayList<>();
                mSystemList = new ArrayList<>();
                List<AppInfo> lists = AppInfoUtils.getAllInfo(AppManagerActivity.this);
                for (AppInfo list : lists) {
                    if (list.isUser()) {
                        mUserList.add(list);
                    } else {
                        mSystemList.add(list);
                    }
                }
                Message msg = Message.obtain();
                msg.obj = lists;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    protected void initListener() {
        lv_app_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //滚动时回调
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if (mUserList != null && mSystemList != null) {
                    if (firstVisibleItem > mUserList.size()) {
                        //第一个可见的item>用户应用时,就来到了系统软件
                        tv_show_content.setText("系统软件" + mSystemList.size() + "个");
                    } else {
                        tv_show_content.setText("用户软件" + mUserList.size() + "个");
                    }
                }
            }
        });

        lv_app_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                } else if (position == mUserList.size() + 1) {
                    return;
                } else if (position <= mUserList.size()) {
                    position = position - 1;
                    mAppInfo = mUserList.get(position);
                } else {
                    position = position - 1 - 1 - mUserList.size();
                    mAppInfo = mSystemList.get(position);
                }
                View ppView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_manager_ll, parent, false);
                /**
                 * 参数1:pp显示的布局转换的view
                 * 参数2,3:pp的宽和高
                 */
                mPopupWindow = new PopupWindow(ppView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //使用popupWindow时需要设置一个背景
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                /**
                 * panrent:pp挂载在谁上面(挂载在item上)
                 * grivaty:pp显示的位置(默认左上角)
                 * x:横轴方向的值(一般为60,而且值都一样)
                 * y:纵轴方向的值:不固定
                 * x,y的动态值都封装在view中的int数组中
                 */
                int[] location = new int[2];
                view.getLocationInWindow(location);
                mPopupWindow.showAtLocation(view, Gravity.TOP + Gravity.LEFT, 60, location[1]);
                //给popupWindow设置弹出动画
                AlphaAnimation aa = new AlphaAnimation(0.1f, 1);
                aa.setDuration(400);
                ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(400);
                //动画集,false表示不同步执行
                AnimationSet aSet = new AnimationSet(false);
                aSet.addAnimation(aa);
                aSet.addAnimation(sa);
                //通过popupWindow的布局启动动画
                ppView.startAnimation(aSet);
                ppView.findViewById(R.id.tv_start).setOnClickListener(AppManagerActivity.this);
                ppView.findViewById(R.id.tv_share).setOnClickListener(AppManagerActivity.this);
                ppView.findViewById(R.id.tv_detail).setOnClickListener(AppManagerActivity.this);
                ppView.findViewById(R.id.tv_uninstall).setOnClickListener(AppManagerActivity.this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start://启动
                startApp();
                break;
            case R.id.tv_share://分享
                shareApp();
                break;
            case R.id.tv_detail://详情
                detailApp();
                break;
            case R.id.tv_uninstall://卸载
                uninstallApp();
                break;
        }
        dismissPopupWindow();//点击popupWindow中的按钮后,不管确定还是取消都需要使popupWindow消失
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    private void startApp() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(mAppInfo.getPackgeName());
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(AppManagerActivity.this, "该软件无法启动", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareApp() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");//分享的形式为文字
        intent.putExtra(Intent.EXTRA_TEXT, "快来下载" + mAppInfo.getPackgeName() + "的应用");
        startActivity(intent);
    }

    private void detailApp() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + mAppInfo.getPackgeName()));
        startActivity(intent);
    }

    //需要通过广播来监听-->当真正卸载时才移除应用
    private void uninstallApp() {
        if (mAppInfo.isUser()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setAction("android.intent.action.DELETE");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.setData(Uri.parse("package:" + mAppInfo.getPackgeName()));
            startActivity(intent);
        } else {
//            if (!mDpm.isAdminActive(mWho)) {
//                Toast.makeText(AppManagerActivity.this, "需要root权限才能卸载哦...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mWho);
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启管理员权限");
//                startActivity(intent);
//            } else {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                intent.setAction("android.intent.action.DELETE");
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse("package:" + mAppInfo.getPackgeName()));
//                startActivity(intent);
//            }
            Toast.makeText(AppManagerActivity.this, "需要root权限才能卸载哦...", Toast.LENGTH_SHORT).show();
        }
    }

    class ApkUninstallReceiver extends BroadcastReceiver {

        //通过注册-->只要有apk被卸载时就会回调
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mAppInfo.isSD()) {
                mSystemList.remove(mAppInfo);
            } else {
                mUserList.remove(mAppInfo);
            }
            mAdapter.notifyDataSetChanged();
            mDpm.removeActiveAdmin(mWho);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        mReceiver = null;
        dismissPopupWindow();
        mDpm.removeActiveAdmin(mWho);
        super.onDestroy();
    }
}
