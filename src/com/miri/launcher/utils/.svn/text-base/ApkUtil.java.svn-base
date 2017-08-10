/**
 * 
 */
package com.miri.launcher.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.miri.launcher.R;
import com.miri.launcher.imageCache.utils.DiskLruCache;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.CustomToast.MsgType;

/**
 * 预装APK管理工具类
 * 
 * @author zengjiantao
 * 
 */
public class ApkUtil {

    public static final String APK_SUBDIR = "apk";

    public static String getDiskCacheDir(Context context, String uniqueName) {
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
        return file.getAbsolutePath();
    }

    public static boolean installExist(Context context, String apkName) {
        String fileName = getDiskCacheDir(context, APK_SUBDIR) + File.separator
                + apkName;
        File file = new File(fileName);
        if (file.exists()) {
            Log.d("Debug", "Install with exist apk!");
            install(context, fileName);
            return true;
        }
        return false;
    }

    private static void install(Context context, final String filePath) {
        // 检查系统是否允许安装第三方应用
        if (isInstallingUnknownAppsAllowed(context)) {
            installApk(context, filePath);
        } else {
            CustomToast.makeText(context, R.string.no_allow_install,
                    Toast.LENGTH_SHORT, MsgType.ERROR).show();
            // Toast.makeText(getApplicationContext(),
            // R.string.no_allow_install, Toast.LENGTH_SHORT)
            // .show();
        }
    }

    /**
     * 是否允许安装第三方应用
     * 
     * @return
     */
    private static boolean isInstallingUnknownAppsAllowed(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0) > 0;
    }

    /**
     * 安装Apk
     * 
     * @param context
     * @param file
     */
    private static void installApk(Context context, String filePath) {
        Intent installer = new Intent(Intent.ACTION_VIEW);
        installer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installer.setDataAndType(Uri.parse("file://" + filePath),
                "application/vnd.android.package-archive");// Uri.fromFile(new
        // File(filePath))
        try {
            context.startActivity(installer);
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getApplicationContext(), R.string.install_fail,
            // Toast.LENGTH_SHORT)
            // .show();
            CustomToast.makeText(context, R.string.install_fail,
                    Toast.LENGTH_SHORT, MsgType.ERROR).show();
        }
    }
}
