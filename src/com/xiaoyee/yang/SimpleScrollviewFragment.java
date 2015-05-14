package com.xiaoyee.yang;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;


public class SimpleScrollviewFragment extends ScrollTabHolderFragment implements RLScrollView.OnScrollChangedListener {

    private static final String TAG = "SimpleScroll";
    RLScrollView mScrollView;
    private static final String ARG_POSITION = "position";

    private int mPosition;


    public static Fragment newInstance(int position) {
        SimpleScrollviewFragment f = new SimpleScrollviewFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    public SimpleScrollviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_scrollview, container, false);
        mScrollView = (RLScrollView) view.findViewById(R.id.svContainer);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScrollView.setOnScrollListener(this);
    }

    @Override
    public void adjustScroll(int scrollHeight, int headerTranslationY) {
        if (scrollHeight == 0 && !mScrollView.isAtTop()) {
            return;
        }
        mScrollView.setScrollY(-headerTranslationY);
    }

    @Override
    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (mScrollTabHolder != null) {
            mScrollTabHolder.onScroll(view, x, y, oldX, oldY, pagePosition);
        }
    }

    @Override
    public void onScrollChanged(ScrollView who, int x, int y, int oldxX, int oldY) {
        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(who, x, y, oldxX, oldY, mPosition);
    }
}
