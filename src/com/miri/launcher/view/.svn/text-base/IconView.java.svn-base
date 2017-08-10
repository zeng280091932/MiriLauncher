/* 
 * 文件名：IconView.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miri.launcher.R;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-6
 */
public class IconView extends FrameLayout {

    private RelativeLayout mContent;

    private ImageView mIconView;

    private TextView mTitle;

    public IconView(Context context) {
        super(context);
    }

    public IconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.iconview_attr);
        View view = LayoutInflater.from(context).inflate(R.layout.icon_view, this, true);
        mContent = (RelativeLayout) view.findViewById(R.id.content_bg);
        mIconView = (ImageView) view.findViewById(R.id.icon);
        mTitle = (TextView) view.findViewById(R.id.title);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);

        int resId;
        //背景
        resId = typedArray.getResourceId(R.styleable.iconview_attr_background, 0);
        if (resId > 0) {
            mContent.setBackgroundResource(resId);
        } else {
            resId = typedArray.getColor(R.styleable.iconview_attr_background, 0);
            if (resId > 0) {
                mContent.setBackgroundColor(resId);
            } else {
                mContent.setBackgroundResource(R.drawable.icon_default_bg);
            }
        }
        //图标
        resId = typedArray.getResourceId(R.styleable.iconview_attr_src, 0);
        if (resId > 0) {
            mIconView.setImageResource(resId);
        } else {
            mIconView.setImageDrawable(null);
        }
        //标题
        resId = typedArray.getResourceId(R.styleable.iconview_attr_text, 0);
        if (resId > 0) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(resId);
        } else {
            CharSequence text = typedArray.getText(R.styleable.iconview_attr_text);
            if (!TextUtils.isEmpty(text)) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(text);
            } else {
                mTitle.setVisibility(View.GONE);
            }
        }
        //标题剧中
        boolean centered = typedArray.getBoolean(R.styleable.iconview_attr_title_centered, false);
        if (centered) {
            mTitle.setGravity(Gravity.CENTER);
        }
        typedArray.recycle();
    }

    /**
     * 设置图标
     * @param resid
     */
    public void setIconDrawable(int resid) {
        if (mIconView != null) {
            mIconView.setImageResource(resid);
            mIconView.setVisibility(View.VISIBLE);
            mIconView.setScaleType(ScaleType.CENTER);
            invalidate();
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

    public void setTitleGravity(int gravity) {
        if (mTitle != null) {
            mTitle.setGravity(gravity);
            invalidate();
        }
    }

    @Override
    protected void onFocusChanged(boolean hasFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);
        if (hasFocus) {
            bringToFront();
            Drawable drawable = getBackground();
            if (drawable == null) {
                setBackgroundResource(R.drawable.select_bg);
            }
        } else {
            setBackgroundDrawable(null);
        }
        invalidate();
    }

}
