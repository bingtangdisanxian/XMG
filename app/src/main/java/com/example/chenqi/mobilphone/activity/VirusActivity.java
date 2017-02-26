package com.example.chenqi.mobilphone.activity;

import android.animation.ObjectAnimator;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.database.dao.Md5Dao;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.chenqi.mobilphone.R.id.ll_virtu_add;
import static com.example.chenqi.mobilphone.R.id.pb_virtu;

/**
 * Created by chenqi on 2017/2/24.
 * 描述:病毒查杀界面
 */
public class VirusActivity extends BaseActivity {
    @Bind(R.id.iv_scanf)
    ImageView mIvScanf;
    @Bind(pb_virtu)
    ProgressBar mPbVirtu;
    @Bind(ll_virtu_add)
    LinearLayout mLlVirtuAdd;
    @Bind(R.id.tv_scan_virus)
    TextView mTvScanVirus;
    @Bind(R.id.btn_start_scan)
    Button mBtnStartScan;
    @Bind(R.id.ll_progressBar)
    LinearLayout mLlProgressBar;
    private int mMaxProcess;
    private String mDesc;
    private List<String> mAppName;
    private ObjectAnimator mRotation;
    private int count = 0;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_virus);
        ButterKnife.bind(this);
    }

    @Override
    protected void initListener() {
        mBtnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAnimation();
                mLlProgressBar.setVisibility(View.VISIBLE);
                mTvScanVirus.setText("开始查杀病毒");
                AsyncTask<Void, Integer, Integer> asyncTask = new AsyncTask<Void, Integer, Integer>() {
                    @Override
                    protected Integer doInBackground(Void... params) {
                        mAppName = scanAllPackage();
                        for (int i = 0; i <= mAppName.size() - 1; i++) {
                            publishProgress(i);
                            SystemClock.sleep(200);
                        }
                        return count;
                    }

                    @Override
                    protected void onProgressUpdate(Integer... values) {
                        mLlProgressBar.setVisibility(View.GONE);
                        mPbVirtu.setMax(mMaxProcess);
                        mPbVirtu.setProgress(values[0]);
                        TextView tv = new TextView(getApplicationContext());
                        if (TextUtils.isEmpty(mDesc)) {
                            //没有查到,没有病毒
                            tv.setText(mAppName.get(values[0]));
                            tv.setTextSize(18);
                            tv.setTextColor(Color.BLACK);
                        } else {
                            //查到了,有病毒
                            tv.setText(mAppName.get(values[0]) + "有病毒");
                            tv.setTextColor(Color.RED);
                            tv.setTextSize(18);
                            count++;
                        }
                        mLlVirtuAdd.addView(tv, 0);
                    }

                    @Override
                    protected void onPostExecute(Integer count) {
                        mRotation.cancel();
                        mTvScanVirus.setText("共查杀了" + count + "个病毒");
                        super.onPostExecute(count);
                    }
                };
                asyncTask.execute();
            }
        });
    }

    private void initAnimation() {
        mRotation = ObjectAnimator.ofFloat(mIvScanf, "rotation", 0, 60, 120, 180, 270, 360);
        mRotation.setDuration(2000);
        mRotation.setRepeatCount(ObjectAnimator.INFINITE);
        mRotation.start();
    }


    private List<String> scanAllPackage() {
        List<String> list = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES + PackageManager.SIGNATURE_UNKNOWN_PACKAGE);
        //当前遍历软件的总个数--进度条的总长度
        mMaxProcess = packages.size();
        for (PackageInfo info : packages) {
            // 当前遍历软件的名称
            String appName = info.applicationInfo.loadLabel(pm).toString();
            list.add(appName);
            //获得当前的所有app的特征码
            String path = info.applicationInfo.sourceDir;
            File file = new File(path);
            try {
                MessageDigest digest = MessageDigest.getInstance("md5");
                FileInputStream fis = new FileInputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, len);
                }
                StringBuffer sb = new StringBuffer();
                byte[] bytes = digest.digest();
                for (byte b : bytes) {
                    String str = Integer.toHexString(b & 0xff);
                    if (str.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(str);
                    String md5 = sb.toString();
                    mDesc = Md5Dao.query(md5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
