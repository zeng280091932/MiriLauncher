/* 
 * 文件名：CustomBtnView.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-9-13
 */
public class CustomBtnView extends LinearLayout {

    private ImageView mIconView;

    private TextView mTitle;

    public CustomBtnView(Context context) {
        super(context);
    }

    public CustomBtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.custombtnview_attr);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_btn, this, true);
        mIconView = (ImageView) view.findViewById(R.id.btn_icon);
        mTitle = (TextView) view.findViewById(R.id.btn_title);

        int resId;
        //背景
        resId = typedArray.getResourceId(R.styleable.custombtnview_attr_btn_background, 0);
        if (resId > 0) {
            setBackgroundResource(resId);
        } else {
            resId = typedArray.getColor(R.styleable.custombtnview_attr_btn_background, 0);
            if (resId > 0) {
                setBackgroundColor(resId);
            } else {
                setBackgroundResource(R.drawable.btn_backgrund_green);
            }
        }
        //图标
        resId = typedArray.getResourceId(R.styleable.custombtnview_attr_btn_icon, 0);
        if (resId > 0) {
            mIconView.setImageResource(resId);
            mIconView.setVisibility(View.VISIBLE);
        } else {
            mIconView.setImageDrawable(null);
            mIconView.setVisibility(View.GONE);
        }
        //标题
        resId = typedArray.getResourceId(R.styleable.custombtnview_attr_btn_title, 0);
        if (resId > 0) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(resId);
        } else {
            CharSequence text = typedArray.getText(R.styleable.custombtnview_attr_btn_title);
            if (!TextUtils.isEmpty(text)) {
                mTitle.setVisibility(View.VISIBLE);
                mTitle.setText(text);
            } else {
                mTitle.setVisibility(View.GONE);
            }
        }
        typedArray.recycle();
    }

    /**
     * 设置文件内容
     */
    public void setText(int resid) {
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

    public void setText(CharSequence text) {
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

    /**
     * 设置图标
     * @param resid
     */
    public void setIconDrawable(int resid) {
        if (mIconView != null) {
            mIconView.setImageResource(resid);
            mIconView.setVisibility(View.VISIBLE);
            invalidate();
        }
    }

}
