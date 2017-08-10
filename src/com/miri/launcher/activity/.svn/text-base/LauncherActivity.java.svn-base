/*
 * 文件名：LauncherActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.miri.launcher.Constants;
import com.miri.launcher.MoretvConstants;
import com.miri.launcher.PersistData;
import com.miri.launcher.R;
import com.miri.launcher.fragment.AppRecommendFragment;
import com.miri.launcher.fragment.CategoryFragment;
import com.miri.launcher.fragment.HomeFragment;
import com.miri.launcher.fragment.SystemSettingsFragment;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.moretv.MoretvParser;
import com.miri.launcher.moretv.model.LoginInfo;
import com.miri.launcher.receiver.AutoStartReceiver;
import com.miri.launcher.service.AppRecommendService;
import com.miri.launcher.service.CoreService;
import com.miri.launcher.service.HeartReportService;
import com.miri.launcher.upgrade.UpgradeHelper;
import com.miri.launcher.utils.AssetsCopyer;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.ResourceHelper;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.IconPagerAdapter;
import com.miri.launcher.view.LauncherHeadView;
import com.miri.launcher.view.PageIndicator;
import com.miri.launcher.view.TabPageView;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-3
 */
public class LauncherActivity extends BaseFragmentActivity {

    private LauncherHeadView mHeadView;

    private ImageView mLogoView;

    private TabPageView mTabPageView;

    private PageIndicator mIndicator;

    private ViewPager mViewPager;

    private LauncherFragmentAdapter mAdapter;

    private boolean isFirst = true;

    private boolean isDestoryed = false;

    private static final int RETRY_LOGIN = 1002;

    private static final int[] CONTENT = new int[] {R.string.home, R.string.video_category,
            R.string.my_app, R.string.settings};

    private Timer mCoreTimer;

    private Timer mAppRecTimer;

    private Timer mHeartTimer;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Logger.getLogger().v("msg:" + msg.what + ", isFinish:" + isFinishing());
            if (isFinishing() || isDestoryed) {
                return;
            }
            switch (msg.what) {
                case RETRY_LOGIN:
                    login();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ResourceHelper.updateConfig(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        findViewById(R.id.head).setVisibility(View.VISIBLE);
        mHeadView = new LauncherHeadView(this);
        mLogoView = (ImageView) findViewById(R.id.home_logo);
        if (PersistData.VERSION_TYPE == Constants.KONGGE) {
            mLogoView.setImageResource(R.drawable.kongge);
            mLogoView.setVisibility(View.VISIBLE);
        } else {
            mLogoView.setImageResource(R.drawable.miri_logo_1);
            mLogoView.setVisibility(View.VISIBLE);
        }
        mTabPageView = (TabPageView) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = mTabPageView.getTabPageIndicator();
        startService(new Intent(this, DownloadService.class));
        startTimer();
        if (!Toolkit.isExistApp(this, MoretvConstants.PACKAGE_NAME)) {
            new AssetsCopyer(this).start();
        }
        login();
    }

    private void init() {
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new LauncherFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        new UpgradeHelper().doUpgrade(this, PersistData.getUpgradeUrl(), false, null);
    }

    /**
     * 防止某些终端未监听系统开机广播，采用定时器
     */
    private void startTimer() {
        // 核心消息定时器
        mCoreTimer = new Timer();
        TimerTask coreTimerTask = new TimerTask() {

            @Override
            public void run() {
                Intent it = new Intent(LauncherActivity.this, CoreService.class);
                startService(it);

            }

        };
        mCoreTimer.schedule(coreTimerTask, 0, AutoStartReceiver.CORESERVICE_PERIOD);
        // 应用数据推荐定时器
        mAppRecTimer = new Timer();
        TimerTask appRecTimerTask = new TimerTask() {

            @Override
            public void run() {
                Intent it = new Intent(LauncherActivity.this, AppRecommendService.class);
                startService(it);

            }

        };
        mAppRecTimer.schedule(appRecTimerTask, 0, AutoStartReceiver.APPRECOMMEND_PERIOD);
        //心跳轮询消息定时器
        mHeartTimer = new Timer();
        TimerTask heartTimerTask = new TimerTask() {

            @Override
            public void run() {
                Intent it = new Intent(LauncherActivity.this, HeartReportService.class);
                startService(it);

            }

        };
        mHeartTimer.schedule(heartTimerTask, AutoStartReceiver.HEART_DELAY,
                AutoStartReceiver.HEART_INTERVAL);
    }

    /**
     * 登录电视猫
     */
    private void login() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    LoginInfo loginInfo = MoretvParser.login(NetworkUtil
                            .getMac(LauncherActivity.this));
                    if (loginInfo != null && loginInfo.getStatus() == MoretvConstants.LOGIN_SUCCESS) {
                        Logger.getLogger().d("Login MoreTv success! " + loginInfo);
                    } else {
                        Logger.getLogger().d("Login MoreTv faild!");
                        mHandler.sendEmptyMessage(RETRY_LOGIN);
                    }
                } catch (Exception e) {
                    Logger.getLogger().e("Login MoreTv faild!");
                    mHandler.sendEmptyMessage(RETRY_LOGIN);
                    e.printStackTrace();
                }
            }
        }).start();
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
    public void onBackPressed() {
        // super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Logger.getLogger().e(
                "============onDestory===============:" + this + ", isFinishing:" + isFinishing());
        isDestoryed = true;
        if (mHeadView != null) {
            mHeadView.destroy();
        }
        mHandler.removeCallbacksAndMessages(null);
        if (mAppRecTimer != null) {
            mAppRecTimer.cancel();
        }
        if (mCoreTimer != null) {
            mAppRecTimer.cancel();
        }
        if (mHeartTimer != null) {
            mHeartTimer.cancel();
        }
        //        unbindService(dlSvcConnection);
        super.onDestroy();
        Logger.getLogger().e(
                "============onDestory after===============:" + this + ", isFinishing:"
                        + isFinishing());
    }

    class LauncherFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

        public LauncherFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % CONTENT.length;
            if (index == 1) {
                return CategoryFragment.newInstance();
            } else if (index == 2) {
                return AppRecommendFragment.newInstance();
            } else if (index == 3) {
                return SystemSettingsFragment.newInstance();
            } else {
                return HomeFragment.newInstance();
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
