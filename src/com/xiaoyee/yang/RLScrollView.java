package com.xiaoyee.yang;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class RLScrollView extends ScrollView {

    private OnScrollChangedListener onScrollChangedListener;

    public RLScrollView(Context context) {
        super(context);
    }

    public RLScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RLScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    public boolean isChildVisible(View child) {
        if (child == null) {
            return false;
        }
        Rect scrollBounds = new Rect();
        getHitRect(scrollBounds);
        return child.getLocalVisibleRect(scrollBounds);
    }

    public boolean isArriveTop() {
        return getScrollY() <= 0;
    }

    public boolean isArriveBottom() {
        return getScrollY() == getChildAt(getChildCount() - 1).getBottom() + getPaddingBottom() - getHeight();
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView who, int x, int y, int oldxX, int oldY);
    }
}