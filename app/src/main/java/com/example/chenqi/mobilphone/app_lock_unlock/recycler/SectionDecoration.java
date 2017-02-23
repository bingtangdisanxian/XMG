package com.example.chenqi.mobilphone.app_lock_unlock.recycler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.example.chenqi.mobilphone.R;

import java.util.List;

/**
 * Created by chenqi on 2017/2/22.
 * 描述:
 */
public class SectionDecoration extends RecyclerView.ItemDecoration {
    private List<NameBean> beanList;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int alignBottom;
    private DecorationCallback callback;
    private Paint.FontMetrics fontMetrics;

    interface DecorationCallback {
        String getGroupId(int position);

        String getGroupFirstLine(int position);
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            //因为是根据字符串的内容的相同与否来判断是不是同一组的，所以此处的标记id要是String类型
            //如果只做联系人列表悬浮框里面显示的是字母则标记id直接用int类型就行了
            String prevGroupId = callback.getGroupId(pos - 1);
            String groupId = callback.getGroupId(pos);
            //判断前一个字符串与当前字符串是否相同
            if (prevGroupId.equals(groupId)) {
                return false;
            } else {
                return true;
            }
        }
    }

    public SectionDecoration(List<NameBean> beanList, Context context, DecorationCallback callback) {
        Resources resources = context.getResources();
        this.beanList = beanList;
        this.callback = callback;
        //设置悬浮栏的画笔
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtils.dip2px(context, 12));
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setColor(Color.WHITE);
        fontMetrics = new Paint.FontMetrics();
        //决定悬浮栏的高度等
        topGap = resources.getDimensionPixelSize(R.dimen.sectioned_top);
        //决定文本的显示位置等
        alignBottom = resources.getDimensionPixelSize(R.dimen.sectioned_alignBottom);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupId = callback.getGroupId(pos);
        if (groupId.equals("-1"))
            return;
        //只有是同一组的第一个才显示悬浮栏
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
            if (beanList.get(pos).getName() == "") {
                outRect.top = 0;
            }
        } else {
            outRect.top = 0;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String groupId = callback.getGroupId(position);
            if (groupId.equals("-1"))
                return;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (textLine == "") {
                float top = view.getTop();
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);
                return;
            } else {
                if (position == 0 || isFirstInGroup(position)) {
                    float top = view.getTop() - topGap;
                    float bottom = view.getTop();
                    //绘制悬浮栏
                    c.drawRect(left, top - topGap, right, bottom, paint);
                    //绘制文本
                    c.drawText(textLine, left, bottom, textPaint);
                }

            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int itemCount = state.getItemCount();
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        float lineHeight = textPaint.getTextSize() + fontMetrics.descent;
        String preGroupId = "";
        String groupId = "-1";
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupId = groupId;
            groupId = callback.getGroupId(position);
            if (groupId.equals("-1") || groupId.equals(preGroupId))
                continue;
            String textLine = callback.getGroupFirstLine(position).toUpperCase();
            if (TextUtils.isEmpty(textLine))
                continue;
            int viewBottom = view.getBottom();
            float textY = Math.max(topGap, view.getTop());
            //下一个和当前不一样移动当前
            if (position + 1 < itemCount) {
                String nextGroupId = callback.getGroupId(position + 1);
                //组内最后一个view进入了header
                if (nextGroupId != groupId && viewBottom < textY) {
                    textY = viewBottom;
                }
            }
            //textY-toGap决定了悬浮栏绘制的高度和位置
            c.drawRect(left, textY - topGap, right, textY, paint);
            //left+2*alignBottom 决定了文本往左偏移的多少（加-->向左移）
            //textY-alignBottom  决定了文本往右偏移的多少  (减-->向上移)
            c.drawText(textLine, left + 2 * alignBottom, textY - alignBottom, textPaint);
        }
    }
}
