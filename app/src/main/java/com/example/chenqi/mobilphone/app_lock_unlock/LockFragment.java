package com.example.chenqi.mobilphone.app_lock_unlock;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chenqi.mobilphone.R;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:
 */
public class LockFragment extends BaseFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_lock, container, false);
        return view;
    }
}
