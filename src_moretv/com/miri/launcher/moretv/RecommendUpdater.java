/**
 * 
 */
package com.miri.launcher.moretv;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.db.manage.MoretvManager;
import com.miri.launcher.db.manage.RecommendDBHelper;
import com.miri.launcher.imageCache.ImageLoader;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.moretv.model.doc.RecommendDocument;
import com.miri.launcher.utils.Toolkit;

/**
 * 首页推荐更新任务
 * 
 * @author zengjiantao
 * 
 */
public class RecommendUpdater {
    // 4小时
    private static final long UPDATE_INTERVAL = 1 * 60 * 60 * 1000;
    // private static final long UPDATE_INTERVAL = 5 * 1000;

    public static final String RECOMMEND_ACTION = "com.miri.launcher.moretv.recommend";

    private final Context mContext;

    private Timer timer;

    private TimerTask mUpdateTask;

    private boolean mIsStart = false;

    private final SharedPreferences mPreference;

    private final RecommendDBHelper mDbHelper;

    private final ImageLoader mImgLoader;

    public RecommendUpdater(Context context) {
        mContext = context;
        mPreference = mContext.getSharedPreferences(
                MoretvConstants.PREFERENCES_RECOMMEND_VERSION,
                Context.MODE_PRIVATE);
        mDbHelper = RecommendDBHelper.getInstance(mContext);
        mImgLoader = ImageLoader.from(mContext);
    }

    /**
     * 初始化
     */
    private void init() {
        timer = new Timer();
        mUpdateTask = new TimerTask() {

            @Override
            public void run() {
                RecommendDocument document = MoretvManager
                        .fetchRecommendDocument();
                if (document != null) {
                    String version = document.getVersion();
                    String localVersion = getSavedVersion();
                    Log.d("Debug", "version:" + version);
                    Log.d("Debug", "localVersion:" + localVersion);
                    if (!Toolkit.isEmpty(version)
                            && version.compareToIgnoreCase(localVersion) > 0) {
                        List<MediaInfo> mediaInfos = document.getMediaInfos();
                        if (!Toolkit.isEmpty(mediaInfos)) {
                            mDbHelper.deleteAll();
                            mDbHelper.save(mediaInfos);
                            for (MediaInfo mediaInfo : mediaInfos) {
                                String url = mediaInfo.getImage1();
                                Log.d("Debug", "url:" + url);
                                mImgLoader.loadImg(url);
                            }
                            saveVersion(version);
                            MoretvData.sRecommendDocument = document;
                            Intent intent = new Intent();
                            intent.setAction(RECOMMEND_ACTION);
                            mContext.sendBroadcast(intent);
                        }
                    }
                }
            }
        };
    }

    /**
     * 获取已经保存的版本号
     * 
     * @return
     */
    private String getSavedVersion() {
        String version = mPreference.getString(
                MoretvConstants.KEY_RECOMMEND_VERSION, "");
        if (Toolkit.isEmpty(version)) {
            version = MoretvManager.fetchDefaultVersion(mContext);
        }
        return version;
    }

    /**
     * 保存版本号
     * 
     * @param version
     */
    private void saveVersion(String version) {
        Editor editor = mPreference.edit();
        editor.putString(MoretvConstants.KEY_RECOMMEND_VERSION, version);
        editor.commit();
    }

    /**
     * 开始更新任务
     */
    public void start() {
        if (!mIsStart) {
            init();
            timer.schedule(mUpdateTask, 0, UPDATE_INTERVAL);
            mIsStart = true;
        }
    }

    /**
     * 停止更新任务
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
            mUpdateTask = null;
            timer = null;
            mIsStart = false;
        }
    }

}
