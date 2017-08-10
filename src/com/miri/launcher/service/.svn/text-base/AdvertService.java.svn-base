/* 
 * 文件名：AdvertService.java
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
import com.miri.launcher.msg.model.doc.AdvertList;
import com.miri.launcher.utils.FileUtil;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-20
 */
public class AdvertService extends IntentService {

    public static final String ADVERTLIST_ACTION = "com.miri.launcher.recommend.advert";

    public AdvertService() {
        super("AdvertService");
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
            Logger.getLogger().d("==========request advert recommend=============");
            try {
                String url = PersistData.getAdvertUrl();
                AdvertList advertList = parseAdservt(url);
                if (advertList == null || Toolkit.isEmpty(advertList.getVersion())) {
                    Logger.getLogger().e("advert data is null or version is null!");
                    return;
                }
                //本地版本
                String localVersionNo = null;
                if (PersistData.mAdvertList != null) {
                    localVersionNo = PersistData.mAdvertList.getVersion();
                }

                //需要更新的条件
                boolean isNeedUpdate = PersistData.mAdvertList == null
                        || Toolkit.isEmpty(localVersionNo)
                        || !localVersionNo.equals(advertList.getVersion());

                Logger.getLogger().d("is need update?" + isNeedUpdate);

                if (isNeedUpdate) {
                    Logger.getLogger().d("latest version is : " + advertList.getVersion());
                    writeAdverList(advertList);
                    PersistData.mAdvertList = advertList;
                    //发送更新广播
                    Intent it = new Intent();
                    intent.setAction(ADVERTLIST_ACTION);
                    sendBroadcast(it);
                    Logger.getLogger().d("=======send advert update broadcast========");
                }
            } catch (NetWorkInfoException e) {
                e.printStackTrace();
            } catch (JsonParserException e) {
                e.printStackTrace();
            }
        } else {
            if (PersistData.mAdvertList == null) {
                PersistData.mAdvertList = loadDefaultAdvertList();
                //发送更新广播
                Intent it = new Intent();
                it.setAction(ADVERTLIST_ACTION);
                sendBroadcast(it);
                Logger.getLogger().d("=======send advert update broadcast========");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.getLogger().d("onDestroy()!");
    }

    /**
     * 解析广告文件
     * @return
     * @throws NetWorkInfoException
     * @throws JsonParserException
     */
    private static AdvertList parseAdservt(String urlString) throws NetWorkInfoException,
            JsonParserException {
        AdvertList ad = (AdvertList) JsonParser.parse(urlString, AdvertList.class, -1);
        return ad;
    }

    /**
     * 写入推荐数据到文件缓存
     * @param appRec
     */
    private void writeAdverList(AdvertList ad) {
        String jsonStr = JsonParser.generateJson(ad);
        File file = getSaveFilePath();
        if (file != null) {
            String filePath = file.getAbsolutePath();
            Logger.getLogger().d("advert list write file Path:" + filePath);
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
     * 加载广告推荐数据
     * @param context
     * @param fileName
     * @return
     */
    private AdvertList loadDefaultAdvertList() {
        AdvertList result = loadCacheAdvertFile();
        if (result != null) {
            return result;
        }
        return loadLocalAssetsFile();
    }

    /**
     * 加载缓存的本地文件
     * @return
     */
    private AdvertList loadCacheAdvertFile() {
        AdvertList result = null;
        File file = getSaveFilePath();
        if (file != null && file.exists()) {
            Logger.getLogger().d("app recommend save file path:" + file.getAbsolutePath());
            String defaultJson = FileUtil.readFile2Str(file);
            try {
                result = (AdvertList) JsonParser.parseForString(defaultJson, AdvertList.class, -1);
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
    private AdvertList loadLocalAssetsFile() {
        AdvertList result = null;
        //取本地默认数据 
        InputStream is = null;
        InputStreamReader inputReader = null;
        try {
            is = getAssets().open(Constants.DEFAULT_ADVERT_FILENAME);
            inputReader = new InputStreamReader(is);
            try {
                result = (AdvertList) JsonParser.parseForStream(inputReader, AdvertList.class, -1);
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
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        String fileName = cacheDir.getAbsolutePath() + File.separator
                + PersistData.getCacheAdvertName();
        //        try {
        //            openFileOutput(fileName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE)
        //                    .write(0);
        //        } catch (FileNotFoundException e) {
        //            e.printStackTrace();
        //            return null;
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //            return null;
        //        }
        File filePath = new File(fileName);
        return filePath;
    }

}
