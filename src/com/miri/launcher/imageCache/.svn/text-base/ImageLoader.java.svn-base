/* 
 * 文件名：ImageLoader.java
 * 版权：Copyright
 */
package com.miri.launcher.imageCache;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.miri.launcher.imageCache.utils.DiskLruCache;
import com.miri.launcher.imageCache.utils.ImageGetFromHttp;
import com.miri.launcher.imageCache.utils.ImageMemoryCache;
import com.miri.launcher.utils.Logger;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-8-5
 */
public class ImageLoader {

	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100; // 100MB

	private static final String DISK_CACHE_SUBDIR = "thumbnails";

	public ImageMemoryCache mMemoryCache;

	public DiskLruCache mDiskCache;

	private final Context ctx;

	private static ImageLoader mImageLoader;

	private final Handler handler;

	private final ExecutorService threadPool;

	private ImageLoader(Context ctx) {
		this.ctx = ctx;
		handler = new Handler();
		mMemoryCache = new ImageMemoryCache(ctx);

		// File cacheDir = DiskLruCache.getDiskCacheDir(ctx, DISK_CACHE_SUBDIR);
		File cacheDir = getDiskCacheDir(ctx, DISK_CACHE_SUBDIR);
		mDiskCache = DiskLruCache.openCache(ctx, cacheDir, DISK_CACHE_SIZE);
		threadPool = Executors.newFixedThreadPool(5);
	}

