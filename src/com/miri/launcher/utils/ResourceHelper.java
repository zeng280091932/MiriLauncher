/* 
 * 文件名：ResourceHelper.java
 */
package com.miri.launcher.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * 资源文件工具类<br>
 * 1.根据获取的屏幕分辨率来设置Configuration
 */
public class ResourceHelper {

    /**
     * 小分辨率960*540
     */
    private static final int SMALL = 960;

    /**
     * 中等分辨率1280*720
     */
    private static final int NORMAL = 1280;

    /**
     * 大分辨率1920*1080
     */
    private static final int LARGE = 1920;

    /**
     * 更新资源配置信息
     * @param context
     * @see [类、类#方法、类#成员]
     * @since [产品/模块版本]
     */
    public static void updateConfig(Context context) {
        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        //获取屏幕信息
        DisplayMetrics dm = new DisplayMetrics();
        if (!(context instanceof Activity)) {
            return;
        }
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int sw = Math.max(width, height);

        dm.densityDpi = DisplayMetrics.DENSITY_MEDIUM;
        dm.density = 1;
        dm.scaledDensity = 1;

        if (sw >= LARGE) {
            conf.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
        } else if (sw >= NORMAL) {
            conf.screenLayout = Configuration.SCREENLAYOUT_SIZE_NORMAL;
        } else {
            conf.screenLayout = Configuration.SCREENLAYOUT_SIZE_SMALL;
        }
        dm.densityDpi = Math.round(DisplayMetrics.DENSITY_MEDIUM * sw / 1280F);
        dm.density = sw / 1280F;
        dm.scaledDensity = sw / 1280F;

        //        Toast.makeText(context, "dm" + dm.toString(), Toast.LENGTH_LONG).show();
        res.updateConfiguration(conf, dm);
        Logger.getLogger().d("dm info-->" + dm.toString());
        //        updateConfig(conf);
    }

    @Deprecated
    public static void updateConfig(Configuration newConfig) {
        try {
            Class activityManageNative = Class.forName("android.app.ActivityManagerNative");
            Method getDefault = activityManageNative.getMethod("getDefault");
            Object iActivityManager = getDefault.invoke(activityManageNative);
            Class iActivityManagerClazz = Class.forName("android.app.IActivityManager");
            Method getConfig = iActivityManagerClazz.getMethod("getConfiguration");
            Configuration config = (Configuration) getConfig.invoke(iActivityManager);
            Method updateConfig = iActivityManagerClazz.getMethod("updateConfiguration",
                    Configuration.class);
            updateConfig.invoke(iActivityManager, newConfig);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
