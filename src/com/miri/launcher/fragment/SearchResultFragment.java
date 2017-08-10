package com.miri.launcher.fragment;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.R;
import com.miri.launcher.activity.SearchResultActivity;
import com.miri.launcher.adapter.MediaInfoListAdapter;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.view.TabPageView;

/**
 * 我的照片
 * @author zengjiantao
 */
public class SearchResultFragment extends BaseFragment {

    private SearchResultActivity mParent;

    private GridView mResultGrid;

    private List<MediaInfo> mMediaInfos;

    private MediaInfoListAdapter mResultAdapter;

    private TextView mResultCountTxt;

    private TabPageView mTabPageView;

    private int mShowMode;

    public SearchResultFragment() {
    }

    public static SearchResultFragment newInstance(List<MediaInfo> mediaInfos, int showMode) {
        SearchResultFragment fragment = new SearchResultFragment();
        fragment.mMediaInfos = mediaInfos;
        fragment.mShowMode = showMode;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (mShowMode == Constants.SHOW_MODE_VERTICAL) {
            view = inflater.inflate(R.layout.search_result_list_vertical, container, false);
        } else {
            view = inflater.inflate(R.layout.search_result_list_horizontal, container, false);
        }
        initiWidget(view);
        fillWidgetContent();
        return view;
    }

    private void initiWidget(View view) {
        mResultGrid = (GridView) view.findViewById(R.id.search_result_grid);
        mResultGrid.setOnKeyListener(mOnKey);
        mResultGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaInfo mediaInfo = mResultAdapter.getItem(position);
                mParent.toDetail(mediaInfo.getLinkData());
            }
        });
        mResultGrid.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mResultAdapter.selectedMovie(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mResultCountTxt = (TextView) view.findViewById(R.id.result_count);
    }

    private void fillWidgetContent() {
        if (mShowMode == Constants.SHOW_MODE_VERTICAL) {
            mResultAdapter = new MediaInfoListAdapter(getActivity(), R.layout.movie_item_vertical,
                    mMediaInfos);
        } else {
            mResultAdapter = new MediaInfoListAdapter(getActivity(),
                    R.layout.movie_item_horizontal, mMediaInfos);
        }

        mResultGrid.setAdapter(mResultAdapter);
        mResultCountTxt
                .setText(getResources().getString(R.string.result_count, mMediaInfos.size()));
    }

    private final OnKeyListener mOnKey = new OnKeyListener() {

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            int columns = 0;
            if (mShowMode == Constants.SHOW_MODE_HORIZONTAL) {
                columns = 6;
            } else {
                columns = 7;
            }
            if (v.getId() == R.id.search_result_grid
                    && mResultGrid.getSelectedItemPosition() < columns
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
        if (activity instanceof SearchResultActivity) {
            mParent = (SearchResultActivity) activity;
        }
        mTabPageView = (TabPageView) activity.findViewById(R.id.tab);
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
