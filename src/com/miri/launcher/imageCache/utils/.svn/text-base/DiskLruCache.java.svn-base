package com.miri.launcher.imageCache.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class DiskLruCache {

    private static final String CACHE_FILENAME_PREFIX = "cache_";

    private final File mCacheDir;

    private static final int MB = 1024 * 1024;

    private long maxCacheByteSize = 100 * MB; // 100MB default

    private static final long FREE_NEEDED_TO_CACHE = 10 * MB; //缓存目录的剩余空间

    /**
     * Used to fetch an instance of DiskLruCache.
     * @param context
     * @param cacheDir
     * @param maxByteSize
     * @return
     */
    public static DiskLruCache openCache(Context context, File cacheDir, long maxByteSize) {
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        if (cacheDir.isDirectory() && cacheDir.canWrite()) {
            return new DiskLruCache(cacheDir, maxByteSize);
        }

        return null;
    }

    /**
     * Constructor that should not be called directly, instead use
     * {@link DiskLruCache#openCache(Context, File, long)} which runs some extra checks before
     * creating a DiskLruCache instance.
     * @param cacheDir
     * @param maxByteSize
     */
    private DiskLruCache(File cacheDir, long maxByteSize) {
        mCacheDir = cacheDir;
        maxCacheByteSize = maxByteSize;
        if (cacheDir != null) {
            clearCache();
        }
    }

    /** 将图片存入文件缓存 **/
    public void put(String key, Bitmap data) {
        if (data == null) {
            return;
        }
        //判断缓存目录空间是否足够
        if (FREE_NEEDED_TO_CACHE > getCacheAvailableMem()) {
            //缓存目录空间不足
            return;
        }
        String filename = createFilePath(key);
        File file = new File(filename);
        OutputStream out = null;
        try {
            file.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(file), 4 * 1024);
            data.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            Log.e("ImageFileCache", "FileNotFoundException:" + e.getMessage());
        } catch (IOException e) {
            Log.e("ImageFileCache", "IOException:" + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get an image from the disk cache.
     * @param key The unique identifier for the bitmap
     * @return The bitmap or null if not found
     */
    public Bitmap get(String key) {
        final String path = createFilePath(key);
        File file = new File(path);
        if (file.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }

    /**
     * Removes all disk cache entries from this instance cache dir
     */
    public void clearAllCache() {
        if (mCacheDir != null) {
            final File[] files = mCacheDir.listFiles(cacheFileFilter);
            if (files != null && files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    files[i].delete();
                }
            }
        }
    }

    /**
     * 计算缓存存储目录下的文件大小， 当文件总大小大于规定的maxCacheByteSize<br>
     * 或者缓存存储目录下剩余空间小于FREE_NEEDED_TO_CACHE的规定 那么删除40%最近没有被使用的文件
     */
    public void clearCache() {
        if (mCacheDir == null) {
            return;
        }
        File[] files = mCacheDir.listFiles(cacheFileFilter);
        if (files == null || files.length == 0) {
            return;
        }
        //计算缓存存储目录下的文件大小
        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            dirSize += files[i].length();
        }

        if (dirSize > maxCacheByteSize || FREE_NEEDED_TO_CACHE > getCacheAvailableMem()) {
            Arrays.sort(files, new FileLastModifSort());
            int removeFactor = (int) ((0.4 * files.length) + 1);
            for (int i = 0; i < removeFactor; i++) {
                files[i].delete();
            }
        }
    }

    /**
     * 获取缓存目录可用存储大小
     * @param path
     * @return
     */
    private long getCacheAvailableMem() {
        StatFs statFs = new StatFs(mCacheDir.getAbsolutePath());
        long blockSize = statFs.getBlockSize();
        return blockSize * statFs.getAvailableBlocks();
    }

    /**
     * 获得缓存目录
     * @param uniqueName 缓存子目录名
     * @return The cache dir
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String dir = "";
        String sdPath = getSDPath();
        //不存在sd，使用内置存储
        if (sdPath == null || sdPath.equals("")) {
            final String cachePath = context.getCacheDir().getPath();
            dir = cachePath + File.separator + uniqueName;
        } else {
            dir = getSDPath() + File.separator + uniqueName;
        }
        return new File(dir);
    }

    /**
     * 取SD卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory(); //获取根目录
        }
        if (sdDir != null) {
            return sdDir.toString();
        } else {
            return "";
        }
    }

    /**
     * Creates a constant cache file path given a target cache directory and an image key.
     * @param cacheDir
     * @param key
     * @return
     */
    public String createFilePath(String key) {
        try {
            // Use URLEncoder to ensure we have a valid filename, a tad hacky but it will do for
            // this example
            return mCacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX
                    + URLEncoder.encode(key.replace("*", ""), "UTF-8");
        } catch (final UnsupportedEncodingException e) {
        }

        return null;
    }

    /**
     * A filename filter to use to identify the cache filenames which have CACHE_FILENAME_PREFIX
     * prepended.
     */
    private static final FilenameFilter cacheFileFilter = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String filename) {
            return filename.startsWith(CACHE_FILENAME_PREFIX);
        }
    };

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {

        @Override
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }

    /** 修改文件的最后修改时间 **/
    private void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }
}
