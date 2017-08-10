/* 
 * 文件名：HeartReportService.java
 * 版权：Copyright
 */
package com.miri.launcher.service;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;

import com.miri.launcher.Init;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.msg.MsgParser;
import com.miri.launcher.msg.exception.MsgInfoException;
import com.miri.launcher.msg.model.Operate;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-20
 */
public class HeartReportService extends IntentService {

    public HeartReportService() {
        super("HeartReportService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.getLogger().d("onCreate()!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Init.getInitState() == Init.State.SUCCESS
                && NetworkUtil.isNetworkAvailable(getApplicationContext())) {
            try {
                List<Operate> operateInfos = MsgParser.heart();
                //TODO 开启异步任务消息处理
                Logger.getLogger().d("heart operateInfo : " + operateInfos);
            } catch (NetWorkInfoException e) {
                e.printStackTrace();
            } catch (JsonParserException e) {
                e.printStackTrace();
            } catch (MsgInfoException e) {
                e.printStackTrace();
            }
        } else {
            Logger.getLogger().i("Do not have heart task!! ");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.getLogger().d("onDestroy()!");
    }

}
