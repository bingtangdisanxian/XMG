package com.example.chenqi.mobilphone.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * textview的滚动工具类
 */

public class FoucsTextView extends TextView {
    public FoucsTextView(Context context) {
        this(context,null);
    }

    public FoucsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //是否可以获取焦点和触摸
    @Override
    public boolean isFocused() {
        return true;
    }
}
