/* 
 * 文件名：DownloadManagerActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.miri.launcher.R;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-12
 */
@SuppressWarnings("deprecation")
public class NetworkManagerActivity extends BaseActivityGroup {

    private FrameLayout mTabContent;

    private TabHost mTabHost;

    protected TabWidget mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_manager);
        mTabHost = (TabHost) findViewById(R.id.tab_host);
        mTabs = (TabWidget) findViewById(android.R.id.tabs);
        mTabContent = (FrameLayout) findViewById(android.R.id.content);
        mTabHost.setup();
        mTabHost.getTabWidget().setOrientation(LinearLayout.VERTICAL);

        Intent intent = new Intent(this, WifiScanActivity.class);
        createTabHost("wifi", "Wi-Fi", R.drawable.wifi_icon_selector, intent);

        //设置默认选择tab项位置
        View view = mTabs.getChildAt(0);
        if (view != null) {
            view.requestFocus();
        }
        mTabHost.setCurrentTab(0);
    }

    /**
     * 构造tab项
     * @param tag
     * @param tabTitle
     * @param intent
     */
    public void createTabHost(String tag, String tabTitle, int iconId, Intent intent) {
        mTabHost.setup(getLocalActivityManager());
        mTabHost.addTab(mTabHost.newTabSpec(tag)
                .setIndicator(createIndicatorView(tabTitle.trim(), iconId)).setContent(intent));
    }

    // 自定义的tab栏
    private View createIndicatorView(String tabTitle, int iconId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tabIndicator = inflater.inflate(R.layout.down_manage_tab_indicator,
                mTabHost.getTabWidget(), false);
        TextView titleView = (TextView) tabIndicator.findViewById(R.id.tab_title);
        titleView.setText(tabTitle.trim());
        ImageView imageView = (ImageView) tabIndicator.findViewById(R.id.tab_imageView);
        imageView.setImageResource(iconId);
        return tabIndicator;
    }

}
