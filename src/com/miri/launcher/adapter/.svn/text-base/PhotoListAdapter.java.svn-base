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
import com.miri.launcher.model.Photo;
import com.miri.launcher.utils.LocalImgLoader;
import com.miri.launcher.utils.LocalImgLoader.ImgCallback;
import com.miri.launcher.utils.Toolkit;

public class PhotoListAdapter extends ArrayAdapter<Photo> {

    private static final int THUMB_WIDTH = 171;

    private static final int THUMB_HEIGHT = 111;

    private final Context mContext;

    private LayoutInflater mInflater = null;

    private final int mItemTheme;

    private final LocalImgLoader mImgLoader;

    public PhotoListAdapter(Context context, int itemTheme, List<Photo> photos) {
        super(context, itemTheme, photos);
        mContext = context;
        mItemTheme = itemTheme;
        mInflater = LayoutInflater.from(context);
        mImgLoader = new LocalImgLoader(context);
    }

    public class Entity {

        public ImageView photoImg;

        public TextView photoName;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Entity entity;
        if (convertView == null) {
            entity = new Entity();
            convertView = mInflater.inflate(mItemTheme, null);
            entity.photoImg = (ImageView) convertView
                    .findViewById(R.id.photo_img);
            entity.photoName = (TextView) convertView
                    .findViewById(R.id.photo_name);
            convertView.setTag(entity);
        } else {
            entity = (Entity) convertView.getTag();
        }

        Photo photo = getItem(position);
        entity.photoName.setText(photo.getName());
        entity.photoImg.setImageResource(R.drawable.video_icon);
        mImgLoader.loadImg(photo.getPath(),
                Toolkit.px2dip(mContext, THUMB_WIDTH),
                Toolkit.px2dip(mContext, THUMB_HEIGHT), entity.photoImg,
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
