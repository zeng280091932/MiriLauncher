/* 
 * 文件名：CoreService.java
 * 描述： 
 * 修改人：
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.miri.launcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.miri.launcher.Init;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.msg.exception.MsgInfoException;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;

/**
 * 核心Service，用来引导数据
 */
public class CoreService extends Service {

    public static final String BOOT_BROASTCAST = "boot_broadcast";

    //消息接口引导成功
    public static final int BOOT_MESSAGE_SUCCESS = 1000;

    //消息接口引导已经成功
    public static final int BOOT_MESSAGE_SUCCESSED = 1001;

    //消息接口导失败
    public static final int BOOT_MESSAGE_FAIL = 1002;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.getLogger().d("create core service!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.getLogger().d("start core service!");
        InitThread initThread = new InitThread();
        initThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Logger.getLogger().e("destory core service!");
        super.onDestroy();
    }

    class InitThread extends CommonThread {

        @Override
        public void run() {
            try {
                //检测网络连接
                boolean isConnected = NetworkUtil.isNetworkAvailable(getApplicationContext());

                if (!isConnected) {
                    Logger.getLogger().e("network is disconnected.");
                    return;
                }

                if (Init.getInitState() == Init.State.RUNNING) {
                    Logger.getLogger().d("Init is now initing! skip this operation.");
                    return;
                }
                if (Init.getInitState() == Init.State.PENDING
                        || Init.getInitState() == Init.State.FAILURE) {
                    Logger.getLogger().d("Init is not run or run failure, so call Init.initMsg().");
                    Init.initMsg();
                    Intent it = new Intent(getApplicationContext(), AppRecommendService.class);
                    startService(it);
                    sendBootBroadcast(BOOT_MESSAGE_SUCCESS);
                } else if (Init.getInitState() == Init.State.SUCCESS) {
                    Logger.getLogger().d("Init success, so skip Init.initMsg()!");
                    sendBootBroadcast(BOOT_MESSAGE_SUCCESSED);
                }

            } catch (NetWorkInfoException e) {
                Logger.getLogger().e("连接网络出错", e);
                sendBootBroadcast(BOOT_MESSAGE_FAIL);
            } catch (JsonParserException e) {
                Logger.getLogger().e("解析消息数据错误", e);
                sendBootBroadcast(BOOT_MESSAGE_FAIL);
            } catch (MsgInfoException e) {
                Logger.getLogger().e("解析首页epg数据网络错误", e);
                int errorCode = e.getErrorCode();
                sendBootBroadcast(BOOT_MESSAGE_FAIL, errorCode);
            }

        }

        private void sendBootBroadcast(int what) {
            Intent i = new Intent(BOOT_BROASTCAST);
            i.putExtra("what", what);
            sendBroadcast(i);
        }

        private void sendBootBroadcast(int what, int arg) {
            Intent i = new Intent(BOOT_BROASTCAST);
            i.putExtra("what", what);
            i.putExtra("arg", arg);
            sendBroadcast(i);
        }

    }

}
