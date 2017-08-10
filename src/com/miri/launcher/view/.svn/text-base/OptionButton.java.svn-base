/* 
 * 文件名：OptionButton.java
 * 版权：Copyright
 */
package com.miri.launcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miri.launcher.R;

/**
 * 操作按钮
 * @author zengjiantao
 * @version TVLAUNCHER001, 2013-9-13
 */
public class OptionButton extends LinearLayout {

    private ImageButton mImageBtn;

    private TextView mText;

    public OptionButton(Context context) {
        super(context);
    }

    public OptionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.option_button_attr);
        View view = LayoutInflater.from(context).inflate(R.layout.option_button, this, true);
        mImageBtn = (ImageButton) view.findViewById(R.id.button);
        mText = (TextView) view.findViewById(R.id.text);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setClickable(true);

        int resId;
        //图标
        resId = typedArray.getResourceId(R.styleable.option_button_attr_btn_src, 0);
        if (resId > 0) {
            mImageBtn.setImageResource(resId);
            mImageBtn.setVisibility(View.VISIBLE);
        } else {
            mImageBtn.setImageDrawable(null);
            mImageBtn.setVisibility(View.GONE);
        }
        //标题
        resId = typedArray.getResourceId(R.styleable.option_button_attr_btn_text, 0);
        if (resId > 0) {
            mText.setVisibility(View.VISIBLE);
            mText.setText(resId);
        } else {
            CharSequence text = typedArray.getText(R.styleable.option_button_attr_btn_text);
            if (!TextUtils.isEmpty(text)) {
                mText.setVisibility(View.VISIBLE);
                mText.setText(text);
            } else {
                mText.setVisibility(View.GONE);
            }
        }
        typedArray.recycle();
    }

    /**
     * 设置文件内容
     */
    public void setText(int resid) {
        if (mText != null) {
            if (resid > 0) {
                mText.setVisibility(View.VISIBLE);
                mText.setText(resid);
            } else {
                mText.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    public void setText(CharSequence text) {
        if (mText != null) {
            if (!TextUtils.isEmpty(text)) {
                mText.setVisibility(View.VISIBLE);
                mText.setText(text);
            } else {
                mText.setVisibility(View.GONE);
            }
            invalidate();
        }
    }

    /**
     * 设置图标
     * @param resid
     */
    public void setImageResource(int resid) {
        if (mImageBtn != null) {
            mImageBtn.setImageResource(resid);
            mImageBtn.setVisibility(View.VISIBLE);
            invalidate();
        }
    }
    
    @Override
    protected void onFocusChanged(boolean hasFocus, int direction,
    		Rect previouslyFocusedRect) {
    	if (hasFocus) {
			mImageBtn.setSelected(true);
			mText.setShadowLayer(10.0f, 0.0f, 0.0f, getResources().getColor(R.color.option_btn_showdow));
			mText.setTextColor(getResources().getColor(R.color.option_btn_selected));
		}else {
			mImageBtn.setSelected(false);
			mText.setShadowLayer(0.0f, 0.0f, 0.0f, Color.TRANSPARENT);
			mText.setTextColor(Color.WHITE);
		}
    	super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);
    }

}
