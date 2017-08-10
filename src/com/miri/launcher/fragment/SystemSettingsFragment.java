/* 
 * 文件名：VideoCategoryFragment.java
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.miri.launcher.R;
import com.miri.launcher.activity.AboutActivity;
import com.miri.launcher.activity.CheckUpgradeActivity;
import com.miri.launcher.activity.NetworkManagerActivity;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.TabPageView;

/**
 * 视频分类
 * @author penglin
 * @version 2013-6-24
 */
public class SystemSettingsFragment extends BaseFragment {

    private RelativeLayout mNetWork;

    private RelativeLayout mSystemUpdate;

    private RelativeLayout mSettings;

    private RelativeLayout mAboutUs;

    private TabPageView mTabPageView;

    public SystemSettingsFragment() {
    }

    /**
     * 创建一个fragment的launcher版式
     * @return
     */
    public static SystemSettingsFragment newInstance() {
        SystemSettingsFragment fragment = new SystemSettingsFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTabPageView = (TabPageView) activity.findViewById(R.id.tab);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.system_setting, container, false);
        mNetWork = (RelativeLayout) view.findViewById(R.id.network_settings);
        mSystemUpdate = (RelativeLayout) view.findViewById(R.id.update);
        mSettings = (RelativeLayout) view.findViewById(R.id.settings);
        mAboutUs = (RelativeLayout) view.findViewById(R.id.aboutus);
        bindListener();
        return view;
    }

    private void bindListener() {
        mNetWork.setOnKeyListener(mOnKey);
        mSystemUpdate.setOnKeyListener(mOnKey);
        mSettings.setOnKeyListener(mOnKey);
        mAboutUs.setOnKeyListener(mOnKey);
        mNetWork.setOnClickListener(mOnClick);
        mSystemUpdate.setOnClickListener(mOnClick);
        mSettings.setOnClickListener(mOnClick);
        mAboutUs.setOnClickListener(mOnClick);
        mNetWork.setOnFocusChangeListener(mOnFocus);
        mSystemUpdate.setOnFocusChangeListener(mOnFocus);
        mSettings.setOnFocusChangeListener(mOnFocus);
        mAboutUs.setOnFocusChangeListener(mOnFocus);
    }

    private OnFocusChangeListener mOnFocus = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            View view = (View) v.getParent();
            if (view != null) {
                if (hasFocus) {
                    view.bringToFront();
                    view.setBackgroundResource(R.drawable.select_bg);
                } else {
                    view.setBackgroundDrawable(null);
                }
            }
        }
    };

    private OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            boolean upView = (v == mNetWork) || (v == mSystemUpdate) || (v == mSettings)
                    || (v == mAboutUs);
            if (upView && (event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                if (mTabPageView != null && mTabPageView.getCurrTabView() != null) {
                    mTabPageView.getCurrTabView().requestFocus();
                }
            }
            return false;
        }
    };

    private OnClickListener mOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.network_settings:
                    startActivity(new Intent(getActivity(), NetworkManagerActivity.class));
                    break;
                case R.id.update:
                    startActivity(new Intent(getActivity(), CheckUpgradeActivity.class));
                    break;
                case R.id.settings:
                    Toolkit.startNetworkSettings(getActivity());
                    break;
                case R.id.aboutus:
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
