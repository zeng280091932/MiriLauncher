package com.miri.launcher.market;

import java.io.File;

final public class RecordInfo {

    private int type = -1;

    private String url;

    private String filePath;

    private String appName;

    //---------完成下载属性
    private int fileLen;

    //---------未完成下载属性
    private int threadId;

    private int done;

    private String iconPath;

    public RecordInfo(String url, String filePath, int thid, int done, String appName,
            String iconPath) {
        type = DownloadService.TYPE_DOWNLOADING;
        this.url = url;
        this.filePath = filePath;
        this.threadId = thid;
        this.done = done;
        this.appName = appName;
        this.iconPath = iconPath;
    }

    public RecordInfo(String url, String filePath, int fileLen, String appName) {
        type = DownloadService.TYPE_COMPLETED;
        this.url = url;
        this.filePath = filePath;
        this.fileLen = fileLen;
        this.appName = appName;
    }

    public int getType() {
        return type;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return (new File(filePath).getName());
    }

    public int getFileLen() {
        return fileLen;
    }

    public String getAppName() {
        return appName;
    }

    public int getDone() {
        return done;
    }

    public String getUrl() {
        return url;
    }

    public int getThreadId() {
        return threadId;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "RecordInfo [type=" + type + ", url=" + url + ", filePath=" + filePath
                + ", appName=" + appName + ", fileLen=" + fileLen + ", threadId=" + threadId
                + ", done=" + done + ", iconPath=" + iconPath + "]";
    }

}
