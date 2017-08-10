/* 
 * 文件名：LauncherModel.java
 * */
package com.miri.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import com.miri.launcher.LauncherApplication;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version 2013-6-4
 */
public class LauncherModel extends BroadcastReceiver {

    private final LauncherApplication mApp;

    private final Object mLock = new Object();

    private static final HandlerThread sWorkerThread = new HandlerThread("launcher-loader");
    static {
        sWorkerThread.start();
    }

    private static final Handler sWorker = new Handler(sWorkerThread.getLooper());

    public LauncherModel(LauncherApplication app) {
        mApp = app;
    }

    enum PackageTaskState {
        OP_NONE, //挂起
        OP_ADD, //添加
        OP_UPDATE, //更新
        OP_REMOVE //删除
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.getLogger().d("onReceive intent=" + intent);

        final String action = intent.getAction();

        if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
                || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                || Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            final String packageName = intent.getData().getSchemeSpecificPart();
            //判断是否是替代原软件
            final boolean replacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);

            PackageTaskState state = PackageTaskState.OP_NONE;

            if (packageName == null || packageName.length() == 0) {
                // they sent us a bad intent
                return;
            }
            if (Intent.ACTION_PACKAGE_CHANGED.equals(action)) {
                state = PackageTaskState.OP_UPDATE;
            } else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
                if (!replacing) {
                    state = PackageTaskState.OP_REMOVE;
                }
                // else, we are replacing the package, so a PACKAGE_ADDED will be sent
                // later, we will update the package at this time
            } else if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                if (!replacing) {
                    state = PackageTaskState.OP_ADD;
                } else {
                    state = PackageTaskState.OP_UPDATE;
                }
            }
            Logger.getLogger().d("Package Task State--->" + state);

            if (state != PackageTaskState.OP_NONE) {
                sWorker.post(new PackageTaskRunnable(state, new String[] {packageName}));
            }
        }

    }

    private class PackageTaskRunnable implements Runnable {

        private String[] mPackages;

        private PackageTaskState state;

        public PackageTaskRunnable(PackageTaskState state, String[] mPackages) {
            this.mPackages = mPackages;
            this.state = state;
        }

        @Override
        public void run() {
            final Context context = mApp;
            final String[] packages = mPackages;
            if (packages == null || packages.length == 0) {
                return;
            }
            Logger.getLogger().e("PackageTaskState: " + state);
            if (state == PackageTaskState.OP_ADD || state == PackageTaskState.OP_UPDATE) {
                for (int i = 0; i < packages.length; i++) {

                    //卸载上一个版本
                    if (context.getPackageName().equals(packages[i])) {
                        String orinalName = "com.launcher.tv";
                        if (Toolkit.isExistApp(context, orinalName)) {
                            Logger.getLogger().e("unistall orinal version!!!");
                            try {
                                Uri packageURI = Uri.parse("package:" + orinalName);
                                Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
                                        packageURI);
                                context.startActivity(uninstallIntent);
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        }

    }

}
