/*
 * 文件名：CategoryFragment.java
 * 描述：
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miri.launcher.PersistData;
import com.miri.launcher.Constants;
import com.miri.launcher.MoretvConstants;
import com.miri.launcher.R;
import com.miri.launcher.activity.LauncherActivity;
import com.miri.launcher.activity.MovieListActivity;
import com.miri.launcher.activity.SearchActivity;
import com.miri.launcher.msg.model.AppNode;
import com.miri.launcher.msg.model.doc.AppRecommend;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.IconView;
import com.miri.launcher.view.PosterView;
import com.miri.launcher.view.TabPageView;

/**
 * 视频分类
 * @author penglin
 * @version 2013-6-24
 */
public class CategoryFragment extends BaseFragment {

    private IconView mSearch;

    private IconView mLive;

    private PosterView mMovie;

    private IconView mAll;

    private PosterView mTV;

    private PosterView mCartoon;

    private PosterView mvariety;

    private TabPageView mTabPageView;

    private LauncherActivity mParent;

    public CategoryFragment() {
    }

    /**
     * 创建一个fragment的launcher版式
     * @return
     */
    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        mTabPageView = (TabPageView) activity.findViewById(R.id.tab);
        if (activity instanceof LauncherActivity) {
            mParent = (LauncherActivity) activity;
        }
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category, container, false);
        mSearch = (IconView) view.findViewById(R.id.search);
        mLive = (IconView) view.findViewById(R.id.live);
        mMovie = (PosterView) view.findViewById(R.id.movie);
        mMovie.setAlawsShowInfo(true);
        mAll = (IconView) view.findViewById(R.id.all);
        mTV = (PosterView) view.findViewById(R.id.tv);
        mTV.setAlawsShowInfo(true);
        mCartoon = (PosterView) view.findViewById(R.id.cartoon);
        mCartoon.setAlawsShowInfo(true);
        mvariety = (PosterView) view.findViewById(R.id.variety);
        mvariety.setAlawsShowInfo(true);
        mMovie.setTitleText(R.string.movie);
        mTV.setTitleText(R.string.tv);
        mCartoon.setTitleText(R.string.cartoon);
        mvariety.setTitleText(R.string.variety);
        mMovie.setPosterDrawable(R.drawable.movie);
        mTV.setPosterDrawable(R.drawable.tv);
        mCartoon.setPosterDrawable(R.drawable.cartoon);
        mvariety.setPosterDrawable(R.drawable.variety);
        bindListener();
        return view;
    }

    private void bindListener() {
        mSearch.setOnKeyListener(mOnKey);
        mLive.setOnKeyListener(mOnKey);
        mvariety.setOnKeyListener(mOnKey);
        mSearch.setOnClickListener(mOnClick);
        mLive.setOnClickListener(mOnClick);
        mMovie.setOnClickListener(mOnClick);
        mAll.setOnClickListener(mOnClick);
        mTV.setOnClickListener(mOnClick);
        mCartoon.setOnClickListener(mOnClick);
        mvariety.setOnClickListener(mOnClick);
    }

    private final OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            boolean upView = (v == mSearch) || (v == mLive) || (v == mvariety);
            if (upView && (event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                if (mTabPageView != null && mTabPageView.getCurrTabView() != null) {
                    mTabPageView.getCurrTabView().requestFocus();
                }
            }
            return false;
        }
    };

    private final OnClickListener mOnClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    break;
                case R.id.live:
                    //                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                    //                        mParent.startApp(MoretvConstants.LIVE_PACKAGE_NAME,
                    //                                MoretvConstants.LIVE_APK_URL, MoretvConstants.LIVE_APK_NAME, null);
                    //                    } else {
                    //                        CustomToast.makeText(getActivity(), "网络未连接，请检查网络", Toast.LENGTH_LONG)
                    //                                .show();
                    //                    }
                    AppNode appRecommend = getLiveRecommendData();
                    if (appRecommend != null) {
                        Logger.getLogger().e(appRecommend.toString());
                        startApp(getActivity(), appRecommend.getPkgName(), appRecommend.getUrl(),
                                appRecommend.getName(), appRecommend.getPoster());
                    }
                    break;

                case R.id.movie:
                    toPlatformList(Constants.MOVIE_LIST_TYPE_MOVIE);
                    break;

                case R.id.all:
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        startApp(getActivity(), MoretvConstants.PACKAGE_NAME,
                                MoretvConstants.APK_URL, MoretvConstants.APK_NAME, null);
                    } else {
                        CustomToast.makeText(getActivity(), "网络未连接，请检查网络", Toast.LENGTH_LONG)
                                .show();
                    }
                    break;

                case R.id.tv:
                    toPlatformList(Constants.MOVIE_LIST_TYPE_TV);
                    break;

                case R.id.cartoon:
                    toPlatformList(Constants.MOVIE_LIST_TYPE_CARTOON);
                    break;

                case R.id.variety:
                    toPlatformList(Constants.MOVIE_LIST_TYPE_VARIETY);
                    break;

                default:
                    break;
            }

        }
    };

    /**
     * 跳转至分类页面
     */
    private void toPlatformList(int listType) {
        if (!NetworkUtil.isNetworkAvailable(getActivity())) {
            CustomToast.makeText(getActivity(), "网络未连接，请检查网络", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(getActivity(), MovieListActivity.class);
        intent.putExtra(Constants.KEY_MOVIE_LIST_TYPE, listType);
        startActivity(intent);
    }

    /**
     * @return
     */
    private AppNode getLiveRecommendData() {
        AppNode result = null;
        AppRecommend mainRec = PersistData.mAppRecommend;
        if (mainRec == null) {
            return null;
        }
        List<AppNode> appRecommend = mainRec.getLiveRecommends();
        if (!Toolkit.isEmpty(appRecommend)) {
            for (AppNode app: appRecommend) {
                if (!Toolkit.isEmpty(app.getPkgName()) && !Toolkit.isEmpty(app.getUrl())) {
                    result = app;
                    break;
                }
            }
            return result;
        }
        return null;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
