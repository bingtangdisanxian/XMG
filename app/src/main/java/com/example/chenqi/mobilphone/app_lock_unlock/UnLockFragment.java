package com.example.chenqi.mobilphone.app_lock_unlock;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenqi.mobilphone.R;
import com.example.chenqi.mobilphone.bean.AppInfo;
import com.example.chenqi.mobilphone.utils.AppInfoUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:未加锁的Fragment
 */
public class UnLockFragment extends BaseFragment {

    @Bind(R.id.tv_unLock_app)
    TextView mTvUnLockApp;
    @Bind(R.id.tv_user_app)
    TextView mTvUserApp;
    @Bind(R.id.rv_app_unLock)
    RecyclerView mRvAppUnLock;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            mRvAppUnLock.setLayoutManager(manager);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            AppLockAndUnLockAdapter adapter = new AppLockAndUnLockAdapter(false, (List<AppInfo>) msg.obj);
            mRvAppUnLock.setAdapter(adapter);
        }
    };

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_unlock, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        new Thread() {
            @Override
            public void run() {
                List<AppInfo> list = AppInfoUtils.getAllInfo(getContext());
                Message msg = Message.obtain();
                msg.obj = list;
                mHandler.sendMessage(msg);
            }
        }.start();
        super.initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
