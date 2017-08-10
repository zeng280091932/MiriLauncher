/* 
 * 文件名：Toolkit.java
 * 版权：Copyright
 */
package com.miri.launcher.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-5-12
 */
public class Toolkit {

    /**
     * 隐藏状态栏和标题栏
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static void hideStatusBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setFlags(flag, flag);
    }

    /**
     * Uppercase first letter.
     * @param string
     * @return
     */
    public static String toUpperCaseFirstLetter(String string) {
        if (string == null) {
            return string;
        }
        StringBuilder builder = new StringBuilder(string.trim());
        if (builder.length() > 0) {
            builder.replace(0, 1, string.substring(0, 1).toUpperCase());
        }
        return builder.toString();
    }

    /**
     * Lowercase first letter.
     * @param string
     * @return
     */
    public static String toLowerCaseFirstLetter(String string) {
        if (string == null) {
            return string;
        }
        StringBuilder builder = new StringBuilder(string.trim());
        if (builder.length() > 0) {
            builder.replace(0, 1, string.substring(0, 1).toLowerCase());
        }
        return builder.toString();
    }

    public static int ceil(int a, int b) {
        return Math.round((float) Math.ceil((double) a / b));
    }

    /**
     * 执行unix命令
     * @param command
     * @return
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static String exec(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            // BufferedReader reader = new BufferedReader(new
            // InputStreamReader(process.getInputStream()));
            // int read;
            // char[] buffer = new char[4096];
            // StringBuffer output = new StringBuffer();
            // while ((read = reader.read(buffer)) > 0) {
            // output.append(buffer, 0, read);
            // }
            // reader.close();
            // process.waitFor();
            // return output.toString();
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将apk中的资源文件下载到系统文件目录中
     * @param name
     * @param localPath
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static void download(Context context, String name, String localPath) {
        try {
            InputStream in = context.getResources().getAssets().open(name);
            File outFile = new File(localPath);
            FileOutputStream out = new FileOutputStream(outFile);
            int read;
            byte[] buffer = new byte[4096];
            while ((read = in.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFromAssets(Context context, String fileName) {
        AssetManager assetManager = null;
        InputStream is = null;
        InputStreamReader inputReader = null;
        BufferedReader bufReader = null;
        try {
            Resources res = context.getResources();
            assetManager = res.getAssets();
            is = assetManager.open(fileName);
            inputReader = new InputStreamReader(is);
            bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }

                if (inputReader != null) {
                    inputReader.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //            if (assetManager != null) {
            //                assetManager.close();
            //            }
        }
    }

    public static String getFromRaw(Context context, int rawId) {
        try {
            Resources res = context.getResources();
            InputStreamReader inputReader = new InputStreamReader(res.openRawResource(rawId));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null) {
                Result += line;
            }
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断字符串是否是整型
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为null或空
     * @param str
     * @return
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }

    /**
     * 判断稀疏数组是否为null或空
     * @param arr
     * @return
     */
    public static <E> boolean isEmpty(SparseArray<E> arr) {
        return ((arr == null) || (arr.size() == 0));
    }

    /**
     * 判断稀疏数组是否为null或空
     * @param arr
     * @return
     */
    public static boolean isEmpty(SparseIntArray arr) {
        return ((arr == null) || (arr.size() == 0));
    }

    /**
     * 判断稀疏数组是否为null或空
     * @param arr
     * @return
     */
    public static boolean isEmpty(SparseBooleanArray arr) {
        return ((arr == null) || (arr.size() == 0));
    }

    /**
     * 判断集合是否为null或空
     * @param collection
     * @return
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection collection) {
        return ((collection == null) || (collection.isEmpty()));
    }

    /**
     * 判断Map是否为null或空
     * @param map
     * @return
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Map map) {
        return ((map == null) || (map.isEmpty()));
    }

    /**
     * 将字符串中所有的字符全角化，即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        if (isEmpty(input)) {
            return "";
        }
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if ((c[i] > 65280) && (c[i] < 65375)) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 去掉字符串中的标点符号和空格
     * @param input
     * @return
     */
    public static String deleteSpaceAndPunct(String input) {
        if (isEmpty(input)) {
            return "";
        }
        return input.replaceAll("[\\p{Punct}\\p{Space}]+", "");
    }

    /**
     * 分割字符串,去除了分割出每项中的空字符（"" , " "）
     * @param source 字符串
     * @param div 分割标识
     * @param isDeleted 是否去除分割出每项中的标点符号和空格
     * @return
     */
    public static String[] splitString(String source, String div, boolean isDeleted) {
        if (source == null || source.trim().equals("")) {
            return null;
        }
        String[] arr = source.split("\\" + div);
        StringBuffer sb = new StringBuffer();
        for (String s: arr) {
            if (!s.trim().equals("")) {
                if (isDeleted) {
                    s = deleteSpaceAndPunct(s);
                }
                sb.append(s + div);
            }
        }
        String[] strValue = sb.substring(0, sb.length() - 1).split("\\" + div);
        return strValue;

    }

