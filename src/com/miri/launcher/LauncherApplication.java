package com.miri.launcher;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;

import com.miri.launcher.crashReporter.CrashHandler;
import com.miri.launcher.db.AppFavSettings.AppFavoritesColumns;
import com.miri.launcher.receiver.LauncherModel;
import com.miri.launcher.utils.Logger;

public class LauncherApplication extends android.app.Application {

    private static LauncherApplication instance;

    private LauncherModel mModel;

    public static LauncherApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        Logger.getLogger().d("MiRiLauncher Application onCreate()");
        //        //设置堆最小为8M，使用率超过75%时重新分配堆大小，android 2.2可用
        //        VMRuntime.getRuntime().setTargetHeapUtilization(0.75F);
        //        //设置最小堆栈值为8M
        //        VMRuntime.getRuntime().setMinimumHeapSize(8 * 1024 * 1024);
        instance = this;
        super.onCreate();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        mModel = new LauncherModel(this);

        // Register intent receivers
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mModel, filter);

        //        filter = new IntentFilter();
        //        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        //        filter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        //        registerReceiver(mModel, filter);

        //create databases
        //Register for changes to the favorites
        ContentResolver resolver = getContentResolver();
        resolver.registerContentObserver(AppFavoritesColumns.getContentUri(true), false,
                mFavoritesObserver);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.getLogger().e("===========LowMemory============");
        System.gc();
    }

    @Override
    public void onTerminate() {
        Logger.getLogger().e("===========onTerminate============");
        super.onTerminate();

        unregisterReceiver(mModel);

        ContentResolver resolver = getContentResolver();
        resolver.unregisterContentObserver(mFavoritesObserver);
    }

    /**
     * Receives notifications whenever the user favorites have changed.
     */
    private final ContentObserver mFavoritesObserver = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {

        }
    };

    public LauncherModel getModel() {
        return mModel;
    }

}
