package com.example.chenqi.mobilphone.activity;

import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.AppInfo;
import com.example.chenqi.mobilphone.utils.ToastUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenqi on 2017/2/25.
 * 描述:缓存清理界面
 */
public class CacheCleanActivity extends BaseActivity {

    private static final int SCAN_FINISH = 0;//扫描完毕
    private static final int SCANING = 2;//正在扫描某个包
    private ProgressBar pb_clear_cache;
    private FrameLayout fl_clear_cache;
    private TextView tv_clear_cache;
    private ListView lv_clear_cache;
    private Button btn_clear_all;
    private List<AppInfo> mLists;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_FINISH:
                    fl_clear_cache.setVisibility(View.GONE);
                    ClearAdapter adapter = new ClearAdapter();
                    lv_clear_cache.setAdapter(adapter);
                    break;
                case SCANING:
                    tv_clear_cache.setText((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_clean_cache);
        pb_clear_cache = (ProgressBar) findViewById(R.id.pb_clear_cacher);
        fl_clear_cache = (FrameLayout) findViewById(R.id.fl_clear_cacher);
        tv_clear_cache = (TextView) findViewById(R.id.tv_clear_cacher);
        lv_clear_cache = (ListView) findViewById(R.id.lv_clear_cacher);
        btn_clear_all = (Button) findViewById(R.id.btn_clear_all);
        scanApp();
    }

    @Override
    public void initListener() {
        //清除全部应用的缓存
        btn_clear_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Method[] methods = PackageManager.class.getDeclaredMethods();
                for (Method  method: methods) {
                    if("freeStorageAndNotify".equals(method.getName())) {
                        try {
                            //申请一个无穷大的空间来清除缓存
                            method.invoke(getPackageManager(),Long.MAX_VALUE,new MyClearCacheObserver());
                            //重新刷新还有缓存的列表
                            scanApp();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    class MyClearCacheObserver extends IPackageDataObserver.Stub{

        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
           ToastUtils.showShort(succeeded?"清理成功":"清理失败");
        }
    }

    private void scanApp() {
        mLists = new ArrayList<>();
        new Thread() {
            public void run() {
                //遍历当前设备上的所有安装的app
                PackageManager pm = getPackageManager();
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                pb_clear_cache.setMax(infos.size());
                int process = 0;
                for (PackageInfo info : infos) {
                    process++;
                    pb_clear_cache.setProgress(process);
                    String packageName = info.packageName;
                    try {
                        //次方法被PackageManager类中,但被隐藏了,所以需要通过反射获取
                        Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        method.invoke(pm, packageName, new MyStatsObserver());
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    SystemClock.sleep(50);
                }
                //说明循环完毕
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }

    class MyStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
            String packageName = pStats.packageName;
            PackageManager pm = getPackageManager();
            PackageInfo info = null;
            try {
                info = pm.getPackageInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String appName = info.applicationInfo.loadLabel(pm).toString();
            Message msg = Message.obtain();
            msg.what = SCANING;
            msg.obj = appName;
            handler.sendMessage(msg);
            long cacheSize = pStats.cacheSize;
            //当有缓存时缓存,才set值(才能在列表中显示出来)
            if (cacheSize > 0) {
                AppInfo infoBean = new AppInfo();
                //保持了大小
                infoBean.setSize(cacheSize);
                // 需要存在缓存文件的名称
                String name = info.applicationInfo.loadLabel(pm).toString();
                infoBean.setName(name);
                // 文件的图片
                Drawable icon = info.applicationInfo.loadIcon(pm);
                infoBean.setIcon(icon);
                infoBean.setPackgeName(pStats.packageName);
                mLists.add(infoBean);
            }
        }
    }

    class ClearAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLists != null ? mLists.size() : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                viewHolder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_clear_lv, null);
                viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                viewHolder.iv_clear = (ImageView) convertView.findViewById(R.id.iv_clear);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_name);
                viewHolder.tv_cache = (TextView) convertView.findViewById(R.id.tv_cache_size);
                convertView.setTag(viewHolder);
            }
            final AppInfo infoBean = mLists.get(position);
            viewHolder.iv_icon.setImageDrawable(infoBean.getIcon());
            viewHolder.tv_name.setText(infoBean.getName());
            viewHolder.tv_cache.setText("缓存"+infoBean.getSize()/1024+"KB");
            viewHolder.iv_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到系统清理缓存界面
                    Intent intent = new Intent();
                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + infoBean.getPackgeName()));
                    startActivity(intent);
                }
            });
            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_cache;
        ImageView iv_clear;
    }
}
