/**
 * 
 */
package com.miri.launcher.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

/**
 * Assets文件拷贝工具类
 * 
 * @author zengjiantao
 * 
 */
public class AssetsCopyer extends Thread {

    private final Context mContext;

    public AssetsCopyer(Context context) {
        mContext = context;
    }

    @Override
    public void run() {
        long time1 = System.currentTimeMillis();
        copyDir(ApkUtil.APK_SUBDIR);
        long time2 = System.currentTimeMillis();
        Log.d("Debug", "time:" + (time2 - time1));
        super.run();
    }

    /**
     * 拷贝assert下面某个文件夹里面的内容到指定文件夹
     */
    private void copyDir(String dirName) {
        try {
            String[] filenames = mContext.getAssets().list(dirName);
            for (String filename : filenames) {
                String srcName = dirName + File.separator + filename;
                copyFile(srcName,
                        ApkUtil.getDiskCacheDir(mContext, ApkUtil.APK_SUBDIR));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝assert下面某个文件到指定文件夹
     */
    private boolean copyFile(String srcName, String destPath) {
        InputStream is = null;
        OutputStream os = null;

        File dir = new File(destPath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        String filename = srcName;
        if (filename.contains(File.separator)) {
            filename = srcName.substring(srcName.lastIndexOf(File.separator),
                    srcName.length());
        }
        if (filename.contains(".mp3")) {
            filename = filename.replaceAll(".mp3", ".apk");
        }

        final String filePath = destPath + File.separator + filename;
        try {
            is = mContext.getAssets().open(srcName);
            os = new FileOutputStream(filePath);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(os);
            close(is);
        }
        return true;
    }

    /**
     * 关闭流
     * 
     * @param stream
     * @throws QueryServerInfoException
     */
    private static void close(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
                stream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
