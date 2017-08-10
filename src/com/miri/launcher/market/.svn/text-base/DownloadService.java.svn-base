package com.miri.launcher.market;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.miri.launcher.R;
import com.miri.launcher.activity.DownloadManagerActivity;
import com.miri.launcher.imageCache.utils.ImageGetFromHttp;
import com.miri.launcher.utils.Logger;
import com.miri.launcher.view.CustomToast;
import com.miri.launcher.view.CustomToast.MsgType;
import com.miri.launcher.view.dialog.AlertDialog;
import com.miri.launcher.view.dialog.AlertUtil;

final public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    private static final String DOWNLOADDIR = "downloads";

    private Dao dao;

    private MyBinder mBinder = new MyBinder();

    // The handler of the activity that need to receive messages from this
    // service.
    private Handler uiHandler;

    // A list of downloading items and completed items.
    private final List<Map<String, Object>> itemsInfo = new ArrayList<Map<String, Object>>(30);

    public static final int DOWNLOAD_INIT = 0;

    public static final int DOWNLOAD_PROGRESS = 1;

    public static final int DOWNLOAD_FETCH_ICON = 2;

    public static final int DOWNLOAD_COMPLETE = 3;

    public static final int DOWNLOAD_PAUSE = 4;

    public static final int DOWNLOAD_ERROR = 5;

    public static final int DOWNLOAD_RETRY = 6;

    public static final int DOWNLOAD_DELETE = 7;

    public static final int TYPE_DOWNLOADING = 0;

    public static final int TYPE_COMPLETED = 1;

    private static final int threadCount = 3;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            final String urlStr = msg.getData().getString("urlStr");
            if (!URLUtil.isValidUrl(urlStr)) {
                return;
            }
            Map<String, Object> dataSet = getItem(urlStr);
            if (dataSet == null || dataSet.size() == 0) {
                return;
            }
            switch (msg.what) {
                case DOWNLOAD_INIT: {
                    // When download threads start, tell UI to update itself, then
                    // show a toast message to user.
                    final int fileLen = msg.getData().getInt("fileLen");
                    final String fileSizeText = getFileLen(fileLen);
                    dataSet.put("fileName", msg.getData().getString("fileName"));
                    dataSet.put("filePath", msg.getData().getString("filePath"));
                    dataSet.put("fileLen", fileLen);
                    dataSet.put("fileSizeText", getFileLen(0) + "/" + fileSizeText);
                    dataSet.put("statusText", "0%");

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_INIT;
                        uiMsg.getData().putString("statusText", "0%");
                        uiMsg.getData().putString("fileSizeText",
                                getFileLen(0) + "/" + fileSizeText);
                        uiHandler.sendMessage(uiMsg);
                    }

                    if (msg.getData().getBoolean("isNew")) {
                        //                        Toast.makeText(getApplicationContext(),
                        //                                getString(R.string.dl_start_apk, (String) dataSet.get("appName")),
                        //                                Toast.LENGTH_SHORT).show();
                        //                        CustomToast.makeText(getApplicationContext(),
                        //                                getString(R.string.dl_start_apk, (String) dataSet.get("appName")),
                        //                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case DOWNLOAD_PROGRESS: {
                    // Update download progress.
                    final int fileLen = (Integer) dataSet.get("fileLen");
                    final int done = msg.getData().getInt("done");
                    final int progress;
                    if (fileLen != 0) {
                        progress = (int) ((float) done / fileLen * 100);
                    } else {
                        progress = 0;
                    }
                    dataSet.put("done", done);
                    dataSet.put("progress", progress);
                    dataSet.put("statusText", progress + "%");
                    dataSet.put("fileSizeText", getFileLen(done) + "/" + getFileLen(fileLen));

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_PROGRESS;
                        uiMsg.getData().putInt("progress", progress);
                        uiMsg.getData().putString("statusText", progress + "%");
                        uiMsg.getData().putString("fileSizeText",
                                getFileLen(done) + "/" + getFileLen(fileLen));
                        uiHandler.sendMessage(uiMsg);
                    }
                    break;
                }
                case DOWNLOAD_COMPLETE: {
                    // When a download completes, tell UI to update itself, then
                    // show a toast message to user. And the installer will start
                    // automatically if the download manager activity is at the top
                    // of the task stack.
                    Integer type = (Integer) dataSet.get("type");
                    if (type == null || type == TYPE_COMPLETED) {
                        return;
                    }
                    // final String fileName = dataSet.get("fileName").toString();
                    final String filePath = (String) dataSet.get("filePath");
                    dataSet.put("type", TYPE_COMPLETED);
                    dataSet.put("progress", 100);
                    dataSet.put("statusText", "100%");
                    final int fileLen = (Integer) dataSet.get("fileLen");
                    dataSet.put("fileSizeText", getFileLen(fileLen));
                    dataSet.put("icon", getApkIcon(getApplicationContext(), filePath));

                    RecordInfo apkInfo = new RecordInfo((String) dataSet.get("urlStr"), filePath,
                            ((Integer) dataSet.get("fileLen")), (String) dataSet.get("appName"));
                    dao.insertDone(apkInfo);

                    if (filePath.startsWith("/data/data/")) {
                        try {
                            int status = Runtime.getRuntime().exec("chmod 604 " + filePath)
                                    .waitFor();
                            if (status != 0) {
                                // chmod failed
                                return;
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            return;
                        }
                    }

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_COMPLETE;
                        uiMsg.getData().putString("filePath", filePath);
                        uiMsg.getData().putInt("progress", 100);
                        uiMsg.getData().putString("statusText", "100%");
                        uiMsg.getData().putString("fileSizeText", getFileLen(fileLen));
                        uiHandler.sendMessage(uiMsg);
                    }
                    install(filePath);
                    //                    Toast.makeText(getApplicationContext(),
                    //                            getString(R.string.dl_finish, (String) dataSet.get("appName")),
                    //                            Toast.LENGTH_SHORT).show();
                    CustomToast.makeText(getApplicationContext(),
                            getString(R.string.dl_finish, (String) dataSet.get("appName")),
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case DOWNLOAD_PAUSE: {
                    // Switch the threads of a download item between running and
                    // sleeping.
                    final boolean isPause = msg.getData().getBoolean("isPause");
                    if (isPause) {
                        ((Downloader) dataSet.get("downloader")).pause();
                    } else {
                        ((Downloader) dataSet.get("downloader")).resume();
                    }
                    dataSet.put("isPause", isPause);
                    break;
                }
                case DOWNLOAD_DELETE: {
                    Logger.getLogger().d("DOWNLOAD_DELETE");
                    // When a download item is deleted, remove the related records
                    // from the database, and tell UI to update itself.
                    final String filePath = (String) dataSet.get("filePath");
                    if (((Integer) dataSet.get("type")).intValue() == TYPE_DOWNLOADING) {
                        ((Downloader) dataSet.get("downloader")).stop();
                        dao.remove((String) dataSet.get("urlStr"), (String) dataSet.get("filePath"));
                    } else {
                        dao.remove(filePath);
                    }
                    if (filePath != null) {
                        new File(filePath).delete();
                    }
                    itemsInfo.remove(dataSet);

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_DELETE;
                        uiHandler.sendMessage(uiMsg);
                    }
                    break;
                }
                case DOWNLOAD_ERROR: {
                    // If an error occurs before download thread start, tell UI to
                    // show the Retry button, and show a toast message to user.
                    String errorStr = msg.getData().getString("errorMsg");
                    Log.e(TAG, "errorMsg:" + errorStr);
                    dataSet.put("isError", true);
                    dataSet.put("statusText", errorStr);
                    ((Downloader) dataSet.get("downloader")).isStarted = false;

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_ERROR;
                        uiMsg.getData().putString("statusText", errorStr);
                        uiHandler.sendMessage(uiMsg);
                    }
                    String str = getString(R.string.dl_error, dataSet.get("appName"), errorStr);
                    //                    Toast.makeText(getApplicationContext(), errorStr, Toast.LENGTH_SHORT).show();
                    CustomToast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                    break;
                }
                case DOWNLOAD_RETRY: {
                    // When Retry button is clicked, try to restart the
                    // corresponding downloader.
                    dataSet.put("isError", false);
                    startDownload((Downloader) dataSet.get("downloader"));

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.getData().putString("urlStr", urlStr);
                        uiMsg.what = DOWNLOAD_RETRY;
                        dataSet.put("statusText", getString(R.string.retrying));
                        uiHandler.sendMessage(uiMsg);
                    }
                    break;
                }
            }
        }
    };

    public class MyBinder extends Binder {

        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "downloadService onCreate");
        init();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "downloadService onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "downloadService onUnbind");
        return super.onUnbind(intent);
    }

    /**
     * Set the handler so that this service can send message to it.
     */
    public void setUIHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public List<Map<String, Object>> getItemsInfo() {
        return itemsInfo;
    }

    //已完成
    public List<Map<String, Object>> getDoneItemsInfo() {
        List<Map<String, Object>> doneItemsInfo = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataSet: itemsInfo) {
            if (((Integer) dataSet.get("type")).intValue() == TYPE_COMPLETED) {
                doneItemsInfo.add(dataSet);
            }
        }
        return doneItemsInfo;
    }

    //未完成
    public List<Map<String, Object>> getUnDoneItemsInfo() {
        List<Map<String, Object>> doneItemsInfo = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> dataSet: itemsInfo) {
            if (((Integer) dataSet.get("type")).intValue() == TYPE_DOWNLOADING) {
                doneItemsInfo.add(dataSet);
            }
        }
        return doneItemsInfo;
    }

    public boolean isExist(String urlStr) {
        for (Map<String, Object> dataSet: itemsInfo) {
            if (urlStr.equals(dataSet.get("urlStr"))) {
                return true;
            }
        }
        return false;
    }

    public boolean isDownFinish(String urlStr) {
        List<Map<String, Object>> doneItemsInfo = getDoneItemsInfo();
        for (Map<String, Object> dataSet: doneItemsInfo) {
            if (urlStr != null && urlStr.equals(dataSet.get("urlStr"))) {
                return true;
            }
        }
        return false;
    }

    public int getItemPosition(String urlStr) {
        for (Map<String, Object> dataSet: itemsInfo) {
            if (urlStr.equals(dataSet.get("urlStr"))) {
                return itemsInfo.indexOf(dataSet);
            }
        }
        return -1;
    }

    public Map<String, Object> getItem(String urlStr) {
        for (Map<String, Object> dataSet: itemsInfo) {
            if (urlStr.equals(dataSet.get("urlStr"))) {
                return dataSet;
            }
        }
        return null;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "downloadService onDestroy");
        super.onDestroy();
        if (dao != null) {
            dao.decReferenceCount();
        }
        // Stop all download threads when this service is about to be destroyed.
        for (Map<String, Object> dataSet: itemsInfo) {
            final Downloader downloader = (Downloader) dataSet.get("downloader");
            if (downloader != null) {
                downloader.stop();
            }
        }
    }

    private void init() {
        dao = new Dao(this);

        // Load completed items.
        List<RecordInfo> doneList = dao.queryDone();
        for (RecordInfo apkInfo: doneList) {
            addCompleted(apkInfo);
        }
        // Load downloading items.
        List<String[]> undoneList = dao.queryUndone();
        for (String[] str: undoneList) {
            Log.d(TAG, "undone url-->" + str[0]);
            createDownload(str[0], str[1], str[2], str[3], false, false);
        }
    }

    private void addCompleted(RecordInfo apkInfo) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("type", TYPE_COMPLETED);
        map.put("isError", false);
        map.put("urlStr", apkInfo.getUrl());
        map.put("filePath", apkInfo.getFilePath());
        map.put("appName", apkInfo.getAppName());
        File apkFile = new File(apkInfo.getFilePath());
        long realFileLen = apkFile != null ? apkFile.length() : -1;
        // Simply verify the local file by compare its size to that recorded in
        // the database.
        if (realFileLen == apkInfo.getFileLen()) {
            map.put("fileSizeText", getFileLen(realFileLen));
            map.put("progress", 100);
            map.put("statusText", "100%");
            map.put("icon", getApkIcon(getApplicationContext(), apkInfo.getFilePath()));
        } else {
            map.put("fileSizeText", "");
            map.put("progress", 0);
            map.put("statusText", getString(R.string.dl_file_error));
        }
        itemsInfo.add(map);
    }

    public String getFileLen(double realFileLen) {
        return (new BigDecimal(realFileLen / 1024 / 1024)).setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB";
    }

    /**
     * Get icon from an apk file.
     */
    private Drawable getApkIcon(Context context, String apkPath) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                ApplicationInfo appInfo = info.applicationInfo;
                // The following two lines are commented in
                // android.content.pm.PackageParser.java for unknown reason, which
                // cause a bug that the ApplicationInfo.loadIcon() returns null.
                appInfo.sourceDir = apkPath;
                appInfo.publicSourceDir = apkPath;
                try {
                    return appInfo.loadIcon(pm);
                } catch (OutOfMemoryError e) {
                    Log.e(TAG, "getApkIcon: " + e.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "getApkIcon: " + e.toString());
        }
        return null;
    }

    /**
     * Get icon from an apk file.
     */
    @SuppressWarnings("unused")
    @Deprecated
    private Drawable getApkIconOld(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        Resources res = null;
        try {
            res = getApkResources(context, apkPath);
        } catch (Exception e) {
            return null;
        }
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            return res.getDrawable(appInfo.icon);
        }

        return null;
    }

    /**
     * Get resources of an apk file by using Java Reflection.
     */
    @Deprecated
    private Resources getApkResources(Context context, String apkPath)
            throws ClassNotFoundException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, SecurityException,
            NoSuchMethodException {
        final String PATH_AssetManager = "android.content.res.AssetManager";
        final Class<?> assetMagCls = Class.forName(PATH_AssetManager);
        final Constructor<?> assetMagCt = assetMagCls.getConstructor((Class[]) null);
        final Object assetMag = assetMagCt.newInstance((Object[]) null);
        Class<?>[] typeArgs = new Class[1];
        typeArgs[0] = String.class;
        Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
        Object[] valueArgs = new Object[1];
        valueArgs[0] = apkPath;
        assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
        Resources res = context.getResources();
        typeArgs = new Class[3];
        typeArgs[0] = assetMag.getClass();
        typeArgs[1] = res.getDisplayMetrics().getClass();
        typeArgs[2] = res.getConfiguration().getClass();
        Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
        valueArgs = new Object[3];
        valueArgs[0] = assetMag;
        valueArgs[1] = res.getDisplayMetrics();
        valueArgs[2] = res.getConfiguration();
        res = resCt.newInstance(valueArgs);
        return res;
    }

    /**
     * @param urlStr
     * @param appName
     * @param iconPath
     * @param file
     * @param isNew
     */
    public void createDownload(final String urlStr, String file, String appName, String iconPath,
            final boolean isNew, boolean isShow) {
        if (!URLUtil.isValidUrl(urlStr)) {
            //            Toast.makeText(getApplicationContext(), R.string.invalid_url, Toast.LENGTH_SHORT)
            //                    .show();
            CustomToast.makeText(getApplicationContext(), R.string.invalid_url, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        //判断是否已存在下载
        boolean isExist = isExist(urlStr);
        Log.d(TAG, "is exist:" + isExist);
        if (isExist) {
            if (isShow) {
                //            Toast.makeText(getApplicationContext(), R.string.dl_exist, Toast.LENGTH_SHORT).show();
                //                CustomToast
                //                        .makeText(getApplicationContext(), R.string.dl_exist, Toast.LENGTH_SHORT)
                //                        .show();
                startDownloadManager();
            }
        } else {
            if (isNew) {
                CustomToast.makeText(getApplicationContext(),
                        getString(R.string.dl_start_apk, appName == null ? "" : appName),
                        Toast.LENGTH_SHORT).show();
            }

            final Map<String, Object> map = new HashMap<String, Object>();
            // Get some information to string.
            map.put("appName", appName == null ? "" : appName);
            map.put("iconPath", iconPath == null ? "" : iconPath);

            //新任务，文件名从url中截取；已存在的任务，文件名为绝对路径
            if (file == null || file.length() == 0) {
                file = urlStr.substring(urlStr.lastIndexOf("/") + 1);
            }
            final String fileStr = file;
            String fileName;
            if (isNew) {
                fileName = fileStr;
            } else {
                fileName = (new File(fileStr)).getName();
            }

            map.put("type", TYPE_DOWNLOADING);
            map.put("isError", false);
            map.put("urlStr", urlStr);
            map.put("fileName", fileName);
            map.put("fileLen", 0);
            map.put("fileSizeText", getFileLen(0));
            map.put("done", 0);
            map.put("progress", 0);
            map.put("isPause", false);
            map.put("statusText", "0%");
            itemsInfo.add(map);

            final Downloader downloader = new Downloader(urlStr, fileStr, appName, iconPath, isNew,
                    threadCount, map);
            map.put("downloader", downloader);
            startDownload(downloader);
        }

    }

    //启动进入下载管理
    private void startDownloadManager() {
        final Context context = this;
        AlertUtil.showAlert(context, context.getString(R.string.system_tips),
                context.getString(R.string.dl_exist_enter_dlmanager),
                context.getString(R.string.ok), new AlertDialog.OnOkBtnClickListener() {

                    @Override
                    public void onClick() {
                        //                        Intent intent = new Intent(context, DownloadManagerActivity.class);
                        //                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //                        context.startActivity(intent);
                        try {
                            Intent callback = new Intent();
                            callback.setClass(context, DownloadManagerActivity.class);
                            callback.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                                    callback, PendingIntent.FLAG_CANCEL_CURRENT);
                            pendingIntent.send();
                        } catch (CanceledException e) {
                            e.printStackTrace();
                        }

                    }
                }, context.getString(R.string.cancel), null);
    }

    public void startDownload(final Downloader downloader) {
        if (downloader == null) {
            return;
        }
        int type = (Integer) downloader.dataSet.get("type");
        if (type == TYPE_COMPLETED) {
            return;
        }
        // Fetch icon.
        final String iconPath = (String) downloader.dataSet.get("iconPath");
        if (downloader.dataSet.get("icon") == null && iconPath != null) {
            new Thread() {

                @Override
                public void run() {
                    //                    final Bitmap bitMap = NetUtil.getBitMap(iconPath);
                    final Bitmap bitMap = ImageGetFromHttp.downloadBitmap(iconPath);
                    if (bitMap == null) {
                        Logger.getLogger().e("fetch app icon is empty!");
                        return;
                    }
                    downloader.dataSet.put("icon", bitMap);

                    if (uiHandler != null) {
                        Message uiMsg = new Message();
                        uiMsg.what = DOWNLOAD_FETCH_ICON;
                        uiMsg.getData().putString("urlStr",
                                (String) downloader.dataSet.get("urlStr"));
                        uiHandler.sendMessage(uiMsg);
                    }
                }

            }.start();
        }
        // Start download.
        downloader.isStarted = true;
        downloader.isPause = false;
        downloader.dataSet.put("isPause", false);
        new Thread() {

            @Override
            public void run() {
                //                super.run();
                try {
                    downloader.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    String errorMsg;
                    if (e instanceof SocketTimeoutException || e instanceof SocketException
                            || e instanceof IllegalArgumentException
                            || e instanceof UnknownHostException
                            || e instanceof MalformedURLException) {
                        errorMsg = getString(R.string.network_fail);
                    } else if (e instanceof IOException) {
                        //Android机子的文件夹下有存放文件的个数限制
                        if (e.getMessage().contains("ENOSPC")) {
                            errorMsg = "设备存储空间不足，请清理！";
                        } else {
                            errorMsg = getString(R.string.io_fail);
                        }
                    } else {
                        errorMsg = e.getMessage();
                    }
                    Log.e(TAG, "downloader start error!");
                    Message msg = new Message();
                    msg.what = DOWNLOAD_ERROR;
                    msg.getData().putString("urlStr", (String) downloader.dataSet.get("urlStr"));
                    msg.getData().putString("errorMsg", errorMsg);
                    handler.sendMessage(msg);
                }
            }

        }.start();
    }

    /**
     * Every downloading item has one downloader, which can start, pause, resume or stop its
     * download threads.
     */
    public class Downloader {

        String urlStr;

        String file;

        String appName;

        String iconPath;

        public boolean isNew;

        int threadCount;

        final private AtomicInteger done = new AtomicInteger(0);

        private int fileLen;

        public boolean isStarted = false;

        public boolean isPause = false;

        private boolean isStop = false;

        public Map<String, Object> dataSet;

        public Downloader(String urlStr, String file, String appName, String iconPath,
                boolean isNew, int threadCount, Map<String, Object> dataSet) {
            this.urlStr = urlStr;
            this.appName = appName;
            this.iconPath = iconPath;
            this.file = file;
            this.isNew = isNew;
            this.isPause = !isNew;
            this.threadCount = threadCount;
            this.dataSet = dataSet;
        }

        public void start() throws IOException {
            URL url = null;
            HttpURLConnection conn = null;
            try {
                url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();
                // Set the connecting timeout in milliseconds.
                conn.setConnectTimeout(NetUtil.CONNECT_TIMEOUT);
                //禁用连接池
                conn.setRequestProperty("http.keepAlive", "false");
                if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                    conn.setRequestProperty("Connection", "close");
                }
                conn.connect();
                int respCode = conn.getResponseCode();
                if (respCode == HttpURLConnection.HTTP_OK) {
                    fileLen = conn.getContentLength();
                    Log.v(TAG, "fileLen: " + java.lang.String.valueOf(fileLen));

                    File filePath;
                    //isNew为true,file为文件名;为false,file为文件绝对路径
                    if (isNew) {
                        filePath = generateFilePath(file, fileLen);
                    } else {
                        filePath = new File(file);
                        if (!filePath.exists() || filePath.length() != fileLen) {
                            dao.remove(urlStr, filePath.getPath());
                            filePath = generateFilePath(filePath.getName(), fileLen);
                        }
                    }

                    Message msg = new Message();
                    msg.what = DOWNLOAD_INIT;
                    msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                    msg.getData().putString("fileName", filePath.getName());
                    msg.getData().putString("filePath", filePath.getPath());
                    msg.getData().putInt("fileLen", fileLen);
                    msg.getData().putBoolean("isNew", isNew);
                    handler.sendMessage(msg);

                    // Calculate start position and length for every download
                    // thread and start all download threads.
                    int partLen = fileLen / threadCount;
                    for (int i = 0; i < threadCount - 1; i++) {
                        new DownloadThread(url, filePath, appName, iconPath, i * partLen, partLen,
                                i).start();
                    }
                    new DownloadThread(url, filePath, appName, iconPath, (threadCount - 1)
                            * partLen, partLen + fileLen % threadCount, threadCount - 1).start();
                } else {
                    throw new IllegalArgumentException("return http responseCode : " + respCode);
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }

        private File generateFilePath(String fileName, int fileLen) throws IOException {
            File dir;
            String sdDir = getSDPath();
            String downloadDir = DOWNLOADDIR;
            if (sdDir != null && sdDir.length() != 0) {
                //使用sd卡存储
                dir = new File(sdDir + File.separator + downloadDir + File.separator);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                fileName = generateFileName(dir, fileName);
            } else {
                // 使用内置存储
                dir = getDir(downloadDir, Context.MODE_PRIVATE | Context.MODE_WORLD_READABLE
                        | Context.MODE_WORLD_WRITEABLE);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                try {
                    int status = Runtime.getRuntime().exec("chmod 705 " + dir.getPath()).waitFor();
                    if (status != 0) {
                        // chmod failed
                        throw new IOException("chmod failed :" + dir.getPath());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new IOException("chmod failed: " + dir.getPath());
                }
                fileName = generateFileName(dir, fileName);
                //                openFileOutput(fileName, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE)
                //                        .write(0);
            }

            File filePath = new File(dir, fileName);
            RandomAccessFile raf = new RandomAccessFile(filePath, "rws");
            raf.setLength(fileLen);
            raf.close();
            Log.v(TAG, "filePath: " + filePath.getPath());
            return filePath;
        }

        /**
         * 取SD卡根路径
         */
        private String getSDPath() {
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
            if (sdCardExist) {
                File sdDir = Environment.getExternalStorageDirectory(); //获取根目录
                if (sdDir != null) {
                    return sdDir.getPath();
                }
            }
            return null;
        }

        /**
         * Rename file when a file with the same name already exists.
         */
        private String generateFileName(File dir, String fileName) {
            String prefix = "";
            int i = 1;
            while ((new File(dir, prefix + fileName)).exists()) {
                prefix = "(" + i++ + ")";
            }
            fileName = prefix + fileName;
            return fileName;
        }

        /**
         * 下载线程
         * @author penglin
         * @version TVLAUNCHER001, 2013-8-27
         */
        private final class DownloadThread extends Thread {

            final private AtomicInteger rateDone = new AtomicInteger(0);

            private URL url;

            private File file;

            String appName;

            String iconPath;

            //该线程下载的起始位置
            private int offset;

            //该线程要下载的文件长度
            private int partLen;

            //该线程已下载完成的字节数
            private int partBytesWtritten;

            //thread id
            private int id;

            private long lastRefreshTime = 0;

            private int restartDelay = 0;

            // The max restart delay in milliseconds when error occurs.
            private final int MAX_RESTART_DELAY = 8000;

            // The restart delay increment in milliseconds.
            private final int RESTART_DELAY_INCREMENT = 1000;

            public DownloadThread(URL url, File file, String appName, String iconPath, int offset,
                    int partLen, int id) {
                this.url = url;
                this.file = file;
                this.appName = appName;
                this.iconPath = iconPath;
                this.offset = offset;
                this.partLen = partLen;
                this.id = id;
            }

            @Override
            public void run() {
                super.run();

                if (!dao.incReferenceCount()) {
                    return;
                }

                // Check if it is a undone task.
                RecordInfo info = dao.query(url.toString(), file.getPath(), id);
                if (info != null) {
                    // If it is a undone task, resume it from the breakpoint.
                    partBytesWtritten = info.getDone();
                    done.addAndGet(partBytesWtritten);
                } else {
                    // If it is a new task, start it from the beginning.
                    info = new RecordInfo(url.toString(), file.getPath(), id, 0, appName, iconPath);
                    dao.insertUndone(info);
                }

                int fileLen = (int) file.length();
                // The start position.
                int start = offset + info.getDone();
                // The end position.
                int end = offset + partLen - 1;

                for (;;) {
                    try {
                        if (isStop) {
                            dao.decReferenceCount();
                            return;
                        }

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        // Set the connection timeout.
                        conn.setConnectTimeout(NetUtil.CONNECT_TIMEOUT);
                        // Set the reading timeout.
                        conn.setReadTimeout(NetUtil.READ_TIMEOUT);
                        // Set the Range header.
                        conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
                        RandomAccessFile raf = new RandomAccessFile(file, "rws");
                        raf.seek(start);
                        // This will trigger the connection.
                        InputStream in = conn.getInputStream();
                        // Reading buffer.
                        byte[] buf = new byte[1024 * 64];
                        int len = -1;
                        for (;;) {
                            if (isPause) {
                                synchronized (Downloader.this) {
                                    try {
                                        Downloader.this.wait();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (isStop) {
                                dao.decReferenceCount();
                                return;
                            }

                            // Read
                            len = in.read(buf);
                            if (len == -1) {
                                break;
                            }

                            // Reset the restart delay.
                            restartDelay = 0;
                            raf.write(buf, 0, len);
                            done.addAndGet(len);
                            partBytesWtritten += len;
                            info.setDone(partBytesWtritten);
                            dao.update(info);

                            long currentTime;
                            if (done.get() >= fileLen) {
                                // Delete the related record.
                                dao.tryRemove(info.getUrl(), file.getPath(), fileLen);
                                Message msg = new Message();
                                msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                                msg.what = DOWNLOAD_COMPLETE;
                                handler.sendMessage(msg);
                            } else if ((currentTime = System.currentTimeMillis()) - lastRefreshTime > 800) {
                                Message msg = new Message();
                                msg.what = DOWNLOAD_PROGRESS;
                                msg.getData().putString("urlStr", (String) dataSet.get("urlStr"));
                                msg.getData().putInt("done", done.get());
                                handler.sendMessage(msg);
                                lastRefreshTime = currentTime;
                            }
                        }
                        in.close();
                        raf.close();
                        // Delete the related record.
                        dao.tryRemove(info.getUrl(), file.getPath(), fileLen);
                        dao.decReferenceCount();
                    } catch (IOException e) {
                        // When error occurs, wait for a while and try to resume
                        // from the breakpoint.
                        e.printStackTrace();
                        try {
                            sleep(restartDelay);
                        } catch (InterruptedException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        start = offset + partBytesWtritten;
                        if (restartDelay < MAX_RESTART_DELAY) {
                            restartDelay += RESTART_DELAY_INCREMENT;
                        }
                        Log.e(TAG, "DownloadThread (" + id + ") restart");
                        continue;
                    }
                    break;
                }
            }
        }

        public void pause() {
            isPause = true;
        }

        public void resume() {
            isPause = false;
            synchronized (Downloader.this) {
                Downloader.this.notifyAll();
            }
        }

        public void stop() {
            isStop = true;
            if (isPause) {
                resume();
            }
        }
    }

    public void install(final String filePath) {
        //检查系统是否允许安装第三方应用
        if (isInstallingUnknownAppsAllowed()) {
            installApk(filePath);
        } else {
            CustomToast.makeText(getApplicationContext(), R.string.no_allow_install,
                    Toast.LENGTH_SHORT, MsgType.ERROR).show();
            //            Toast.makeText(getApplicationContext(), R.string.no_allow_install, Toast.LENGTH_SHORT)
            //                    .show();
        }
    }

    /**
     * 是否允许安装第三方应用
     * @return
     */
    public boolean isInstallingUnknownAppsAllowed() {
        return Settings.Secure.getInt(getContentResolver(),
                Settings.Secure.INSTALL_NON_MARKET_APPS, 0) > 0;
    }

    /**
     * 安装Apk
     * @param context
     * @param file
     */
    public void installApk(String filePath) {
        Intent installer = new Intent(Intent.ACTION_VIEW);
        installer.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installer.setDataAndType(Uri.parse("file://" + filePath),
                "application/vnd.android.package-archive");//Uri.fromFile(new File(filePath))
        try {
            startActivity(installer);
        } catch (Exception e) {
            e.printStackTrace();
            //            Toast.makeText(getApplicationContext(), R.string.install_fail, Toast.LENGTH_SHORT)
            //                    .show();
            CustomToast.makeText(getApplicationContext(), R.string.install_fail,
                    Toast.LENGTH_SHORT, MsgType.ERROR).show();
        }
    }

}
