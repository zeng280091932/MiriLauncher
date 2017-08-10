package com.miri.launcher.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.utils.Logger;

/**
 * This widget implements the dynamic action bar tab behavior that can change across different
 * configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {

    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {

        /**
         * Callback when the selected tab has been reselected.
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            final int oldSelected = mViewPager.getCurrentItem();
            if (view instanceof TabView) {
                TabView tabView = (TabView) view;
                final int newSelected = tabView.getIndex();
                Logger.getLogger().e(
                        "oldSelected-->" + oldSelected + ",newSelected-->" + newSelected);
                mViewPager.setCurrentItem(newSelected);
                if (oldSelected == newSelected && mTabReselectedListener != null) {
                    mTabReselectedListener.onTabReselected(newSelected);
                }
            }
        }
    };

    private final OnFocusChangeListener mTabFocusListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            //                Logger.getLogger().e("v:" + v + " , hasFocus:" + hasFocus);
            if (hasFocus) {
                mTabClickListener.onClick(view);
            } else {
            }
        }
    };

    private LinearLayout mTabLayout;

    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener mListener;

    private int mMaxTabWidth;

    private int mSelectedTabIndex;

    private OnTabReselectedListener mTabReselectedListener;

    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabLayout = new LinearLayout(context);
        mTabLayout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTabLayout.setGravity(Gravity.CENTER);
        addView(mTabLayout);
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1
                && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {

            @Override
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        super.onDetachedFromWindow();
    }

    private void addTab(int index, CharSequence text, int iconResId) {
        final TabView tabView = new TabView(getContext());
        //        tabView.setId(new Random().nextInt());
        tabView.setIndex(index);
        tabView.setFocusable(true);
        tabView.setFocusableInTouchMode(true);
        tabView.setClickable(true);
        tabView.setOnClickListener(mTabClickListener);
        //        tabView.setOnFocusChangeListener(mTabFocusListener);
        tabView.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN)) {
                    int newSelected = tabView.getIndex();
                    int count = mTabLayout.getChildCount();
                    if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)) {
                        if (newSelected <= count - 1) {
                            mViewPager.setCurrentItem(newSelected + 1);
                        }
                    } else if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT)) {
                        if (newSelected >= 1) {
                            mViewPager.setCurrentItem(newSelected - 1);
                        }
                    }
                }
                return false;
            }
        });
        tabView.setTitle(text, iconResId);

        mTabLayout.addView(tabView);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter) adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    public View getCurrTabView() {
        if (mTabLayout != null) {
            View view = mTabLayout.getChildAt(mSelectedTabIndex);
            return view;
        }
        return null;
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private class TabView extends LinearLayout {

        private TextView tabTextView;

        private ImageView tabLight;

        private int mIndex;

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, AttributeSet attrs) {
            super(context, attrs);
            View view = LayoutInflater.from(context).inflate(R.layout.tab_item, this, true);
            tabTextView = (TextView) view.findViewById(R.id.tabTextView);
            tabLight = (ImageView) view.findViewById(R.id.tab_light);
            view.setBackgroundResource(R.drawable.tab_title_bg);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }

        public void setIndex(int index) {
            this.mIndex = index;
        }

        public void setTitle(CharSequence title, int iconResId) {
            if (tabTextView != null) {
                tabTextView.setText(title);
                if (iconResId != 0) {
                    tabTextView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
                }
            }
        }

    }

}
