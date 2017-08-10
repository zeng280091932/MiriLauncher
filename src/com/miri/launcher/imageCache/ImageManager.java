package com.miri.launcher.imageCache;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.miri.launcher.R;
import com.miri.launcher.imageCache.utils.DiskLruCache;
import com.miri.launcher.imageCache.utils.ImageGetFromHttp;
import com.miri.launcher.imageCache.utils.ImageMemoryCache;

/**
 * 图片加载类
 */
@Deprecated
public class ImageManager {

    private static final String LOG_TAG = "ImageManager";

    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 100; // 100MB

    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    private static ImageManager imageManager;

    private Context mContext;

    public ImageMemoryCache mMemoryCache;

    public DiskLruCache mDiskCache;

    /** 图片加载队列(栈)，后进先出 */
    private Stack<ImageRef> mImageQueue = new Stack<ImageRef>();

    /** 图片请求队列，先进先出，用于存放已发送的请求。 */
    private Queue<ImageRef> mRequestQueue = new LinkedList<ImageRef>();

    /** 图片加载线程消息处理器 */
    private Handler mImageLoaderHandler;

    /** 图片加载线程是否就绪 */
    private boolean mImageLoaderIdle = true;

    /** 请求图片 */
    private static final int MSG_REQUEST = 1;

    /** 图片加载完成 */
    private static final int MSG_REPLY = 2;

    /** 中止图片加载线程 */
    private static final int MSG_STOP = 3;

    /** 如果图片是从网络加载，则应用渐显动画，如果从缓存读出则不应用动画 */
    private boolean isFromNet = true;

