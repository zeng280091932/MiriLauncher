/* 
 * 文件名：CustomVideoView.java
 * 版权：Copyright 2009-2012 KOOLSEE MediaNet. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.VideoView;

/**
 * 自定义播放器
 * 
 * @author zengjiantao
 */
public class CustomVideoView extends VideoView {

	private Context mContext;

	public CustomVideoView(Context context) {
		super(context);
		mContext = context;
	}

	public CustomVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Display display = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		Log.d("Debug", "width==" + width + "height===" + height);
		setMeasuredDimension(width, height);
	}
}
