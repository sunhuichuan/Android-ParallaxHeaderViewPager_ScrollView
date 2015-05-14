package com.xiaoyee.yang;

import android.widget.ScrollView;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight, int headerTranslationY);

    void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition);
}
