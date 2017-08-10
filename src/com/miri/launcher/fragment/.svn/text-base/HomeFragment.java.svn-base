/*
 * 文件名：HomeFragment.java
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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.activity.LauncherActivity;
import com.miri.launcher.activity.MovieListActivity;
import com.miri.launcher.db.manage.MoretvManager;
import com.miri.launcher.db.manage.RecommendDBHelper;
import com.miri.launcher.moretv.MediaInfoUtil;
import com.miri.launcher.moretv.RecommendUpdater;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.utils.IconUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.IconView;
import com.miri.launcher.view.PagerPosterView;
import com.miri.launcher.view.PagerPosterView.OnReClickLisitener;
import com.miri.launcher.view.PosterView;
import com.miri.launcher.view.TabPageView;

/**
 * 首页
 * @author penglin
 * @version 2013-6-24
 */
public class HomeFragment extends BaseFragment {

    private static final int FETCH_RECOMMEND_SUCCESS = 100;

    private static final int FETCH_RECOMMEND_FAILD = 101;

    private final Logger log = Logger.getLogger();

    private IconView mPlayRecord;

    private PosterView mLongPoster;

    private PosterView mNormalPoster1;

    private PosterView mNormalPoster2;

    private PosterView mNormalPoster3;

    private PagerPosterView mPagerPoster;

    private TabPageView mTabPageView;

    private List<MediaInfo> mMediaInfos;

    private Handler mHandler;

    private RecommendDBHelper mDbHelper;

    private RecommendUpdater mRecommendUpdater;

    private LauncherActivity mParent;

    private RecommendReceiver mRecommendReceiver;

    public HomeFragment() {
    }

