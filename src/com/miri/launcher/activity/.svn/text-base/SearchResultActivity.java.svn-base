/*
 * 文件名：LauncherActivity.java
 * 版权：Copyright
 */
package com.miri.launcher.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.miri.launcher.Constants;
import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.R;
import com.miri.launcher.db.manage.MoretvManager;
import com.miri.launcher.fragment.SearchResultFragment;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.moretv.model.doc.SearchResultDocument;
import com.miri.launcher.utils.ApkUtil;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.IconPagerAdapter;
import com.miri.launcher.view.PageIndicator;
import com.miri.launcher.view.TabPageView;
import com.miri.launcher.view.dialog.LoadingDialog;

/**
 * 我的媒体
 * 
 * @author zengjiantao
 * @version TVLAUNCHER001, 2013-9-7
 */
public class SearchResultActivity extends BaseFragmentActivity {

    // private LauncherHeadView mHeadView;

    private static final int SEARCH_SUCCESS = 100;

    private static final int SEARCH_FAILD = 101;

    private TabPageView mTabPageView;

    private PageIndicator mIndicator;

    private ViewPager mViewPager;

    private SearchResultFragmentAdapter mAdapter;

    private String mSearchKey;

    private List<Integer> mContent;

    private List<List<MediaInfo>> mDatas;

    private Handler mHandler;

    private LoadingDialog mLoadingDlg;

    private DownloadService mDownloadService;

    private ServiceConnection mDlSvcConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);

        initWdiget();
        initHandler();
        initData();
    }

    private void initWdiget() {
        // mHeadView = new LauncherHeadView(this);
        mTabPageView = (TabPageView) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = mTabPageView.getTabPageIndicator();

        mDlSvcConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDownloadService = ((DownloadService.MyBinder) service)
                        .getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        // Bind the DownloadService.
        bindService(new Intent(this, DownloadService.class), mDlSvcConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case SEARCH_SUCCESS:
                    hideLoading();
                    fillWidget();
                    break;
                case SEARCH_FAILD:
                    hideLoading();
                    CustomToast.makeText(getApplicationContext(), "没找到您想要的影片！",
                            Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
                }
                return false;
            }
        });
    }

    private void initData() {

        showLoading();
        mSearchKey = getIntent().getStringExtra(Constants.KEY_SEARCH_KEY);
        new Thread(new Runnable() {

            @Override
            public void run() {
                SearchResultDocument result = MoretvManager
                        .searchDocument(mSearchKey);
                if (result != null) {
                    mDatas = new ArrayList<List<MediaInfo>>();
                    mContent = new ArrayList<Integer>();
                    List<MediaInfo> movietv = result.getMovietv();
                    if (!Toolkit.isEmpty(movietv)) {
                        mDatas.add(movietv);
                        mContent.add(R.string.movietv);
                    }
                    List<MediaInfo> zongyi = result.getZongyi();
                    if (!Toolkit.isEmpty(zongyi)) {
                        mDatas.add(zongyi);
                        mContent.add(R.string.variety);
                    }
                    List<MediaInfo> comickids = result.getComickids();
                    if (!Toolkit.isEmpty(comickids)) {
                        mDatas.add(comickids);
                        mContent.add(R.string.cartoon);
                    }
                    List<MediaInfo> mv = result.getMv();
                    if (!Toolkit.isEmpty(mv)) {
                        mDatas.add(mv);
                        mContent.add(R.string.mv);
                    }
                    List<MediaInfo> hot = result.getHot();
                    if (!Toolkit.isEmpty(hot)) {
                        mDatas.add(hot);
                        mContent.add(R.string.hot);
                    }
                }
                if (!Toolkit.isEmpty(mDatas) && !Toolkit.isEmpty(mContent)
                        && mDatas.size() == mContent.size()) {
                    mHandler.sendEmptyMessage(SEARCH_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(SEARCH_FAILD);
                }
            }
        }).start();
    }

    private void fillWidget() {
        mViewPager.setOffscreenPageLimit(1);
        mAdapter = new SearchResultFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mTabPageView.getCurrTabView().requestFocus();
    }

    /**
     * 显示加载提示
     */
    public void showLoading() {
        if (mLoadingDlg == null) {
            mLoadingDlg = new LoadingDialog(this);
        }
        mLoadingDlg.show();
    }

    /**
     * 隐藏加载提示
     */
    public void hideLoading() {
        if (mLoadingDlg != null) {
            mLoadingDlg.dismiss();
        }
    }

    public void toDetail(String linkData) {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            CustomToast.makeText(this, "网络未连接，请检查网络", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("moretv.service.action");
            if (MoretvData.sLoginInfo != null) {
                intent.putExtra("UserID", MoretvData.sLoginInfo.getUserId());
                intent.putExtra("Token", MoretvData.sLoginInfo.getToken());
            }
            intent.putExtra("Data", linkData);
            intent.putExtra("launcher", 1);
            intent.putExtra("ReturnMode", 0);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            boolean installExist = ApkUtil.installExist(this,
                    MoretvConstants.FILE_NAME);
            Log.d("Debug", "installExist:" + installExist);
            if (!installExist && mDownloadService != null) {
                mDownloadService.createDownload(MoretvConstants.APK_URL, null,
                        MoretvConstants.APK_NAME, null, true, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        // if (mHeadView != null) {
        // mHeadView.destroy();
        // }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        unbindService(mDlSvcConnection);
        super.onDestroy();
    }

    class SearchResultFragmentAdapter extends FragmentPagerAdapter implements
    IconPagerAdapter {

        public SearchResultFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % mContent.size();
            if (mContent.get(index) == R.string.hot
                    || mContent.get(index) == R.string.mv) {
                return SearchResultFragment.newInstance(mDatas.get(index),
                        Constants.SHOW_MODE_HORIZONTAL);
            } else {
                return SearchResultFragment.newInstance(mDatas.get(index),
                        Constants.SHOW_MODE_VERTICAL);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(mContent.get(position % mContent.size()));
        }

        @Override
        public int getCount() {
            return mContent.size();
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }
    }

}