    /**
     * 私有构造函数，保证单例模式
     * @param context
     */
    private ImageManager(Context context) {
        mContext = context;
        mMemoryCache = new ImageMemoryCache(context);

        //        File cacheDir = DiskLruCache.getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        File cacheDir = getDiskCacheDir(context, DISK_CACHE_SUBDIR);
        mDiskCache = DiskLruCache.openCache(context, cacheDir, DISK_CACHE_SIZE);
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        File file = null;
        String sdPath = DiskLruCache.getSDPath();
        //不存在sd，使用内置存储
        if (sdPath == null || sdPath.equals("")) {
            final String cachePath = context.getCacheDir().getPath();
            file = new File(cachePath + File.separator + uniqueName);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
        } else {
            //在sd目录下创建包名文件目录
            String pkgNamePath = sdPath + File.separator + context.getPackageName();
            File pkgNameFile = new File(pkgNamePath);
            if (!pkgNameFile.exists() || !pkgNameFile.isDirectory()) {
                pkgNameFile.mkdirs();
            }
            //在包名文件目录下创建uniqueName文件目录
            file = new File(pkgNamePath + File.separator + uniqueName);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
            }
        }
        return file;
    }

    /**
     * 获取单例，只能在UI线程中使用。
     * @param context
     * @return
     */
    public static ImageManager from(Context context) {

        // 如果不在ui线程中，则抛出异常
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Cannot instantiate outside UI thread.");
        }

        if (imageManager == null) {
            imageManager = new ImageManager(context);
        }
        return imageManager;
    }

    /**
     * 存放图片信息
     */
    class ImageRef {

        /** 图片对应ImageView控件 */
        ImageView imageView;

        /** 图片URL地址 */
        String url;

        /** 默认图资源ID */
        int resId;

        int width = 0;

        int height = 0;

        /**
         * 构造函数
         * @param imageView
         * @param url
         * @param resId
         * @param filePath
         */
        ImageRef(ImageView imageView, String url, int resId) {
            this.imageView = imageView;
            this.url = url;
            this.resId = resId;
        }

        ImageRef(ImageView imageView, String url, int resId, int width, int height) {
            this.imageView = imageView;
            this.url = url;
            this.resId = resId;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageRef [imageView=" + imageView + ", url=" + url + ", resId=" + resId
                    + ", width=" + width + ", height=" + height + "]";
        }

    }

    /**
     * 显示图片
     * @param imageView
     * @param url
     * @param resId
     */
    public void displayImage(ImageView imageView, String url, int resId) {
        if (imageView == null) {
            return;
        }
        if (imageView.getTag(R.string.imageview_cache) != null
                && imageView.getTag(R.string.imageview_cache).toString().equals(url)) {
            return;
        }
        if (resId >= 0) {
            if (imageView.getBackground() == null) {
                imageView.setBackgroundResource(resId);
            }
            imageView.setImageDrawable(null);

        }
        if (url == null || url.equals("")) {
            return;
        }

        // 添加url tag
        imageView.setTag(R.string.imageview_cache, url);

        // 先从内存缓存中读取图片
        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, false);
            return;
        }

        // 从文件缓存中读取图片
        bitmap = mDiskCache.get(url);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, false);
            mMemoryCache.put(url, bitmap);
            return;
        }

        //TODO 优化使用多个线程同时下载，优化排队机制
        queueImage(new ImageRef(imageView, url, resId));
    }

    /**
     * 显示图片固定大小图片的缩略图，一般用于显示列表的图片，可以大大减小内存使用
     * @param imageView 加载图片的控件
     * @param url 加载地址
     * @param resId 默认图片
     * @param width 指定宽度
     * @param height 指定高度
     */
    public void displayImage(ImageView imageView, String url, int resId, int width, int height) {
        if (imageView == null) {
            return;
        }
        if (resId >= 0) {

            if (imageView.getBackground() == null) {
                imageView.setBackgroundResource(resId);
            }
            imageView.setImageDrawable(null);

        }
        if (url == null || url.equals("")) {
            return;
        }

        // 添加url tag
        imageView.setTag(R.string.imageview_cache, url);
        // 先从内存缓存中读取图片
        Bitmap bitmap = mMemoryCache.get(url + width + height);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, false);
            return;
        }

        // 从文件缓存中读取图片
        bitmap = mDiskCache.get(url + width + height);
        if (bitmap != null) {
            setImageBitmap(imageView, bitmap, false);
            mMemoryCache.put(url + width + height, bitmap);
            return;
        }

        queueImage(new ImageRef(imageView, url, resId, width, height));
    }

    /**
     * 入队，后进先出
     * @param imageRef
     */
    private void queueImage(ImageRef imageRef) {

        // 删除已有ImageView
        Iterator<ImageRef> iterator = mImageQueue.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().imageView == imageRef.imageView) {
                iterator.remove();
            }
        }

        // 添加请求
        mImageQueue.push(imageRef);
        sendRequest();
    }

    /**
     * 发送请求
     */
    private void sendRequest() {

        // 开启图片加载线程
        if (mImageLoaderHandler == null) {
            HandlerThread imageLoader = new HandlerThread("image_loader");
            imageLoader.start();
            mImageLoaderHandler = new ImageLoaderHandler(imageLoader.getLooper());
        }

        // 发送请求
        if (mImageLoaderIdle && mImageQueue.size() > 0) {
            ImageRef imageRef = mImageQueue.pop();
            Message message = mImageLoaderHandler.obtainMessage(MSG_REQUEST, imageRef);
            mImageLoaderHandler.sendMessage(message);
            mImageLoaderIdle = false;
            mRequestQueue.add(imageRef);
        }
    }

    /**
     * 图片加载线程
     */
    class ImageLoaderHandler extends Handler {

        public ImageLoaderHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }

            switch (msg.what) {

                case MSG_REQUEST: // 收到请求
                    Bitmap bitmap = null;
                    if (msg.obj != null && msg.obj instanceof ImageRef) {

                        ImageRef imageRef = (ImageRef) msg.obj;
                        String url = imageRef.url;
                        if (url == null) {
                            return;
                        }

                        //从文件缓存中获取
                        bitmap = mDiskCache.get(url);

                        if (bitmap != null) {
                            String key = "";
                            if (imageRef.width != 0 && imageRef.height != 0) {
                                key = url + imageRef.width + imageRef.height;
                            } else {
                                key = url;
                            }

                            if (mMemoryCache.get(key) == null) {
                                mMemoryCache.put(key, bitmap);
                            }

                        } else {
                            //从网络上读取
                            Bitmap tBitmap = null;
                            try {
                                tBitmap = ImageGetFromHttp.downloadBitmap(url);

                                if (tBitmap != null) {

                                    if (imageRef.width != 0 && imageRef.height != 0) {
                                        bitmap = ThumbnailUtils.extractThumbnail(tBitmap,
                                                imageRef.width, imageRef.height,
                                                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                                    } else {
                                        bitmap = tBitmap;
                                        tBitmap = null;
                                    }

                                    if (bitmap != null && url != null) {
                                        // 写入文件缓存和内存缓存
                                        String key = "";
                                        if (imageRef.width != 0 && imageRef.height != 0) {
                                            key = url + imageRef.width + imageRef.height;
                                        } else {
                                            key = url;
                                        }
                                        mDiskCache.put(key, bitmap);
                                        mMemoryCache.put(key, bitmap);
                                        isFromNet = true;
                                    }
                                }
                            } catch (OutOfMemoryError e) {
                                Log.e(LOG_TAG, e.getMessage());
                            }

                        }

                    }

                    if (mImageManagerHandler != null) {
                        Message message = mImageManagerHandler.obtainMessage(MSG_REPLY, bitmap);
                        mImageManagerHandler.sendMessage(message);
                    }
                    break;

                case MSG_STOP: // 收到终止指令
                    Looper.myLooper().quit();
                    break;

            }
        }
    }

    /** UI线程消息处理器 */
    private Handler mImageManagerHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                switch (msg.what) {

                    case MSG_REPLY: // 收到应答

                        do {
                            ImageRef imageRef = mRequestQueue.remove();

                            if (imageRef == null) {
                                break;
                            }

                            if (imageRef.imageView == null
                                    || imageRef.imageView.getTag(R.string.imageview_cache) == null
                                    || imageRef.url == null) {
                                break;
                            }

                            if (!(msg.obj instanceof Bitmap) || msg.obj == null) {
                                break;
                            }
                            Bitmap bitmap = (Bitmap) msg.obj;

                            // 非同一ImageView
                            if (!(imageRef.url).equals(imageRef.imageView
                                    .getTag(R.string.imageview_cache))) {
                                break;
                            }

                            setImageBitmap(imageRef.imageView, bitmap, isFromNet);
                            isFromNet = false;

                        } while (false);

                        break;
                }
            }
            // 设置闲置标志
            mImageLoaderIdle = true;

            // 若服务未关闭，则发送下一个请求。
            if (mImageLoaderHandler != null) {
                sendRequest();
            }
        }
    };

    /**
     * 添加图片显示渐现动画
     * @param isTran 是否有图片渐显效果
     */
    private void setImageBitmap(ImageView imageView, Bitmap bitmap, boolean isTran) {
        if (isTran) {
            final TransitionDrawable td = new TransitionDrawable(new Drawable[] {
                    new ColorDrawable(android.R.color.transparent), new BitmapDrawable(bitmap)});
            td.setCrossFadeEnabled(true);
            imageView.setImageDrawable(td);
            td.startTransition(300);
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Activity#onStop后，ListView不会有残余请求。
     */
    public void stop() {
        // 清空请求队列
        mImageQueue.clear();

    }

}