    /**
     * 创建一个fragment的launcher版式
     * @return
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.home, container, false);
        initWidget(view);
        initHandler();
        initData();
        return view;
    }

    private void initWidget(View view) {
        mPlayRecord = (IconView) view.findViewById(R.id.play_record);
        mLongPoster = (PosterView) view.findViewById(R.id.image_long);
        mNormalPoster1 = (PosterView) view.findViewById(R.id.image_noraml_1);
        mNormalPoster2 = (PosterView) view.findViewById(R.id.image_normal_2);
        mNormalPoster3 = (PosterView) view.findViewById(R.id.image_normal_3);
        mPagerPoster = (PagerPosterView) view.findViewById(R.id.image_big);

        mDbHelper = RecommendDBHelper.getInstance(getActivity());
        mRecommendReceiver = new RecommendReceiver();
        IntentFilter filter = new IntentFilter(RecommendUpdater.RECOMMEND_ACTION);
        getActivity().registerReceiver(mRecommendReceiver, filter);
    }

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case FETCH_RECOMMEND_SUCCESS:
                        fillWidget();
                        if (mRecommendUpdater == null) {
                            mRecommendUpdater = new RecommendUpdater(getActivity());
                            mRecommendUpdater.start();
                        }
                        break;
                    case FETCH_RECOMMEND_FAILD:
                        CustomToast.makeText(getActivity(), "加载数据出错！", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // mMediaInfos = MoretvManager.fetchRecommend();
                mMediaInfos = mDbHelper.findAll();
                if (Toolkit.isEmpty(mMediaInfos)) {
                    mMediaInfos = MoretvManager.fetchDefaultRecommend(getActivity());
                }
                if (!Toolkit.isEmpty(mMediaInfos)) {
                    mHandler.sendEmptyMessage(FETCH_RECOMMEND_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(FETCH_RECOMMEND_FAILD);
                }
            }
        }).start();

    }

    private void fillWidget() {

        int size = mMediaInfos.size();
        if (size >= 4) {
            for (int i = 0; i < 4; i++) {
                MediaInfo mediaInfo = mMediaInfos.get(i);
                if (mediaInfo != null) {

                    switch (i) {
                        case 0:
                            fillPosterView(mediaInfo, mLongPoster);
                            break;
                        case 1:
                            fillPosterView(mediaInfo, mNormalPoster1);
                            break;
                        case 2:
                            fillPosterView(mediaInfo, mNormalPoster2);
                            break;
                        case 3:
                            fillPosterView(mediaInfo, mNormalPoster3);
                            break;

                        default:
                            break;
                    }
                }
            }
        }
        if (size >= 7) {
            int end = mMediaInfos.size();
            if (end > 10) {
                end = 10;
            }
            List<MediaInfo> mediaInfos = mMediaInfos.subList(4, end);
            mPagerPoster.setInitMediaInfo(mediaInfos, true, true);
        }
    }

    private void fillPosterView(MediaInfo mediaInfo, PosterView posterView) {
        String url = mediaInfo.getImage1();
        String title = mediaInfo.getTitle();
        boolean customIcon = mediaInfo.isCustomIcon();
        String iconResource = mediaInfo.getIconResource();
        posterView.setTag(mediaInfo);
        posterView.setTitleText(title);
        posterView.setDescText(MediaInfoUtil.createDesc(mediaInfo));

        if (customIcon) {
            Intent.ShortcutIconResource iconRes = new Intent.ShortcutIconResource();
            iconRes.packageName = getActivity().getPackageName();
            iconRes.resourceName = getActivity().getPackageName() + ":" + iconResource;
            posterView.setPosterDrawable(IconUtil.getShortcutIncoResource(getActivity(), iconRes));
        } else {
            if (!Toolkit.isEmpty(url)) {
                posterView.setPosterUrl(url, R.drawable.movie_default);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPlayRecord.setOnKeyListener(mOnKey);
        mLongPoster.setOnKeyListener(mOnKey);
        mPagerPoster.setOnKeyListener(mOnKey);
        mPlayRecord.setOnClickListener(mOnClick);
        mLongPoster.setOnClickListener(mOnClick);
        mNormalPoster3.setOnClickListener(mOnClick);
        mNormalPoster2.setOnClickListener(mOnClick);
        mNormalPoster1.setOnClickListener(mOnClick);
        mPagerPoster.setOnReClickLisitener(new OnReClickLisitener() {

            @Override
            public void onClick(View currView) {
                MediaInfo mediaLong = (MediaInfo) currView.getTag();
                toDetail(getActivity(), mediaLong.getLinkData());
            }
        });
    }

    private final OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            boolean upView = (v == mPlayRecord) || (v == mLongPoster) || (v == mPagerPoster);
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
                case R.id.play_record:
                    if (NetworkUtil.isNetworkAvailable(getActivity())) {
                        Intent intent = new Intent(getActivity(), MovieListActivity.class);
                        intent.putExtra(Constants.KEY_MOVIE_LIST_TYPE,
                                Constants.MOVIE_LIST_TYPE_HISTORY);
                        startActivity(intent);
                    } else {
                        CustomToast.makeText(getActivity(), "网络未连接，请检查网络", Toast.LENGTH_LONG)
                                .show();
                    }
                    break;
                case R.id.image_long:
                    MediaInfo mediaLong = (MediaInfo) mLongPoster.getTag();
                    toDetail(getActivity(), mediaLong.getLinkData());
                    break;
                case R.id.image_noraml_1:
                    MediaInfo mediaNor1 = (MediaInfo) mNormalPoster1.getTag();
                    toDetail(getActivity(), mediaNor1.getLinkData());
                    break;
                case R.id.image_normal_2:
                    MediaInfo mediaNor2 = (MediaInfo) mNormalPoster2.getTag();
                    toDetail(getActivity(), mediaNor2.getLinkData());
                    break;
                case R.id.image_normal_3:
                    MediaInfo mediaNor3 = (MediaInfo) mNormalPoster3.getTag();
                    toDetail(getActivity(), mediaNor3.getLinkData());
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mRecommendReceiver != null) {
            getActivity().unregisterReceiver(mRecommendReceiver);
        }
        super.onDestroyView();
    };

    @Override
    public void onDestroy() {
        if (mRecommendUpdater != null) {
            mRecommendUpdater.stop();
            mRecommendUpdater = null;
        }
        super.onDestroy();
        Log.d("Debug", "fragement ondestory after...getActivity:" + getActivity());
    }

    /**
     * 接收应用更新广播
     * @author penglin
     * @version HDMNV100R001, 2013-6-24
     */
    class RecommendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Debug", "update recomends");
            initData();
        }
    }
}