	/**
	 * 获取单例，只能在UI线程中使用。
	 * 
	 * @param context
	 * @return
	 */
	public static ImageLoader from(Context context) {
		// 如果不在ui线程中，则抛出异常
		if (Looper.myLooper() != Looper.getMainLooper()) {
			throw new RuntimeException("Cannot instantiate outside UI thread.");
		}

		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(context);
		}
		return mImageLoader;
	}

	private File getDiskCacheDir(Context context, String uniqueName) {
		File file = null;
		String sdPath = DiskLruCache.getSDPath();
		// 不存在sd，使用内置存储
		if (sdPath == null || sdPath.equals("")) {
			final String cachePath = context.getCacheDir().getPath();
			file = new File(cachePath + File.separator + uniqueName);
			if (!file.exists() || !file.isDirectory()) {
				file.mkdirs();
			}
		} else {
			// 在sd目录下创建包名文件目录
			String pkgNamePath = sdPath + File.separator
					+ context.getPackageName();
			File pkgNameFile = new File(pkgNamePath);
			if (!pkgNameFile.exists() || !pkgNameFile.isDirectory()) {
				pkgNameFile.mkdirs();
			}
			// 在包名文件目录下创建uniqueName文件目录
			file = new File(pkgNamePath + File.separator + uniqueName);
			if (!file.exists() || !file.isDirectory()) {
				file.mkdirs();
			}
		}
		return file;
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 *            图片地址
	 * @param view
	 *            要显示图片的控件
	 * @param resId
	 *            默认资源图片
	 * @param callback
	 *            回调函数
	 */
	public void loadImg(final Object view, final String url, final int resId,
			final ImgCallback callback) {
		loadImg(view, url, resId, 0, 0, callback);
	}

	/**
	 * 获取网络图片,显示图片固定大小图片的缩略图
	 * 
	 * @param url
	 *            图片地址
	 * @param view
	 *            要显示图片的控件
	 * @param resId
	 *            默认资源图片
	 * @param width
	 *            指定宽度
	 * @param height
	 *            指定高度
	 * @param callback
	 *            回调函数
	 */
	public void loadImg(final Object view, final String url, final int resId,
			final int width, final int height, final ImgCallback callback) {

		if (view == null) {
			return;
		}

		if (resId > 0 && view instanceof View) {
			if (((View) view).getBackground() == null) {
				((View) view).setBackgroundResource(resId);
			}
			if (view instanceof ImageView) {
				((ImageView) view).setImageResource(resId);
			}
		}

		if (url == null || url.equals("")) {
			return;
		}

		final String key;
		if (width != 0 && height != 0) {
			key = url + width + height;
		} else {
			key = url;
		}

		final Bitmap bitmap;
		try {

			// 先从缓存中读取图片资源
			bitmap = getImageFromCache(key);

			if (null == bitmap) {
				// 开启线程从网络上下载
				this.threadPool.submit(new Runnable() {// submit方法确保下载是从线程池中的线程执行

							@Override
							public void run() {
								final boolean isFromNet;
								// 从文件缓存中获取
								Bitmap bitmapRes = mDiskCache.get(key);
								if (bitmapRes == null) {
									isFromNet = true;
									// 从网络上获取
									Bitmap bitmapFromUrl = ImageGetFromHttp
											.downloadBitmap(url);
									if (width != 0 && height != 0) {
										bitmapRes = ThumbnailUtils
												.extractThumbnail(
														bitmapFromUrl,
														width,
														height,
														ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
									} else {
										bitmapRes = bitmapFromUrl;
									}
								} else {
									isFromNet = false;
								}

								if (bitmapRes != null) {
									if (mDiskCache.get(key) == null) {
										mDiskCache.put(key, bitmapRes);
									}
									if (mMemoryCache.get(key) == null) {
										mMemoryCache.put(key, bitmapRes);
									}
								}

								final Bitmap bitmap = bitmapRes;
								// 回调函数
								handler.post(new Runnable() {

									@Override
									public void run() {
										if (callback != null) {
											callback.refresh(view, bitmap,
													isFromNet);
										}
									}
								});
							}
						});
			} else {
				// 回调函数
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (callback != null) {
							callback.refresh(view, bitmap, false);
						}
					}
				});
			}
		} catch (Exception e) {
			Logger.getLogger().e("loadImgError,imgPath=" + url);
		}

	}

	/**
	 * 获取网络图片并保存至缓存中
	 * 
	 * @param url
	 */
	public void loadImg(final String url) {
		loadImg(url, 0, 0);
	}

	/**
	 * 获取网络图片并保存至缓存中
	 * 
	 * @param url
	 * @param width
	 * @param height
	 */
	public void loadImg(final String url, final int width, final int height) {

		if (url == null || url.equals("")) {
			return;
		}

		final String key;
		if (width != 0 && height != 0) {
			key = url + width + height;
		} else {
			key = url;
		}

		final Bitmap bitmap;
		try {

			// 先从缓存中读取图片资源
			bitmap = getImageFromCache(key);

			if (null == bitmap) {
				// 开启线程从网络上下载
				this.threadPool.submit(new Runnable() {// submit方法确保下载是从线程池中的线程执行

							@Override
							public void run() {
								// 从文件缓存中获取
								Bitmap bitmapRes = null;
								// 从网络上获取
								Bitmap bitmapFromUrl = ImageGetFromHttp
										.downloadBitmap(url);
								if (width != 0 && height != 0) {
									bitmapRes = ThumbnailUtils
											.extractThumbnail(
													bitmapFromUrl,
													width,
													height,
													ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
								} else {
									bitmapRes = bitmapFromUrl;
								}

								if (bitmapRes != null) {
									if (mDiskCache.get(key) == null) {
										mDiskCache.put(key, bitmapRes);
									}
									if (mMemoryCache.get(key) == null) {
										mMemoryCache.put(key, bitmapRes);
									}
								}
							}
						});
			}
		} catch (Exception e) {
			Logger.getLogger().e("loadImgError,imgPath=" + url);
		}

	}

	/**
	 * 从缓存中读取图片
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getImageFromCache(String url) {
		// 先从内存缓存中读取图片
		Bitmap bitmap = mMemoryCache.get(url);
		if (bitmap != null) {
			return bitmap;
		}

		// 从文件缓存中读取图片
		bitmap = mDiskCache.get(url);
		if (bitmap != null) {
			mMemoryCache.put(url, bitmap);
			return bitmap;
		}

		return null;
	}

	/**
	 * 添加图片显示渐现动画
	 * 
	 * @param isTran
	 *            是否有图片渐显效果
	 */
	public static void setImageBitmap(View view, Bitmap bitmap, boolean isTran) {
		if (bitmap == null) {
			return;
		}
		if (isTran) {
			final TransitionDrawable td = new TransitionDrawable(
					new Drawable[] {
							new ColorDrawable(android.R.color.transparent),
							new BitmapDrawable(bitmap) });
			td.setCrossFadeEnabled(true);
			if (view instanceof ImageView) {
				((ImageView) view).setImageDrawable(td);
			} else {
				view.setBackgroundDrawable(td);
			}
			td.startTransition(300);
		} else {
			if (view instanceof ImageView) {
				((ImageView) view).setImageBitmap(bitmap);
			} else {
				view.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}
	}

	/**
	 * 回调函数
	 * 
	 * @author penglin
	 * @version TVLAUNCHER001, 2013-8-5
	 */
	public interface ImgCallback {

		public void refresh(Object view, Bitmap bitmap, boolean isFromNet);
	}

}
