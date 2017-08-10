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

import com.miri.launcher.R;
import com.miri.launcher.model.Video;
import com.miri.launcher.utils.Toolkit;
import com.miri.launcher.utils.VideoImgLoader;
import com.miri.launcher.utils.VideoImgLoader.ImgCallback;

public class VideoListAdapter extends ArrayAdapter<Video> {

    private static final int THUMB_WIDTH = 171;

    private static final int THUMB_HEIGHT = 111;

    private final Context mContext;

    private LayoutInflater mInflater = null;

    private final int mItemTheme;

    private final VideoImgLoader mImgLoader;

    public VideoListAdapter(Context context, int itemTheme, List<Video> videos) {
        super(context, itemTheme, videos);
        mContext = context;
        mItemTheme = itemTheme;
        mInflater = LayoutInflater.from(context);
        mImgLoader = new VideoImgLoader(context);
    }

    public class Entity {

        public ImageView videoImg;

        public TextView videoName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entity entity;
        if (convertView == null) {
            entity = new Entity();
            convertView = mInflater.inflate(mItemTheme, null);
            entity.videoImg = (ImageView) convertView
                    .findViewById(R.id.video_img);
            entity.videoName = (TextView) convertView
                    .findViewById(R.id.video_name);
            convertView.setTag(entity);
        } else {
            entity = (Entity) convertView.getTag();
        }

        Video video = getItem(position);
        entity.videoName.setText(video.getName());
        entity.videoImg.setImageResource(R.drawable.video_icon);
        mImgLoader.loadImg(video.getPath(),
                Toolkit.px2dip(mContext, THUMB_WIDTH),
                Toolkit.px2dip(mContext, THUMB_HEIGHT), entity.videoImg,
                new ImgCallback() {

            @Override
            public void refresh(Object view, Bitmap bitmap) {
                ImageView imageView = (ImageView) view;
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(R.drawable.video_icon);
                }
            }
        });
        return convertView;
    }
}
