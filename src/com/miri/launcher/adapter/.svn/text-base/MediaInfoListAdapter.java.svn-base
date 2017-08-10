package com.miri.launcher.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miri.launcher.Constants;
import com.miri.launcher.MoretvConstants;
import com.miri.launcher.R;
import com.miri.launcher.activity.MovieListActivity;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.imageCache.ImageLoader.ImgCallback;
import com.miri.launcher.moretv.MediaInfoUtil;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.utils.Toolkit;

/**
 * 电视猫视频适配器
 * 
 * @author zengjiantao
 * 
 */
public class MediaInfoListAdapter extends ArrayAdapter<MediaInfo> {

    private final Context mContext;

    private LayoutInflater mInflater = null;

    private final int mItemTheme;

    private final ImageLoader mImgLoader;

    private boolean mIsFirst = true;

    private View mLastSelectedView;

    private int mListType;

    public MediaInfoListAdapter(Context context, int itemTheme,
            List<MediaInfo> mediaInfos) {
        super(context, itemTheme, mediaInfos);
        mContext = context;
        mItemTheme = itemTheme;
        mInflater = LayoutInflater.from(context);
        mImgLoader = ImageLoader.from(context);
    }

    public class Entity {

        public ImageView movieImg;

        public ImageView hdIcon;

        public TextView movieName;

        public TextView movieDesc;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entity entity;
        if (convertView == null) {
            entity = new Entity();
            convertView = mInflater.inflate(mItemTheme, null);
            entity.movieImg = (ImageView) convertView
                    .findViewById(R.id.movie_img);
            entity.hdIcon = (ImageView) convertView.findViewById(R.id.hd_icon);
            entity.movieName = (TextView) convertView
                    .findViewById(R.id.movie_name);
            entity.movieDesc = (TextView) convertView
                    .findViewById(R.id.media_desc);
            convertView.setTag(entity);
        } else {
            entity = (Entity) convertView.getTag();
        }

        MediaInfo mediaInfo = getItem(position);
        String name = mediaInfo.getTitle();
        if (Toolkit.isEmpty(name)) {
            name = mediaInfo.getItemTitle();
        }
        entity.movieName.setText(name);
        if (mediaInfo.isMore()) {
            entity.movieImg.setImageResource(R.drawable.movie_more);
            entity.hdIcon.setVisibility(View.GONE);
            entity.movieDesc.setText("");
        } else {
            String isHd = mediaInfo.getIsHd();
            if (!Toolkit.isEmpty(isHd) && isHd.equals(MoretvConstants.HD_YES)) {
                entity.hdIcon.setVisibility(View.VISIBLE);
            } else {
                entity.hdIcon.setVisibility(View.GONE);
            }
            if (mListType == Constants.MOVIE_LIST_TYPE_HISTORY) {
                entity.movieDesc.setText(MediaInfoUtil.createDesc(mediaInfo,
                        true));
            } else {
                entity.movieDesc.setText(MediaInfoUtil.createDesc(mediaInfo));
            }
            String url = mediaInfo.getImage1();
            if (Toolkit.isEmpty(url)) {
                url = mediaInfo.getIcon1();
            }
            entity.movieImg.setImageResource(R.drawable.movie_default);
            mImgLoader.loadImg(entity.movieImg, url, R.drawable.movie_default,
                    new ImgCallback() {

                @Override
                public void refresh(Object view, Bitmap bitmap,
                        boolean isFromNet) {
                    ImageView imageView = (ImageView) view;
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView
                        .setImageResource(R.drawable.movie_default);
                    }
                }
            });
        }

        if (mIsFirst && position == 0) {
            if (mContext instanceof MovieListActivity) {
                selectedMovie(convertView);
            }
            mIsFirst = false;
        }
        return convertView;
    }

    public void selectedMovie(View view) {
        if (mLastSelectedView != null) {
            TextView movieName = (TextView) mLastSelectedView
                    .findViewById(R.id.movie_name);
            TextView movieDesc = (TextView) mLastSelectedView
                    .findViewById(R.id.media_desc);
            movieName.setTextColor(mContext.getResources().getColor(
                    R.color.movie_name_unselected));
            if (!Toolkit.isEmpty(movieDesc.getText().toString())) {
                movieDesc.setVisibility(View.GONE);
            }
        }
        if (view != null) {
            TextView movieName = (TextView) view.findViewById(R.id.movie_name);
            TextView movieDesc = (TextView) view.findViewById(R.id.media_desc);
            movieName.setTextColor(mContext.getResources().getColor(
                    R.color.movie_name_selected));
            if (!Toolkit.isEmpty(movieDesc.getText().toString())) {
                movieDesc.setVisibility(View.VISIBLE);
            }
            mLastSelectedView = view;
        }
    }

    public void setListType(int listType) {
        mListType = listType;
    }

}
