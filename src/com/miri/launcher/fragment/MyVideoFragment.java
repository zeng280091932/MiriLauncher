package com.miri.launcher.fragment;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.activity.VideoPlayerActivity;
import com.miri.launcher.adapter.VideoListAdapter;
import com.miri.launcher.db.manage.MediaManager;
import com.miri.launcher.model.Video;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.TabPageView;

/**
 * 我的照片
 * @author zengjiantao
 */
public class MyVideoFragment extends BaseFragment {

    private static final int FETCH_VIDEO_SUCCESS = 100;

    private static final int FETCH_VIDEO_FAILD = 101;

    private static final int COLUMNS = 6;

    private GridView mVideoGrid;

    private List<Video> mVideos;

    private VideoListAdapter mVideoAdapter;

    private TextView mVideoCountTxt;

    private ImageView mNoVideoView;

    private TabPageView mTabPageView;

    private Handler mHandler;

    private View mLastSelectedView;

    public MyVideoFragment() {
    }

    public static MyVideoFragment newInstance() {
        MyVideoFragment fragment = new MyVideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_video, container, false);
        initWidget(view);
        initHandler();
        initData();
        return view;
    }

    private void initWidget(View view) {
        mVideoGrid = (GridView) view.findViewById(R.id.video_grid);
        mVideoGrid.setOnKeyListener(mOnKey);
        mVideoGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra(Constants.KEY_VIDEO_POSITION, position);
                startActivity(intent);
            }
        });
        mVideoGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMovie(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mVideoCountTxt = (TextView) view.findViewById(R.id.video_count);
        mNoVideoView = (ImageView) view.findViewById(R.id.no_video_view);
    }

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case FETCH_VIDEO_SUCCESS:
                        fillWidget();
                        break;
                    case FETCH_VIDEO_FAILD:
                        showNoPhotoView();
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
                mVideos = MediaManager.queryVideos(getActivity());
                if (!Toolkit.isEmpty(mVideos)) {
                    mHandler.sendEmptyMessage(FETCH_VIDEO_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(FETCH_VIDEO_FAILD);
                }
            }
        }).start();

    }

    private void showNoPhotoView() {
        mNoVideoView.setVisibility(View.VISIBLE);
        mVideoGrid.setVisibility(View.GONE);
        mVideoCountTxt.setVisibility(View.INVISIBLE);
    }

    private void fillWidget() {
        mVideoAdapter = new VideoListAdapter(getActivity(), R.layout.video_grid_item, mVideos);
        mVideoGrid.setAdapter(mVideoAdapter);
        mVideoCountTxt.setText(getResources().getString(R.string.video_count, mVideos.size()));
        mNoVideoView.setVisibility(View.GONE);
        mVideoGrid.setVisibility(View.VISIBLE);
        mVideoCountTxt.setVisibility(View.VISIBLE);
    }

    public void selectedMovie(View view) {
        if (mLastSelectedView != null) {
            TextView movieName = (TextView) mLastSelectedView.findViewById(R.id.video_name);
            movieName.setTextColor(getResources().getColor(R.color.movie_name_unselected));
        }
        if (view != null) {
            TextView movieName = (TextView) view.findViewById(R.id.video_name);
            movieName.setTextColor(getResources().getColor(R.color.movie_name_selected));
            mLastSelectedView = view;
        }
    }

    private OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getId() == R.id.video_grid && mVideoGrid.getSelectedItemPosition() < COLUMNS
                    && (event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_DPAD_UP)) {
                if (mTabPageView != null && mTabPageView.getCurrTabView() != null) {
                    mTabPageView.getCurrTabView().requestFocus();
                }
            }
            return false;
        }
    };

    @Override
    public void onAttach(Activity activity) {
        mTabPageView = (TabPageView) activity.findViewById(R.id.tab);
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
