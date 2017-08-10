package com.miri.launcher.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.miri.launcher.Constants;
import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.R;
import com.miri.launcher.adapter.MediaInfoListAdapter;
import com.miri.launcher.db.manage.MoretvManager;
import com.miri.launcher.market.DownloadService;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.utils.ApkUtil;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.dialog.LoadingDialog;

/**
 * 搜索结果
 * 
 * @author zengjiantao
 */
public class MovieListActivity extends BaseActivity {

    private static final int FETCH_MEDIA_SUCCESS = 100;

    private static final int FETCH_MEDIA_FAILD = 101;

    private int mListType;

    private String mSearchKey;

    private TextView mTitleTxt;

    private GridView mMovieGrid;

    private List<MediaInfo> mMediaInfos;

    private MediaInfoListAdapter mMediaInfoAdapter;

    private TextView mMovieCountTxt;

    private Handler mHandler;

    private DownloadService mDownloadService;

    private ServiceConnection mDlSvcConnection;

    private LoadingDialog mLoadingDlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

        Intent intent = getIntent();
        mListType = intent.getIntExtra(Constants.KEY_MOVIE_LIST_TYPE,
                Constants.MOVIE_LIST_TYPE_SEARCH);
        if (mListType == Constants.MOVIE_LIST_TYPE_SEARCH) {
            mSearchKey = getIntent().getStringExtra(Constants.KEY_SEARCH_KEY);
            Toast.makeText(this, mSearchKey, Toast.LENGTH_LONG).show();
        }

        initiWidget();
        initHandler();
        initData();
    }

    private void initiWidget() {
        mTitleTxt = (TextView) findViewById(R.id.movie_list_title);
        if (mListType == Constants.MOVIE_LIST_TYPE_SEARCH) {
            mTitleTxt.setText(getResources().getString(
                    R.string.search_result_title));
        } else if (mListType == Constants.MOVIE_LIST_TYPE_HISTORY) {
            mTitleTxt.setText(getResources().getString(R.string.history_title));
        } else if (mListType == Constants.MOVIE_LIST_TYPE_MOVIE) {
            mTitleTxt.setText(getResources().getString(R.string.movie));
        } else if (mListType == Constants.MOVIE_LIST_TYPE_TV) {
            mTitleTxt.setText(getResources().getString(R.string.tv));
        } else if (mListType == Constants.MOVIE_LIST_TYPE_CARTOON) {
            mTitleTxt.setText(getResources().getString(R.string.cartoon));
        } else if (mListType == Constants.MOVIE_LIST_TYPE_VARIETY) {
            mTitleTxt.setText(getResources().getString(R.string.variety));
        }

        mMovieGrid = (GridView) findViewById(R.id.movie_list_grid);
        mMovieGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                MediaInfo mediaInfo = mMediaInfoAdapter.getItem(position);
                toDetail(mediaInfo.getLinkData());
            }
        });
        mMovieGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                mMediaInfoAdapter.selectedMovie(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMovieCountTxt = (TextView) findViewById(R.id.movie_count);

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

    private void toDetail(String linkData) {
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

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                case FETCH_MEDIA_SUCCESS:
                    hideLoading();
                    if (!Toolkit.isEmpty(mMediaInfos)) {
                        fillWidget();
                    } else {
                        showToast("暂时没有您想要的数据！");
                    }
                    break;
                case FETCH_MEDIA_FAILD:
                    hideLoading();
                    showToast("加载数据出错！");
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
        new Thread(new Runnable() {

            @Override
            public void run() {
                switch (mListType) {
                case Constants.MOVIE_LIST_TYPE_SEARCH:
                    mMediaInfos = MoretvManager.search(mSearchKey);
                    Log.d("Debug", "mediaInfos:" + mMediaInfos);
                    break;
                case Constants.MOVIE_LIST_TYPE_HISTORY:
                    mMediaInfos = MoretvManager
                    .fetchHistory(MovieListActivity.this);
                    break;
                case Constants.MOVIE_LIST_TYPE_MOVIE:
                    mMediaInfos = MoretvManager.fetchPlatform(
                            MovieListActivity.this, MoretvConstants.PAGE_MOVIE);
                    if (mMediaInfos != null) {
                        MediaInfo moreMovie = new MediaInfo();
                        moreMovie.setMore(true);
                        moreMovie.setTitle(getResources().getString(
                                R.string.more));
                        moreMovie.setLinkData(MoretvConstants.LINKDATA_MOVIE);
                        mMediaInfos.add(moreMovie);
                    }
                    break;
                case Constants.MOVIE_LIST_TYPE_TV:
                    mMediaInfos = MoretvManager.fetchPlatform(
                            MovieListActivity.this, MoretvConstants.PAGE_TV);
                    if (mMediaInfos != null) {
                        MediaInfo moreTv = new MediaInfo();
                        moreTv.setMore(true);
                        moreTv.setTitle(getResources().getString(R.string.more));
                        moreTv.setLinkData(MoretvConstants.LINKDATA_TV);
                        mMediaInfos.add(moreTv);
                    }
                    break;
                case Constants.MOVIE_LIST_TYPE_CARTOON:
                    mMediaInfos = MoretvManager.fetchPlatform(
                            MovieListActivity.this, MoretvConstants.PAGE_COMIC);
                    if (mMediaInfos != null) {
                        MediaInfo moreCartoon = new MediaInfo();
                        moreCartoon.setMore(true);
                        moreCartoon.setTitle(getResources().getString(
                                R.string.more));
                        moreCartoon.setLinkData(MoretvConstants.LINKDATA_COMIC);
                        mMediaInfos.add(moreCartoon);
                    }
                    break;
                case Constants.MOVIE_LIST_TYPE_VARIETY:
                    mMediaInfos = MoretvManager
                    .fetchPlatform(MovieListActivity.this,
                            MoretvConstants.PAGE_ZONGYI);
                    if (mMediaInfos != null) {
                        MediaInfo moreVariety = new MediaInfo();
                        moreVariety.setMore(true);
                        moreVariety.setTitle(getResources().getString(
                                R.string.more));
                        moreVariety
                        .setLinkData(MoretvConstants.LINKDATA_ZONGYI);
                        mMediaInfos.add(moreVariety);
                    }
                    break;

                default:
                    break;
                }
                if (mMediaInfos != null) {
                    mHandler.sendEmptyMessage(FETCH_MEDIA_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(FETCH_MEDIA_FAILD);
                }
            }
        }).start();
    }

    private void fillWidget() {
        mMediaInfoAdapter = new MediaInfoListAdapter(this,
                R.layout.movie_item_vertical, mMediaInfos);
        mMediaInfoAdapter.setListType(mListType);
        mMovieGrid.setAdapter(mMediaInfoAdapter);
        mMovieGrid.requestFocus();
        mMovieGrid.setSelection(0);
        View view = mMovieGrid.getSelectedView();
        mMediaInfoAdapter.selectedMovie(view);

        mMovieCountTxt.setText(getResources().getString(R.string.result_count,
                mMediaInfos.size()));
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

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        unbindService(mDlSvcConnection);
        super.onDestroy();
    }
}
