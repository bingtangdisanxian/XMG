package com.example.chenqi.mobilphone.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.bean.AppInfo;
import com.example.chenqi.mobilphone.database.dao.AppLockDao;
import com.example.chenqi.mobilphone.utils.AppInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenqi on 2016/12/25.
 * 作用:
 */
public class AppLockActivity extends BaseActivity {

    @Bind(R.id.btn_unlock)
    Button mBtnUnlock;
    @Bind(R.id.btn_locked)
    Button mBtnLocked;
    @Bind(R.id.lv_app_unlock)
    ListView mLvAppUnlock;
    @Bind(R.id.ll_unlock_app)
    LinearLayout mLlUnlockApp;
    @Bind(R.id.lv_app_locked)
    ListView mLvAppLocked;
    @Bind(R.id.ll_locked_app)
    LinearLayout mLlLockedApp;
    @Bind(R.id.tv_show_content)
    TextView mTvShowContent;
    @Bind(R.id.tv_unlock)
    TextView mTvUnlock;
    @Bind(R.id.tv_locked)
    TextView mTvLocked;
    private List<AppInfo> mList;
    private List<AppInfo> mUnlockList, mUnlockUserList, mUnlockSystemList;
    private List<AppInfo> mLockedList, mLockUserList, mLockSystemList;
    private AppUnLockAndLockAdapter mUnLockAdapter, mLockAdapter;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mUnLockAdapter = new AppUnLockAndLockAdapter(true);
            mLvAppUnlock.setAdapter(mUnLockAdapter);
            mLockAdapter = new AppUnLockAndLockAdapter(false);
            mLvAppLocked.setAdapter(mLockAdapter);
        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);
        mLlLockedApp.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mUnlockList = new ArrayList<>();
        mUnlockUserList = new ArrayList<>();
        mUnlockSystemList = new ArrayList<>();
        mLockedList = new ArrayList<>();
        mLockUserList = new ArrayList<>();
        mLockSystemList = new ArrayList<>();

        final AppLockDao dao = new AppLockDao(AppLockActivity.this);
        new Thread() {
            @Override
            public void run() {
                mList = AppInfoUtils.getAllInfo(AppLockActivity.this);
                for (AppInfo info : mList) {
                    String appName = info.getPackgeName();
                    boolean isFind = dao.findApp(appName);
                    if (isFind) {
                        mLockedList.add(info);
                    } else {
                        mUnlockList.add(info);
                    }
                }
                //区分未加锁的用户软件和系统软件
                for (AppInfo appInfo : mUnlockList) {
                    if (appInfo.isUser()) {
                        mUnlockUserList.add(appInfo);
                    } else {
                        mUnlockSystemList.add(appInfo);
                    }
                }
                //区分已加锁的用户软件和系统软件
                for (AppInfo appInfo : mLockedList) {
                    if (appInfo.isUser()) {
                        mLockUserList.add(appInfo);
                    } else {
                        mLockSystemList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void initListener() {
        mLvAppUnlock.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mUnlockUserList != null && mUnlockSystemList != null) {
                    if (firstVisibleItem > mUnlockUserList.size()) {
                        mTvShowContent.setText("系统软件" + mUnlockSystemList.size() + "个");
                    } else {
                        mTvShowContent.setText("用户软件" + mUnlockUserList.size() + "个");
                    }
                }
            }
        });
        mLvAppLocked.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mLockUserList != null && mLockSystemList != null) {
                    if (firstVisibleItem > mLockUserList.size()) {
                        mTvShowContent.setText("系统软件" + mLockSystemList.size() + "个");
                    } else {
                        mTvShowContent.setText("用户软件" + mLockUserList.size() + "个");
                    }
                }
            }
        });
    }

    public class AppUnLockAndLockAdapter extends BaseAdapter {
        private boolean isUnlock;
        private AppInfo mAppInfo;

        AppUnLockAndLockAdapter(boolean isUnlock) {
            this.isUnlock = isUnlock;
        }

        @Override
        public int getCount() {
            int count;
            if (isUnlock) {
                count = (mUnlockUserList.size() + mUnlockSystemList.size());
                mTvUnlock.setText("未加锁的程序:" + count);
            } else {
                count = (mLockUserList.size() + mLockSystemList.size());
                mTvLocked.setText("已加锁的程序:" + count);
            }
            return count + 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(parent.getContext());
            tv.setTextSize(20);
            tv.setBackgroundColor(Color.GRAY);
            tv.setTextColor(Color.WHITE);
            //区分加锁和未加锁的用户软件和系统软件
            if (isUnlock) {
                if (position == 0) {
                    //                    tv.setText("用户软件" + mUnlockUserList.size() + "个");
                    return tv;
                } else if (position == mUnlockUserList.size() + 1) {
                    tv.setText("系统软件" + mUnlockSystemList.size() + "个");
                    return tv;
                } else if (position <= mUnlockUserList.size()) {
                    position = position - 1;
                    mAppInfo = mUnlockUserList.get(position);
                } else {
                    position = position - 2 - mUnlockUserList.size();
                    mAppInfo = mUnlockSystemList.get(position);
                }
            }
            if (!isUnlock) {
                if (position == 0) {
                    //                    tv.setText("用户软件" + mLockUserList.size() + "个");
                    return tv;
                } else if (position == mLockUserList.size() + 1) {
                    tv.setText("系统软件" + mLockSystemList.size() + "个");
                    return tv;
                } else if (position <= mLockUserList.size()) {
                    position = position - 1;
                    mAppInfo = mLockUserList.get(position);
                } else {
                    position = position - 2 - mLockUserList.size();
                    mAppInfo = mLockSystemList.get(position);
                }
            }
            ViewHolder holder;
            final AppLockDao dao = new AppLockDao(parent.getContext());
            if (convertView != null && convertView instanceof RelativeLayout) {
                holder = (ViewHolder) convertView.getTag();
            } else {
                holder = new ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.lv_app_unlock, null);
                holder.mIvAppIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                holder.mTvAppName = (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.mIvAppLock = (ImageView) convertView.findViewById(R.id.iv_app_lock);
                convertView.setTag(holder);
            }
            if (isUnlock) {
                final AppInfo mFinalAppInfo = mAppInfo;
                holder.mIvAppIcon.setImageDrawable(mAppInfo.getIcon());
                holder.mTvAppName.setText(mAppInfo.getName());
                holder.mIvAppLock.setImageResource(R.drawable.list_button_lock_pressed);
                final View finalConvertView = convertView;
                holder.mIvAppLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        animation.setDuration(500);
                        finalConvertView.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dao.addApp(mFinalAppInfo.getPackgeName());
                                if (mFinalAppInfo.isUser()) {
                                    mUnlockUserList.remove(mFinalAppInfo);
                                    mLockUserList.add(mFinalAppInfo);
                                } else {
                                    mUnlockSystemList.remove(mFinalAppInfo);
                                    mLockSystemList.add(mFinalAppInfo);
                                }
                                mUnLockAdapter.notifyDataSetChanged();
                                mLockAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });
            } else {
                holder.mIvAppIcon.setImageDrawable(mAppInfo.getIcon());
                holder.mTvAppName.setText(mAppInfo.getName());
                holder.mIvAppLock.setImageResource(R.drawable.list_button_unlock_pressed);
                final View finalConvertView = convertView;
                final AppInfo mFinalAppInfo = mAppInfo;
                holder.mIvAppLock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
                        animation.setDuration(500);
                        finalConvertView.startAnimation(animation);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                dao.delete(mFinalAppInfo.getPackgeName());
                                if (mFinalAppInfo.isUser()) {
                                    mLockUserList.remove(mFinalAppInfo);
                                    mUnlockUserList.add(mFinalAppInfo);
                                } else {
                                    mLockSystemList.remove(mFinalAppInfo);
                                    mUnlockSystemList.add(mFinalAppInfo);
                                }
                                mLockAdapter.notifyDataSetChanged();
                                mUnLockAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }
                });
            }
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

        class ViewHolder {
            ImageView mIvAppIcon;
            ImageView mIvAppLock;
            TextView mTvAppName;
        }
    }

    @OnClick({R.id.btn_unlock, R.id.btn_locked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_unlock:
                //修改两个button的选中状态
                mBtnUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                mBtnLocked.setBackgroundResource(R.drawable.tab_right_default);
                //listView的显示和隐藏
                mLlUnlockApp.setVisibility(View.VISIBLE);
                mLlLockedApp.setVisibility(View.GONE);
                break;
            case R.id.btn_locked:
                mBtnUnlock.setBackgroundResource(R.drawable.tab_left_default);
                mBtnLocked.setBackgroundResource(R.drawable.tab_right_pressed);
                mLlUnlockApp.setVisibility(View.GONE);
                mLlLockedApp.setVisibility(View.VISIBLE);
                break;
        }
    }
}