    /**
     * 判断系统是否存在该应用
     * @param context
     * @param packName
     * @return
     */
    public static boolean isExistApp(Context context, String packName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (packageInfo.packageName.equals(packName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定的服务是否启动
     * @param context 系统上下文
     * @param serviceName 服务名称为包名+类名
     * @return
     */
    public static boolean isServiceWorked(Context context, final String serviceName) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);
        //要判断的服务名字
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceName.equals(serviceList.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取本地版本字符串(AndroidManifest.xml中配置的版本信息)
     * @return
     */
    public static String getLocalVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        String version = null;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取本地版本字符串(AndroidManifest.xml中配置的版本信息)
     * @return
     */
    public static int getLocalVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        int versionCode = Integer.MIN_VALUE;
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 删除全部文件
     * @param paramFile
     */
    public static void deleteFile(File paramFile) {
        if (paramFile.exists()) {
            if (!paramFile.isFile()) {
                if (paramFile.isDirectory()) {
                    File[] arrayOfFile = paramFile.listFiles();
                    for (int i = 0; i < arrayOfFile.length; i++) {
                        deleteFile(arrayOfFile[i]);
                    }
                }
            } else {
                paramFile.delete();
            }
            paramFile.delete();
        }
    }

    /**
     * 禁用ScrollView、ListView等控件拖动到边缘时显示黄光的效果
     * <p>
     * 因{@link android.view.View#setOverScrollMode(int)} 是从API9(2.3)开始才有的，所以API9以下的使用反射来调用
     * @param view
     */
    @SuppressWarnings({"rawtypes"})
    public static void disableOverScrollMode(View view) {
        try {
            Class viewClass = View.class;
            Method setOverScrollMode = viewClass.getMethod("setOverScrollMode", int.class);
            // View.OVER_SCROLL_NEVER = 2
            setOverScrollMode.invoke(view, 2);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    /**
     * 设置文字颜色
     * @param vew
     */
    public static void createTextColor(Context context, TextView vew, int resId) {
        XmlResourceParser xml = context.getResources().getXml(resId);
        ColorStateList colorStateList;
        try {
            colorStateList = ColorStateList.createFromXml(context.getResources(), xml);
            vew.setTextColor(colorStateList);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前应用是否是debuggable模式
     * @return
     */
    public static boolean isDebuggable(Context context) {
        return 0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    /**
     * 保存Logcat日志 <br/>
     * //TODO 还需完善，执行保存logcat日志的Process可能有多个
     * @param fileName
     */
    public static boolean saveLogToFile(String fileName) {
        boolean successed = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            String str = String.format("logcat -d -v time -f %s\n", new Object[] {fileName});
            Process process = runtime.exec(str);
            process.waitFor();
            int exitValue = process.exitValue();
            Logger.getLogger().i("saveLogToFile finished,exitCode = " + exitValue);
            if (exitValue != 0) {
                successed = false;
            } else {
                successed = true;
            }

        } catch (Exception e) {
            Logger.getLogger().e("saveLogToFile failed", e);
            successed = false;
        }
        return successed;
    }

    /**
     * 清除LogCat日志
     */
    public static boolean clearLogCat() {
        boolean successed = false;
        Runtime runtime = Runtime.getRuntime();
        try {
            String str = "logcat -c";
            Process process = runtime.exec(str);
            process.waitFor();
            int exitValue = process.exitValue();
            Logger.getLogger().i("clearLogCat finished,exitCode = " + exitValue);
            if (exitValue != 0) {
                successed = false;
            } else {
                successed = true;
            }
        } catch (Exception e) {
            Logger.getLogger().e("clearLogCat failed", e);
            successed = false;
        }
        return successed;
    }

    /**
     * px转化为dip/dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, int pxValue) {
        final float density = context.getResources().getDisplayMetrics().density;
        return Math.round(density * pxValue);
    }

    /**
     * 安装Apk
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 静默安装Apk
     * @param context
     * @param 要安装的apk包名
     * @param file
     */
    public static void installApkSilent(Context context, String packageName, File file) {
        try {
            Class packagemanage = Class.forName("android.content.pm.PackageManager");

            Class packageInstallObserver = Class
                    .forName("android.content.pm.IPackageInstallObserver");

            Method installPackage = packagemanage.getMethod("installPackage", Uri.class,
                    packageInstallObserver, int.class, String.class);
            // 0x00000002
            int INSTALL_REPLACE_EXISTING = packagemanage.getField("INSTALL_REPLACE_EXISTING")
                    .getInt(null);
            Object iActivityManager = installPackage.invoke(context.getPackageManager(),
                    Uri.fromFile(file), null, INSTALL_REPLACE_EXISTING, packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动网络设置
     * @param context
     */
    public static void startNetworkSettings(Context context) {
        Intent intent = null;

        try {
            // letv
            if ("c1".equalsIgnoreCase(getprop("persist.product.letv.name"))
                    || "c1s".equalsIgnoreCase(getprop("persist.product.letv.name"))) {
                intent = context.getPackageManager().getLaunchIntentForPackage(
                        "com.letv.t1.setting");
            }
            // 判断手机系统的版本 即API大于10 就是3.0或以上版本
            else if (android.os.Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings",
                        "com.android.settings.Settings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            Logger.getLogger().e(e.getMessage());
        }
    }

    /**
     * 保存属性到SystemProperties中
     * @param key
     * @param value
     */
    public static void setprop(String key, String value) {
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("set", String.class, String.class);
            Object obj = method.invoke(clazz, key, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从SystemProperties获取属性值
     * @param key
     * @return
     */
    public static String getprop(String key) {
        String override = null;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", String.class);
            Object obj = method.invoke(clazz, key);
            if (obj != null) {
                override = obj.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return override;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     * @return 返回包含所有包名的字符串列表
     */
    private static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        // 属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri: resolveInfo) {
            names.add(ri.activityInfo.packageName);
            Logger.getLogger().d(ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 判断当前应用是否是home
     * @param context
     * @return
     */
    public static boolean isHome(Context context) {
        String packageName = context.getPackageName();
        List<String> homePackageNames = getHomes(context);
        if (Toolkit.isEmpty(homePackageNames)) {
            return false;
        } else {
            return homePackageNames.contains(packageName);
        }
    }

    /**
     * 判断当前界面是否是桌面
     */
    public static boolean isInHome(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
        List<String> homePackageNames = getHomes(context);
        if (Toolkit.isEmpty(homePackageNames)) {
            return false;
        } else {
            return homePackageNames.contains(rti.get(0).topActivity.getPackageName());
        }
    }

    /**
     * android图片的内存优化
     */
    public static void setBackgroundResource(Context context, View view, int resId) {
        Bitmap bm = readBitMap(context, resId);
        BitmapDrawable bd = new BitmapDrawable(context.getResources(), bm);
        view.setBackgroundDrawable(bd);
    }

    /**
     * 及时释放内存
     */
    public static void recycle(Context context, View view) {
        BitmapDrawable bd = (BitmapDrawable) view.getBackground();
        if (bd != null) {
            bd.setCallback(null);
            Bitmap bmp = bd.getBitmap();
            bmp.recycle(); // 回收图片所占的内存
        }
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static void openStrictMode() {
        // if (Toolkit.isDebuggable() && VERSION.SDK_INT >= 9) {
        // Logger.getLogger().e("Open strict mode!");
        // StrictMode.setThreadPolicy(new
        // StrictMode.ThreadPolicy.Builder().detectAll()
        // .penaltyLog().build());
        // StrictMode.setVmPolicy(new
        // StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
        // .build());
        // }
    }

    /**
     * 如果焦点从GridView上的一个Item移出，使GridView失去焦点，当焦点返回GridView时，
     * 如果第一个获得焦点的Item是上次最后移出那个Item此时不会触发OnItemSelectedListener中的onItemSelected事件 ，
     * 使用下面反射的代码清除默认焦点，达到预期效果
     * @param gridView
     */
    public static void clearGridViewFocus(GridView gridView) {
        try {
            @SuppressWarnings("unchecked")
            Class<GridView> c = (Class<GridView>) Class.forName("android.widget.GridView");
            Method[] flds = c.getDeclaredMethods();
            for (Method f: flds) {
                if ("setSelectionInt".equals(f.getName())) {
                    f.setAccessible(true);
                    f.invoke(gridView, new Object[] {Integer.valueOf(-1)});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // android判断应用是否是内置的
    public static boolean isSystemApplication(Context context, String packageName) {
        boolean isflag = false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo pInfo = pm
                    .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            if ((pInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                isflag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isflag;
    }

    /**
     * 将时长转化为时间字符串，单位毫秒
     * @param duration in ms
     * @return
     */
    public static String parseDuration(int duration) {
        StringBuffer formatBuilder = new StringBuffer();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());
        int totalSeconds = duration / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        if (hours > 0) {
            return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
