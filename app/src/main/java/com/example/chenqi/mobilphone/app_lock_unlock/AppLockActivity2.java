package com.example.chenqi.mobilphone.app_lock_unlock;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.activity.AdvanceToolActivity;
import com.example.chenqi.mobilphone.base.BaseActivity;
import com.example.chenqi.mobilphone.utils.IntentUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:用Fragment+recyclerView来实现程序锁界面
 */
public class AppLockActivity2 extends BaseActivity {

    @Bind(R.id.tb_toolbar)
    Toolbar mTbToolbar;
    @Bind(R.id.btn_unlock)
    Button mBtnUnlock;
    @Bind(R.id.btn_locked)
    Button mBtnLocked;
    private UnLockFragment mUnLockFragment;
    private LockFragment mLockFragment;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_app_lock_2);
        ButterKnife.bind(this);
        setToolBar();
        setDefaultFragment();
    }

    private void setToolBar() {
        setSupportActionBar(mTbToolbar);
        mTbToolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void setDefaultFragment() {
        FragmentManager manager = getSupportFragmentManager();
        mUnLockFragment = new UnLockFragment();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fm_content, mUnLockFragment);
        transaction.commit();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick({R.id.btn_unlock, R.id.btn_locked})
    public void onClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (view.getId()) {
            case R.id.btn_unlock:
                mBtnUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
                mBtnLocked.setBackgroundResource(R.drawable.tab_right_default);
                if (mUnLockFragment == null) {
                    mUnLockFragment = new UnLockFragment();
                    transaction.add(R.id.fm_content, mUnLockFragment);
                } else {
                    transaction.show(mUnLockFragment);
                }
                break;
            case R.id.btn_locked:
                mBtnUnlock.setBackgroundResource(R.drawable.tab_left_default);
                mBtnLocked.setBackgroundResource(R.drawable.tab_right_pressed);
                if (mLockFragment == null) {
                    mLockFragment = new LockFragment();
                    transaction.add(R.id.fm_content, mLockFragment);
                } else {
                    transaction.show(mLockFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mUnLockFragment != null) {
            transaction.hide(mUnLockFragment);
        }
        if (mLockFragment != null) {
            transaction.hide(mLockFragment);
        }
    }

    @Override
    protected void initListener() {
        mTbToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.startActivity(AppLockActivity2.this,AdvanceToolActivity.class);
                finish();
            }
        });
        super.initListener();
    }
}
