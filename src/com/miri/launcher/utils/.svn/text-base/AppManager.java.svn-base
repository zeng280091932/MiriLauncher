package com.miri.launcher.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;

import com.miri.launcher.model.AppInfo;

/**
 * 应用管理工具
 * 
 * @author Administrator
 */
public class AppManager {

    public static ArrayList<AppInfo> findAllApps(Context contenxt) {
        ArrayList<AppInfo> mApplications = null;

        PackageManager manager = contenxt.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(
                mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();
            mApplications = new ArrayList<AppInfo>(count);

            for (int i = 0; i < count; i++) {
                AppInfo application = new AppInfo();
                ResolveInfo info = apps.get(i);
                String packageName = info.activityInfo.applicationInfo.packageName;
                if (!Toolkit.isEmpty(packageName)
                        && !packageName.equals(contenxt.getPackageName())) {
                    // Logger.getLogger().d("名称：" + info.loadLabel(manager));
                    // Logger.getLogger().d("包名：" +
                    // info.activityInfo.applicationInfo.packageName);

                    application.title = info.loadLabel(manager);
                    application
                    .setActivity(
                            new ComponentName(
                                    info.activityInfo.applicationInfo.packageName,
                                    info.activityInfo.name),
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    application.icon = info.activityInfo.loadIcon(manager);

                    mApplications.add(application);
                }

            }
        }
        return mApplications;
    }

    /**
     * 启动应用
     * 
     * @param context
     * @param packageName
     */
    public static void runApp(Context context, String packageName) {
        PackageInfo pi;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            // resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pi.packageName);
            PackageManager pManager = context.getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(
                    resolveIntent, 0);

            boolean isHas = apps.iterator().hasNext();
            if (!isHas) {
                return;
            }
            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                // intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                context.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断下载的apk是否已安装
     * 
     * @param filePath
     * @return
     */
    public static boolean isInstall(Context context, String filePath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filePath,
                    PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                String packageName = appInfo.packageName;
                return Toolkit.isExistApp(context, packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
