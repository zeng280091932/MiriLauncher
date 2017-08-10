/*
 * 
 */
package com.miri.launcher.db.manage;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.miri.launcher.db.DBOpenHelper;
import com.miri.launcher.moretv.model.MediaInfo;

/**
 * 找你妹游戏答案选项图片数据库帮助类
 * 
 * @author zengjiantao
 * @date 2013-3-18
 */
public class RecommendDBHelper {
    private static final String TAG = "RecommendDBHelper";

    private static final String TABLE_NAME = "recommend";

    private static final String[] COLUMS = { "link_data", "title", "is_hd",
        "content_type", "episode_count", "episode", "score", "image" };

    private static DBOpenHelper dbOpenHelper;

    private static RecommendDBHelper instance;

    private RecommendDBHelper(Context context) {
        dbOpenHelper = DBOpenHelper.getInstance(context);
    }

    public static RecommendDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RecommendDBHelper(context);
        }
        return instance;
    }

    /**
     * 创建表
     * 
     * @param db
     */
    private static void createTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + TABLE_NAME + " ("
                + COLUMS[0] + " varchar primary key," + COLUMS[1] + " varchar,"
                + COLUMS[2] + " varchar," + COLUMS[3] + " varchar," + COLUMS[4]
                        + " varchar," + COLUMS[5] + " varchar," + COLUMS[6]
                                + " varchar," + COLUMS[7] + " varchar)");
    }

    /**
     * 删除表
     * 
     * @param db
     */
    private static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public static void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion,
            int newVersion) {
        dropTable(db);
        createTable(db);
    }

    /**
     * 添加媒体信息
     * 
     * @param mediaInfo
     */
    public void save(MediaInfo mediaInfo) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        Object[] arr = new Object[COLUMS.length];
        arr[0] = mediaInfo.getLinkData();
        arr[1] = mediaInfo.getTitle();
        arr[2] = mediaInfo.getIsHd();
        arr[3] = mediaInfo.getContentType();
        arr[4] = mediaInfo.getEpisodeCount();
        arr[5] = mediaInfo.getEpisode();
        arr[6] = mediaInfo.getScore();
        arr[7] = mediaInfo.getImage1();
        db.execSQL("insert into " + TABLE_NAME + " (" + COLUMS[0] + ","
                + COLUMS[1] + "," + COLUMS[2] + "," + COLUMS[3] + ","
                + COLUMS[4] + "," + COLUMS[5] + "," + COLUMS[6] + ","
                + COLUMS[7] + ") values (?,?,?,?,?,?,?,?)", arr);
    }

    /**
     * 批量插入媒体信息
     * 
     * @param mediaInfos
     */
    public void save(List<MediaInfo> mediaInfos) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Object[] arr = new Object[COLUMS.length];
            for (MediaInfo mediaInfo : mediaInfos) {
                arr[0] = mediaInfo.getLinkData();
                arr[1] = mediaInfo.getTitle();
                arr[2] = mediaInfo.getIsHd();
                arr[3] = mediaInfo.getContentType();
                arr[4] = mediaInfo.getEpisodeCount();
                arr[5] = mediaInfo.getEpisode();
                arr[6] = mediaInfo.getScore();
                arr[7] = mediaInfo.getImage1();
                db.execSQL("insert into " + TABLE_NAME + " (" + COLUMS[0] + ","
                        + COLUMS[1] + "," + COLUMS[2] + "," + COLUMS[3] + ","
                        + COLUMS[4] + "," + COLUMS[5] + "," + COLUMS[6] + ","
                        + COLUMS[7] + ") values (?,?,?,?,?,?,?,?)", arr);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 结束事务
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 获取所有的推荐影片
     * 
     * @param num
     * @return
     */
    public List<MediaInfo> findAll() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<MediaInfo> mediaInfos = new ArrayList<MediaInfo>();
        try {
            if (cursor != null) {
                int linkDataIndex = cursor.getColumnIndex(COLUMS[0]);
                int titleIndex = cursor.getColumnIndex(COLUMS[1]);
                int isHdIndex = cursor.getColumnIndex(COLUMS[2]);
                int contentTypeIndex = cursor.getColumnIndex(COLUMS[3]);
                int episodeCountIndex = cursor.getColumnIndex(COLUMS[4]);
                int episodeIndex = cursor.getColumnIndex(COLUMS[5]);
                int scoreIndex = cursor.getColumnIndex(COLUMS[6]);
                int imageIndex = cursor.getColumnIndex(COLUMS[7]);
                MediaInfo mediaInfo = null;
                while (cursor.moveToNext()) {
                    mediaInfo = new MediaInfo();
                    mediaInfo.setLinkData(cursor.getString(linkDataIndex));
                    mediaInfo.setTitle(cursor.getString(titleIndex));
                    mediaInfo.setIsHd(cursor.getString(isHdIndex));
                    mediaInfo
                    .setContentType(cursor.getString(contentTypeIndex));
                    mediaInfo.setEpisodeCount(cursor
                            .getString(episodeCountIndex));
                    mediaInfo.setEpisode(cursor.getString(episodeIndex));
                    mediaInfo.setScore(cursor.getString(scoreIndex));
                    mediaInfo.setImage1(cursor.getString(imageIndex));
                    mediaInfos.add(mediaInfo);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "查询数据库出错" + e);
        } finally {
            cursor.close();
        }
        return mediaInfos;
    }

    /**
     * 删除全部数据
     */
    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
    }
}
