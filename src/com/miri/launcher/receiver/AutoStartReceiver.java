/* 
 * 文件名：AutoStartReceiver.java
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.miri.launcher.service.AppRecommendService;
import com.miri.launcher.service.CoreService;
import com.miri.launcher.service.HeartReportService;
import com.miri.launcher.utils.Logger;

/**
 * 监听系统开机广播，启动服务
 */
public class AutoStartReceiver extends BroadcastReceiver {

    //核心消息轮询间隔时间
    public static final int CORESERVICE_PERIOD = 5 * 1000;

    //应用推荐数据轮询间隔时间5分钟  (test 1分钟)
    public static final int APPRECOMMEND_PERIOD = 10 * 60 * 1000;

    /** 心跳启动延迟时间 */
    public static final int HEART_DELAY = 5000;

    /** 心跳间隔时间 30秒 */
    public static final int HEART_INTERVAL = 30 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.getLogger().i("Auto Start Receiver");
        //        startCoreServiceTimer(context);
        //        startAppRecommendServiceTimer(context);
        Intent it = new Intent(context, CoreService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(it);
        it = new Intent(context, AppRecommendService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(it);
        it = new Intent(context, HeartReportService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(it);

    }

    private void startCoreServiceTimer(Context context) {
        Intent it = new Intent(context, CoreService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, it, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        Logger.getLogger().i("first time-->" + firstTime);
        am.cancel(pendingIntent);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, CORESERVICE_PERIOD,
                pendingIntent);
    }

    private void startAppRecommendServiceTimer(Context context) {
        Intent it = new Intent(context, AppRecommendService.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, it, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        Logger.getLogger().i("first time-->" + firstTime);
        am.cancel(pendingIntent);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, APPRECOMMEND_PERIOD,
                pendingIntent);
    }

}
