/* 
 * 文件名：CrashReporterService.java
 */
package com.miri.launcher.crashReporter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.miri.launcher.utils.NetworkUtil;

/**
 * 通过邮件发送错误日志文件
 */
public class CrashReporterService extends Service {

    private CrashHandler crashHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        crashHandler = CrashHandler.getInstance();
        super.onCreate();
        Context ctx = getApplicationContext();
        //检测网络
        if (NetworkUtil.isNetworkAvailable(ctx)) {
            new SendCrashReporterTask(ctx, crashHandler).execute();
        } else {
            //TODO 如果网络不通，则删除日志
            crashHandler.delCrashReportFiles(ctx);
        }
    }

    class SendCrashReporterTask extends AsyncTask<Void, Void, Void> {

        private Context context;

        private CrashHandler crashHandler;

        public SendCrashReporterTask(Context context, CrashHandler crashHandler) {
            this.context = context;
            this.crashHandler = crashHandler;
        }

        @Override
        protected void onPostExecute(Void result) {
            CrashReporterService.this.stopSelf();
        }

        @Override
        protected Void doInBackground(Void... params) {
            crashHandler.sendPreviousReportsToServer(context);
            return null;
        }
    }

}
