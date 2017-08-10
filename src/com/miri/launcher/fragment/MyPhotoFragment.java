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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.activity.PhotoViewerActivity;
import com.miri.launcher.adapter.PhotoListAdapter;
import com.miri.launcher.db.manage.MediaManager;
import com.miri.launcher.model.Folder;
import com.miri.launcher.model.Photo;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.view.TabPageView;

/**
 * 我的照片
 * @author zengjiantao
 */
public class MyPhotoFragment extends BaseFragment {

    private static final int FETCH_PHOTO_SUCCESS = 100;

    private static final int FETCH_PHOTO_FAILD = 101;

    private static final int COLUMNS = 6;

    private GridView mPhotoGrid;

    private List<Photo> mPhotos;

    private PhotoListAdapter mPhotoAdapter;

    private TextView mPhotoCountTxt;

    private LinearLayout mNoPhotoView;

    private TabPageView mTabPageView;

    private Handler mHandler;

    private View mLastSelectedView;

    public MyPhotoFragment() {
    }

    public static MyPhotoFragment newInstance() {
        MyPhotoFragment fragment = new MyPhotoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_photo, container, false);
        initiWidget(view);
        initHandler();
        initData();
        return view;
    }

    private void initiWidget(View view) {
        mPhotoGrid = (GridView) view.findViewById(R.id.photo_grid);
        mPhotoGrid.setOnKeyListener(mOnKey);
        mPhotoGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PhotoViewerActivity.class);
                Folder folder = new Folder();
                folder.setPhotos(mPhotos);
                intent.putExtra(Constants.KEY_FOLDER, folder);
                intent.putExtra(Constants.KEY_PHOTO_POSITION, position);
                startActivity(intent);
            }
        });
        mPhotoGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMovie(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mPhotoCountTxt = (TextView) view.findViewById(R.id.photo_count);
        mNoPhotoView = (LinearLayout) view.findViewById(R.id.no_photo_view);
    }

    private void initHandler() {
        mHandler = new Handler(new Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case FETCH_PHOTO_SUCCESS:
                        fillWidgetContent();
                        break;
                    case FETCH_PHOTO_FAILD:
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
                mPhotos = MediaManager.queryPhotos(getActivity());
                if (!Toolkit.isEmpty(mPhotos)) {
                    mHandler.sendEmptyMessage(FETCH_PHOTO_SUCCESS);
                } else {
                    mHandler.sendEmptyMessage(FETCH_PHOTO_FAILD);
                }
            }
        }).start();
    }

    private void showNoPhotoView() {
        mNoPhotoView.setVisibility(View.VISIBLE);
        mPhotoGrid.setVisibility(View.GONE);
        mPhotoCountTxt.setVisibility(View.INVISIBLE);
    }

    private void fillWidgetContent() {
        mPhotoAdapter = new PhotoListAdapter(getActivity(), R.layout.photo_grid_item, mPhotos);
        mPhotoGrid.setAdapter(mPhotoAdapter);
        mPhotoCountTxt.setText(getResources().getString(R.string.photo_count, mPhotos.size()));
        mNoPhotoView.setVisibility(View.GONE);
        mPhotoGrid.setVisibility(View.VISIBLE);
        mPhotoCountTxt.setVisibility(View.VISIBLE);
    }

    public void selectedMovie(View view) {
        if (mLastSelectedView != null) {
            TextView movieName = (TextView) mLastSelectedView.findViewById(R.id.photo_name);
            movieName.setTextColor(getResources().getColor(R.color.movie_name_unselected));
        }
        if (view != null) {
            TextView movieName = (TextView) view.findViewById(R.id.photo_name);
            movieName.setTextColor(getResources().getColor(R.color.movie_name_selected));
            mLastSelectedView = view;
        }
    }

    private OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (v.getId() == R.id.photo_grid && mPhotoGrid.getSelectedItemPosition() < COLUMNS
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
