/*
 * 文件名：IconView.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.imageCache.ImageLoader.ImgCallback;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-6
 */
public class PosterView extends FrameLayout {

    private ImageView mPoster;

    private TextView mTitle;

    private TextView mDesc;

    private LinearLayout mInfoLayout;

    private boolean alawsShowInfo = false;

    public PosterView(Context context) {
        super(context);
    }

    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.poster_view,
                this, true);
        mPoster = (ImageView) view.findViewById(R.id.poster);
        mTitle = (TextView) view.findViewById(R.id.title);
        mDesc = (TextView) view.findViewById(R.id.desc);
        mInfoLayout = (LinearLayout) view.findViewById(R.id.info_layout);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    /**
     * 设置海报
     * 
     * @param resid
     */
    public void setPosterDrawable(int resid) {
        if (mPoster != null) {
            mPoster.setImageResource(resid);
            mPoster.setVisibility(View.VISIBLE);
            mPoster.setScaleType(ScaleType.FIT_XY);
            setBackgroundResource(0);
            invalidate();
        }
    }

    public void setPosterBitmap(Bitmap bm) {
        if (mPoster != null) {
            mPoster.setImageBitmap(bm);
            mPoster.setVisibility(View.VISIBLE);
            mPoster.setScaleType(ScaleType.FIT_XY);
            setBackgroundResource(0);
            invalidate();
        }
    }

    public void setPosterDrawable(Drawable drawable){
        if (mPoster != null) {
            mPoster.setImageDrawable(drawable);
            mPoster.setVisibility(View.VISIBLE);
            mPoster.setScaleType(ScaleType.FIT_XY);
            setBackgroundResource(0);
            invalidate();
        }
    }

    public void setPosterUrl(String url, int defaultImageId) {
        if (mPoster != null) {
            ImageLoader.from(getContext()).loadImg(mPoster, url,
                    defaultImageId, new ImgCallback() {

                @Override
                public void refresh(Object view, Bitmap bitmap,
                        boolean isFromNet) {
                    ((ImageView) view).setImageBitmap(bitmap);
                    ((ImageView) view).setVisibility(View.VISIBLE);
                    ((ImageView) view).setScaleType(ScaleType.FIT_XY);
                    setBackgroundResource(0);
                    invalidate();
                }
            });
        }
    }

    /**
     * 设置文件内容
     */
    public void setTitleText(int resid) {
        if (mTitle != null) {
            if (resid > 0) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(resid);
            } else {
                mTitle.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    public void setTitleText(CharSequence text) {
        if (mTitle != null) {
            if (!TextUtils.isEmpty(text)) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(text);
            } else {
                mTitle.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    public void setDescText(CharSequence text) {
        if (mDesc != null) {
            if (!TextUtils.isEmpty(text)) {
                mDesc.setVisibility(View.VISIBLE);
                mDesc.setText(text);
            } else {
                mDesc.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    public void setAlawsShowInfo(boolean alawsShowInfo) {
        this.alawsShowInfo = alawsShowInfo;
        mInfoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onFocusChanged(boolean hasFocus, int direction,
            Rect previouslyFocusedRect) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);
        if (hasFocus) {
            bringToFront();
            Drawable drawable = getBackground();
            if (drawable == null) {
                setBackgroundResource(R.drawable.select_bg);
            }
            if (!alawsShowInfo) {
                mInfoLayout.setVisibility(View.VISIBLE);
            }
        } else {
            setBackgroundDrawable(null);
            if (!alawsShowInfo) {
                mInfoLayout.setVisibility(View.GONE);
            }
        }
        invalidate();
    }

}
