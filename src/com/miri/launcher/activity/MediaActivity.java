/* 
 * 文件名：LauncherActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.miri.launcher.R;
import com.miri.launcher.fragment.MyPhotoFragment;
import com.miri.launcher.fragment.MyVideoFragment;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.view.IconPagerAdapter;
import com.miri.launcher.view.LauncherHeadView;
import com.miri.launcher.view.PageIndicator;
import com.miri.launcher.view.TabPageView;

/**
 * 我的媒体
 * @author zengjiantao
 * @version TVLAUNCHER001, 2013-9-7
 */
public class MediaActivity extends BaseFragmentActivity {

    private LauncherHeadView mHeadView;

    private TabPageView mTabPageView;

    private PageIndicator mIndicator;

    private ViewPager mViewPager;

    private MediaFragmentAdapter mAdapter;

    private boolean isFirst = true;

    private static final int[] CONTENT = new int[] {R.string.my_video, R.string.my_photo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        //		mHeadView = new LauncherHeadView(this);
        mTabPageView = (TabPageView) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = mTabPageView.getTabPageIndicator();

    }

    private void init() {
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new MediaFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            init();
            isFirst = false;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.getLogger().d("onConfigurationChanged:" + newConfig);
        super.onConfigurationChanged(newConfig);
        // finish();
    }

    @Override
    protected void onDestroy() {
        Logger.getLogger().d(
                "============onDestory===============:" + this + ", isFinishing:" + isFinishing());
        if (mHeadView != null) {
            mHeadView.destroy();
        }
        super.onDestroy();
        Logger.getLogger().d(
                "============onDestory after===============:" + this + ", isFinishing:"
                        + isFinishing());
    }

    class MediaFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        public MediaFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % CONTENT.length;
            if (index == 1) {
                return MyPhotoFragment.newInstance();
            } else {
                return MyVideoFragment.newInstance();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(CONTENT[position % CONTENT.length]);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }
    }

}
