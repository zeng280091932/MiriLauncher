package com.miri.launcher.market;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

final public class Dao {

    private DBOpenHelper helper;

    private SQLiteDatabase db;

    private Context context;

    /**
     * <p>
     * Record the reference count of a instance of this class, so that the database can be closed
     * automatically when the reference count decrease to 0.
     * </p>
     */
    private int referenceCount;

    public Dao(Context context) {
        this.context = context;
        open();
        referenceCount = 1;
    }

    public void open() {
        helper = new DBOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    // Return true if the instance of this class can still be used, otherwise return false.
    public synchronized boolean incReferenceCount() {
        if (referenceCount > 0) {
            referenceCount++;
            return true;
        }
        return false;
    }

    public synchronized void decReferenceCount() {
        if (--referenceCount == 0) {
            close();
        }
    }

    /**
     * Insert a record of a thread of an undone downloading item.<br>
     * 插入一个下载记录
     * @param info
     */
    public void insertUndone(RecordInfo info) {
        db.execSQL(
                "INSERT INTO downloading(url,filePath,thid,done,appName,iconPath) VALUES(?,?,?,?,?,?)",
                new Object[] {info.getUrl(), info.getFilePath(), info.getThreadId(),
                        info.getDone(), info.getAppName(), info.getIconPath()});
    }

    /**
     * Insert a record of a done downloading item.<br>
     * 插入已下载的记录
     * @param info
     */
    public void insertDone(RecordInfo info) {
        db.execSQL(
                "INSERT INTO completed(url,  filePath, fileLen, appName) VALUES(?, ?, ?, ?)",
                new Object[] {info.getUrl(), info.getFilePath(), info.getFileLen(),
                        info.getAppName()});
    }

    /**
     * Update the progress of a thread of a downloading item.
     * @param info
     */
    public void update(RecordInfo info) {
        db.execSQL(
                "UPDATE downloading SET done=? WHERE url=? AND filePath=? AND thid=?",
                new Object[] {info.getDone(), info.getUrl(), info.getFilePath(), info.getThreadId()});
    }

    /**
     * Query the record of a thread of a downloading item.
     * @param url
     * @param filePath
     * @param thid
     * @return
     */
    public RecordInfo query(String url, String filePath, int thid) {
        Cursor c = db
                .rawQuery(
                        "SELECT url,filePath,thid,done,appName,iconPath FROM downloading WHERE url=? AND filePath=? AND thid=?",
                        new String[] {url, filePath, String.valueOf(thid)});
        RecordInfo apkInfo = null;
        if (c.moveToNext()) {
            apkInfo = new RecordInfo(c.getString(0), c.getString(1), c.getInt(2), c.getInt(3),
                    c.getString(4), c.getString(5));
        }
        c.close();
        return apkInfo;
    }

    @Deprecated
    public boolean isExist(String url) {
        Cursor c = db.rawQuery("SELECT url FROM completed WHERE url=?", new String[] {url});
        boolean isExist = false;
        if (c.getCount() > 0) {
            isExist = true;
        } else {
            c = db.rawQuery("SELECT url FROM downloading WHERE url=?", new String[] {url});
            isExist = c.getCount() > 0;
        }
        c.close();
        return isExist;
    }

    /**
     * Delete the record of a downloading item.<br>
     * 删除正在下载的记录
     * @param url
     * @param filePath
     */
    public void remove(String url, String filePath) {
        db.execSQL("DELETE FROM downloading WHERE url=? AND filePath=?", new Object[] {url,
                filePath});
    }

    /**
     * Delete the record of a completed item.<br>
     * 删除已下载完成的记录
     * @param filePath
     */
    public void remove(String filePath) {
        db.execSQL("DELETE FROM completed WHERE filePath=?", new Object[] {filePath});
    }

    /**
     * Delete the record of a downloading item if it is finished.<br>
     * 下载完成后，删除正在下载的记录
     * @param url
     * @param filePath
     * @param len
     */
    public void tryRemove(String url, String filePath, int len) {
        Cursor c;
        c = db.rawQuery("SELECT SUM(done) FROM downloading WHERE url=? AND filePath=?",
                new String[] {url, filePath});
        if (c != null) {
            if (c.moveToNext()) {
                int result = c.getInt(0);
                if (result == len) {
                    remove(url, filePath);
                }
            }
            c.close();
        }
    }

    /**
     * Query the record of the threads of a downloading item.<br>
     * 查询未下载完成的记录
     * @return
     */
    public List<String[]> queryUndone() {
        //SELECT DISTINCT url,filePath,thid,done,appName,iconPath FROM downloading
        String sqlString = "SELECT DISTINCT url,filePath,appName,iconPath FROM downloading";
        Cursor c = db.rawQuery(sqlString, null);
        List<String[]> result = new ArrayList<String[]>();
        if (c != null) {
            while (c.moveToNext()) {
                String[] str = new String[4];
                str[0] = c.getString(0);
                str[1] = c.getString(1);
                str[2] = c.getString(2);
                str[3] = c.getString(3);
                result.add(str);
            }
            c.close();
        }
        return result;
    }

    /**
     * Query the record of a completed item.<br>
     * 查询已下载完成的记录
     * @return
     */
    public List<RecordInfo> queryDone() {
        Cursor c = db.rawQuery("SELECT DISTINCT url,filePath,fileLen,appName FROM completed", null);
        List<RecordInfo> infoList = new ArrayList<RecordInfo>();
        if (c != null) {
            while (c.moveToNext()) {
                infoList.add(new RecordInfo(c.getString(0), c.getString(1), c.getInt(2), c
                        .getString(3)));
            }
            c.close();
        }
        return infoList;
    }

    /**
     * 关闭数据库 Close the database.
     */
    public void close() {
        if (helper != null) {
            helper.close();
        }
        if (db != null) {
            db.close();
        }
    }
}
