package com.xiaoyee.yang;

import android.app.Activity;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.*;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.astuetz.PagerSlidingTabStrip;
import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.nineoldandroids.view.ViewHelper;

public class VPContainerFragment extends Fragment implements ScrollTabHolder, ViewPager.OnPageChangeListener {

    private Activity mContext;

    private KenBurnsSupportView mHeaderPicture;
    private View mHeader;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mMinHeaderTranslation;
    private ImageView mHeaderLogo;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private TypedValue mTypedValue = new TypedValue();
    private SpannableString mSpannableString;
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;

    public static VPContainerFragment newInstance() {
        return new VPContainerFragment();
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mMinHeaderTranslation = -mMinHeaderHeight;


        View view = inflater.inflate(R.layout.vp_container_layout, container, false);

        mHeaderPicture = (KenBurnsSupportView) view.findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.pic0);
        mHeaderLogo = (ImageView) view.findViewById(R.id.header_logo);
        mHeaderLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "btnClicked", Toast.LENGTH_LONG).show();
            }
        });

        mHeader = view.findViewById(R.id.header);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);

        mPagerAdapter = new PagerAdapter(((ActionBarActivity) mContext).getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);

        mViewPager.setAdapter(mPagerAdapter);

        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);


        //CUSTOM
        mPagerSlidingTabStrip.setShouldExpand(true);
        mSpannableString = new SpannableString(getString(R.string.actionbar_title));
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);


        ((ActionBarActivity) mContext).getSupportActionBar().setBackgroundDrawable(null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((ActionBarActivity) mContext).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // nothing
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // nothing
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ScrollTabHolder> scrollTabHolders = mPagerAdapter.getScrollTabHolders();
        ScrollTabHolder currentHolder = scrollTabHolders.valueAt(position);
        currentHolder.adjustScroll(mHeader.getHeight(), (int) ViewHelper.getTranslationY(mHeader));
    }

    @Override
    public void adjustScroll(int scrollHeight, int headerTranslationY) {
        // nothing
    }

    @Override
    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (mViewPager.getCurrentItem() == pagePosition) {
            int scrollY = view.getScrollY();
            ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
            float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
            float alpha = clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F);
            setTitleAlpha(alpha);

            Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ic_launcher);

            int iconAlpha = (int) (Math.abs(1 - alpha) * 255);
            drawable.setAlpha(iconAlpha);
            ((ActionBarActivity) mContext).getSupportActionBar().setHomeAsUpIndicator(drawable);
            if (iconAlpha <= 1) {
                ((ActionBarActivity) mContext).getSupportActionBar().hide();
            } else {
                ((ActionBarActivity) mContext).getSupportActionBar().show();
            }

        }
    }

    private void setTitleAlpha(float alpha) {
        mAlphaForegroundColorSpan.setAlpha(alpha);
        mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((ActionBarActivity) mContext).getSupportActionBar().setTitle(mSpannableString);

    }


    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Page 1", "Page 2", "Page 3"};
        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {

            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) SimpleScrollviewFragment.newInstance(position);

            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }
            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }

    }
}
