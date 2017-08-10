package com.miri.launcher.utils;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

/**
 * 本地图片图片加载器<br>
 * 支持线程池、消息队列、缓冲的方式获取网络图片<br>
 * 缓冲只支持线程级的
 * 
 * @author zengjiantao
 */
public class LocalImgLoader {

	private Map<String, SoftReference<Bitmap>> imgCache;

	private Handler handler;

	private ExecutorService threadPool;

	public LocalImgLoader(Context ctx) {
		handler = new Handler();
		imgCache = new HashMap<String, SoftReference<Bitmap>>();
		threadPool = Executors.newFixedThreadPool(5);
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 *            图片地址
	 * @param view
	 *            要显示图片的控件
	 * @param callback
	 *            回调函数
	 * @see [类、类#方法、类#成员]
	 * @since [产品/模块版本]
	 */
	public void loadImg(final String url, final int width, final int height,
			final Object view, final ImgCallback callback) {

		// 先从缓存中读取图片资源
		final Bitmap bitmap;
		try {
			bitmap = getImgFromCache(url, width, height);

			if (null == bitmap) {
				// 开启线程从网络上下载
				this.threadPool.submit(new Runnable() {// submit方法确保下载是从线程池中的线程执行

							@Override
							public void run() {
								final Bitmap bitmapFromUrl = getBitMapFromUrl(
										url, width, height);
								// 回调函数
								handler.post(new Runnable() {

									@Override
									public void run() {
										callback.refresh(view, bitmapFromUrl);
									}
								});
							}
						});
			} else {
				// 回调函数
				handler.post(new Runnable() {

					@Override
					public void run() {
						callback.refresh(view, bitmap);
					}
				});
			}
		} catch (Exception e) {
			Log.e("@@@", "loadImgError,imgPath=" + url);
		}

	}

	/**
	 * 回调接口，用来刷新界面
	 * 
	 * @author clw
	 * @version HDMNV100R001, 2011-10-23
	 * @see [相关类/方法]
	 * @since [产品/模块版本]
	 */

	public interface ImgCallback {

		public void refresh(Object view, Bitmap bitmap);

	}

	/**
	 * 从本地加载图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitMapFromUrl(String url, int reqWidth, int reqHeight) {
		File file = new File(url);
		if (!file.exists()) {
			return null;
		}
		Bitmap bitmap = ImageUtil.decodeSampledBitmapFromFile(file, reqWidth,
				reqHeight);
		// 将图片保存进内存中
//		synchronized (this.imgCache) {
//			if (!this.imgCache.containsKey(MD5Encrypt.encryptStr(url))) {
//				this.imgCache.put(MD5Encrypt.encryptStr(url),
//						new SoftReference<Bitmap>(bitmap));
//			}
//		}
		return bitmap;
	}

	/**
	 * 从缓存中读取
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private Bitmap getImgFromCache(String url, int reqWidht, int reqHeight)
			throws Exception {
		Bitmap bitmap = null;
		// 从内存中读取
		if (this.imgCache.containsKey(MD5Encrypt.encryptStr(url))) {
			synchronized (this.imgCache) {
				SoftReference<Bitmap> bitmapReference = this.imgCache
						.get(MD5Encrypt.encryptStr(url));
				if (null != bitmapReference) {
					bitmap = bitmapReference.get();
				}
			}
		}
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			if (width != reqWidht || height != reqHeight) {
				bitmap = ScreenCapture.extractThumbnail(bitmap, reqWidht,
						reqHeight);
			}
		}
		return bitmap;
	}

}
