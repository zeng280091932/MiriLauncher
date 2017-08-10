/* 
 * 文件名：AppRecommendService.java
 * 版权：Copyright
 */
package com.miri.launcher.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.IntentService;
import android.content.Intent;

import com.miri.launcher.Constants;
import com.miri.launcher.Init;
import com.miri.launcher.PersistData;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParser;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.msg.model.doc.AppRecommend;
import com.miri.launcher.utils.FileUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-20
 */
public class AppRecommendService extends IntentService {

    public static final String RECOMMEND_ACTION = "com.miri.launcher.recommend.app";

    public AppRecommendService() {
        super("AppRecommendService");
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
            Logger.getLogger().d("==========request app recommend =============");
            try {
                AppRecommend appRec = parseAppRecommend(PersistData.getAppRecommendUrl());
                if (appRec == null || Toolkit.isEmpty(appRec.getVersion())) {
                    Logger.getLogger().e("app recommend data is null or version is null!");
                    return;
                }
                //本地版本
                String localVersionNo = null;
                if (PersistData.mAppRecommend != null) {
                    localVersionNo = PersistData.mAppRecommend.getVersion();
                }

                //需要更新的条件
                boolean isNeedUpdate = PersistData.mAppRecommend == null
                        || Toolkit.isEmpty(localVersionNo)
                        || !localVersionNo.equals(appRec.getVersion());

                Logger.getLogger().d("is need update?" + isNeedUpdate);
                Logger.getLogger().d("latest version is : " + appRec.getVersion());

                if (isNeedUpdate) {
                    writeAppRecommend(appRec);
                    PersistData.mAppRecommend = appRec;
                    //发送更新广播
                    Intent it = new Intent();
                    it.setAction(RECOMMEND_ACTION);
                    sendBroadcast(it);
                    Logger.getLogger().d("=======send app recommend update broadcast========");
                }
            } catch (NetWorkInfoException e) {
                e.printStackTrace();
            } catch (JsonParserException e) {
                e.printStackTrace();
            }
        } else {
            if (PersistData.mAppRecommend == null) {
                PersistData.mAppRecommend = loadDefaultAppRecommend();
                //发送更新广播
                Intent it = new Intent();
                it.setAction(RECOMMEND_ACTION);
                sendBroadcast(it);
                Logger.getLogger().d("=======send app recommend update broadcast========");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.getLogger().d("onDestroy()!");
    }

    /**
     * 解析网络文件的应用推荐
     * @return
     * @throws NetWorkInfoException
     * @throws JsonParserException
     */
    private static AppRecommend parseAppRecommend(String url) throws NetWorkInfoException,
            JsonParserException {
        AppRecommend commend = (AppRecommend) JsonParser.parse(url, AppRecommend.class);
        return commend;
    }

    /**
     * 写入推荐数据到文件缓存
     * @param appRec
     */
    private void writeAppRecommend(AppRecommend appRec) {
        String jsonStr = JsonParser.generateJson(appRec);
        //        Logger.getLogger().d("app recommend json:" + jsonStr);
        File file = getSaveFilePath();
        if (file != null) {
            String filePath = file.getAbsolutePath();
            Logger.getLogger().d("app recommend write file Path:" + filePath);
            if (file.exists()) {
                file.delete();
                file = new File(filePath);
            }

            //写入文件
            FileWriter fw = null;
            try {
                fw = new FileWriter(file);
                fw.write(jsonStr, 0, jsonStr.length());
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    /**
     * 加载本地的应用推荐数据
     * @param context
     * @param fileName
     * @return
     */
    private AppRecommend loadDefaultAppRecommend() {
        //先读取缓存文件
        AppRecommend result = loadCacheRecommendFile();
        if (result != null) {
            return result;
        }
        return loadLocalAssetsFile();
    }

    /**
     * 加载缓存的本地文件
     * @return
     */
    private AppRecommend loadCacheRecommendFile() {
        AppRecommend result = null;
        File file = getSaveFilePath();
        if (file != null && file.exists()) {
            Logger.getLogger().d("app recommend save file path:" + file.getAbsolutePath());
            String defaultJson = FileUtil.readFile2Str(file);
            try {
                result = (AppRecommend) JsonParser.parseForString(defaultJson, AppRecommend.class,
                        -1);
            } catch (JsonParserException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 加载assets本地默认文件
     * @return
     */
    private AppRecommend loadLocalAssetsFile() {
        AppRecommend result = null;
        //取本地默认数据 
        InputStream is = null;
        InputStreamReader inputReader = null;
        try {
            is = getAssets().open(Constants.DEFAULT_RECOMMEND_FILENAME);
            inputReader = new InputStreamReader(is);
            try {
                result = (AppRecommend) JsonParser.parseForStream(inputReader, AppRecommend.class,
                        -1);
            } catch (JsonParserException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputReader != null) {
                    inputReader.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 获取推荐文件数据的保存路径
     * @return
     */
    private File getSaveFilePath() {
        // 使用内置存储
        File cacheDir = getCacheDir();
        if (cacheDir == null) {
            return null;
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        String fileName = cacheDir.getAbsolutePath() + File.separator
                + PersistData.getCacheAppRecomName();
        File filePath = new File(fileName);
        return filePath;
    }

}
