package com.miri.launcher.moretv.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * 播放历史
 * 
 * @author zengjiantao
 */
public class History extends Response implements Serializable {

    private static final long serialVersionUID = -940941888874389339L;

    @SerializedName("link_data")
    private String linkData;

    @SerializedName("title")
    private String title;

    @SerializedName("dateTime")
    private Date playTime;

    @SerializedName("playOver")
    private boolean playOver;

    @SerializedName("second")
    private int second;

    @SerializedName("totalSecond")
    private int totalSecond;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("episode")
    private String episode;

    @SerializedName("score")
    private String score;

    @SerializedName("icon1")
    private String icon1;

    @SerializedName("metadata")
    private Metadata metadata;

    public String getLinkData() {
        return linkData;
    }

    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Date getPlayTime() {
        return playTime;
    }

    public void setPlayTime(Date playTime) {
        this.playTime = playTime;
    }

    public boolean isPlayOver() {
        return playOver;
    }

    public void setPlayOver(boolean playOver) {
        this.playOver = playOver;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getTotalSecond() {
        return totalSecond;
    }

    public void setTotalSecond(int totalSecond) {
        this.totalSecond = totalSecond;
    }

    @Override
    public String toString() {
        return "History [linkData=" + linkData + ", title=" + title
                + ", playTime=" + playTime + ", playOver=" + playOver
                + ", second=" + second + ", totalSecond=" + totalSecond
                + ", contentType=" + contentType + ", episode=" + episode
                + ", score=" + score + ", icon1=" + icon1 + ", metadata="
                + metadata + "]";
    }

}
