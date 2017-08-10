/*
 * 
 */
package com.miri.launcher.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;

/**
 * 截屏工具类
 * 
 * @author zengjiantao
 * @date 2013-5-31
 */
public class ScreenCapture {

	/**
	 * 截取全屏截图
	 * 
	 * @param activity
	 * @return
	 */
	public static Bitmap captureFullScreen(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();

		Bitmap screenBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		view.destroyDrawingCache();
		return screenBitmap;
	}

	/**
	 * 截取指定控件截图
	 * 
	 * @param view
	 * @return
	 */
	public static Bitmap captureWidget(View view) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		int width = view.getMeasuredWidth();
		int height = view.getMeasuredHeight();

		Bitmap screenBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		view.destroyDrawingCache();
		return screenBitmap;
	}

	/**
	 * 截取自定区域的屏幕内容
	 * 
	 * @param activity
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap captureSpecifiedScreen(Activity activity, int x,
			int y, int width, int height) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		Bitmap screenBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
		view.destroyDrawingCache();
		return screenBitmap;
	}

	/**
	 * 生成缩略图
	 * 
	 * @param source
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap extractThumbnail(Bitmap source, int width, int height) {
		return ThumbnailUtils.extractThumbnail(source, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath
	 *            视频的路径
	 * @param width
	 *            指定输出视频缩略图的宽度
	 * @param height
	 *            指定输出视频缩略图的高度度
	 * @param kind
	 *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap extractVideoThumbnail(String videoPath, int width,
			int height) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,
				MediaStore.Images.Thumbnails.MINI_KIND);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
