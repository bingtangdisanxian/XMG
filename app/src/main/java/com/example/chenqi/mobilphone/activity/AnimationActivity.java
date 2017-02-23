package com.example.chenqi.mobilphone.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.JsonBean;
import com.example.chenqi.mobilphone.config.Constans;
import com.example.chenqi.mobilphone.utils.SpUtils;
import com.example.chenqi.mobilphone.utils.StreamUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class AnimationActivity extends Activity {
    private static final int STARTHOME = 0;//不需升级,进入主界面
    private static final int NEEDUPDATE = 1;//需要升级
    private TextView mTv_VersionCode, mTv_VersionName;
    private RelativeLayout mRl_root;
    private AnimationSet mAS;
    private int mVersionCode;
    private String mVersionName;
    private JsonBean mJsonBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTHOME:
                    startHome();//进入主界面
                    finish();
                    break;
                case NEEDUPDATE: //需要提醒用户升级
                    showDownLoadDialog((JsonBean) msg.obj);
                    break;
                case 1000: //需要提醒用户升级
                    Toast.makeText(AnimationActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    break;
                case 1001: //需要提醒用户升级
                    Toast.makeText(AnimationActivity.this, "io异常", Toast.LENGTH_SHORT).show();
                    break;
            }
            startHome();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_animation);
        initView();
        initData();
        initAnimation();
        initListener();
        copyAddressDatabase("address.db");
        copyAddressDatabase("commonnum.db");
    }

    private void initView() {
        mRl_root = (RelativeLayout) findViewById(R.id.rl_root);
        mTv_VersionCode = (TextView) findViewById(R.id.tv_version_code);
        mTv_VersionName = (TextView) findViewById(R.id.tv_version_name);
    }

    private void initData() {
        getAppVersion();
    }

    private void getAppVersion() {
        //获得包的管理器
        PackageManager pm = getPackageManager();
        try {
            //获取包的信息
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号
            mVersionCode = packageInfo.versionCode;
            //获取版本名称
            mVersionName = packageInfo.versionName;
            //显示数据
            mTv_VersionCode.setText(String.valueOf(mVersionCode));
            mTv_VersionName.setText(mVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showDownLoadDialog(final JsonBean jsonBean) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("提示");
        ab.setMessage("描述" + mJsonBean.getDesc());
        ab.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApp(jsonBean);
            }
        });
        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startHome();
                finish();
            }
        });
        ab.show();
    }

    //下载app的方法
    private void downLoadApp(JsonBean jsonBean) {
        HttpUtils httpUtils = new HttpUtils();
        //创建文件
        final File file = new File(Environment.getExternalStorageDirectory(), "xx.apk");
        httpUtils.download(jsonBean.getDownloadurl(), file.getAbsolutePath(), new RequestCallBack<File>() {
            //下载成功回调
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                //需要按照最新的apk
                Toast.makeText(AnimationActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
               /* <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="content" />
                <data android:scheme="file" />
                <data android:mimeType="application/vnd.android.package-archive" />*/
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivityForResult(intent, 1);
            }

            //下载失败回调
            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(AnimationActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                startHome();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initListener() {
        mAS.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                //1.动画开始时-->判断用户是否开启了是否需要更新的功能
                if (SpUtils.getBoolean(getApplicationContext(), Constans.AUTOUPDATE)) {
                    //说明需要更新-->检查版本更新
                    checkUpDate();
                }
                //如果没有开启,则不需要任何操作,等待动画完成即可
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //1.动画结束时-->判断用户是否开启了更新的功能
                if (SpUtils.getBoolean(getApplicationContext(), Constans.AUTOUPDATE)) {
                    //不需要任何操作
                } else {
                    //不需要自动更新-->跳到主界面
                    startHome();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //动画重复时回调
            }
        });
    }

    private void checkUpDate() {
        new Thread() {
            @Override
            public void run() {
                //获取网络访问开始时的时间
                long startTime = SystemClock.currentThreadTimeMillis();
                Message msg = Message.obtain();
                try {
                    //访问网络获取数据
                    URL url = new URL(Constans.URL.UPDATEURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //设置超时时间
                    conn.setConnectTimeout(3 * 1000);
                    if (conn.getResponseCode() == 200) {
                        InputStream in = conn.getInputStream();
                        String json = StreamUtils.InputSteam2String(in);
                        //将json字符串转为bean对象
                        mJsonBean = new Gson().fromJson(json, JsonBean.class);
                        if (mVersionCode > Integer.parseInt(mJsonBean.getVersion())) {
                            msg.what = NEEDUPDATE;
                            msg.obj = mJsonBean;
                        } else {
                            msg.what = STARTHOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    msg.what = 1000;//url异常
                    e.printStackTrace();
                } catch (IOException e) {
                    msg.what = 1001;//io异常
                    e.printStackTrace();
                } finally {
                    //获取网络访问结束的时间
                    long endTime = SystemClock.currentThreadTimeMillis();
                    if (endTime - startTime < 1000) {
                        SystemClock.sleep(1000 - (endTime - startTime));
                    }
                    //当获取网络数据不够动画持续的3秒时,就睡够3秒再发送消息
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void startHome() {
        //切记:创建新的activity需要在清单文件中配置
        Intent intent = new Intent(AnimationActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void initAnimation() {
        //旋转动画
        /**
         * 参数1和2:起始角度和结束角度
         * 参数3,4,5,6:旋转中心(包括x值和y值)
         */
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setFillAfter(true);
        //缩放动画
        /**
         * 参数1,2,3,4:x和y的起始缩放的比例
         * 参数3,4,5,6:缩放中心
         */
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f
                , Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);
        sa.setFillAfter(true);
        //透明动画
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1000);
        aa.setFillAfter(true);
        //创建动画集
        /**
         * 参数:所有动画是否按统一速度运行
         */
        mAS = new AnimationSet(false);
        //添加动画
        mAS.addAnimation(ra);
        mAS.addAnimation(sa);
        mAS.addAnimation(aa);
        //在根布局中启动动画
        mRl_root.startAnimation(mAS);
    }



    private void copyAddressDatabase(final String name) {
        new Thread() {
            public void run() {
                try {
                    File file = new File(getFilesDir(), name);
                    if (file.exists() && file.length() > 0) {

                    } else {
                        InputStream is = getAssets().open(name);
                        // 将一个流转换为一个文件中的数据
                        // 1 先有文件
                        FileOutputStream fos = new FileOutputStream(file);
                        int len = -1;
                        byte[] buffer = new byte[1024];
                        while ((len = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                        }
                        is.close();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

