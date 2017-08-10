/* 
 * 文件名：MainRecommend.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model.doc;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.msg.model.AppNode;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class AppRecommend extends BaseDocument {

    private static final long serialVersionUID = 8631454003169486089L;

    @SerializedName("LocalFile")
    List<AppNode> localFileRecommends;

    @SerializedName("Broswer")
    List<AppNode> broswerRecommends;

    @SerializedName("Market")
    List<AppNode> marketRecommends;

    @SerializedName("Live")
    List<AppNode> liveRecommends;

    @SerializedName("MainOne")
    List<AppNode> appRecommends;

    public List<AppNode> getAppRecommends() {
        return appRecommends;
    }

    public void setAppRecommends(List<AppNode> appRecommends) {
        this.appRecommends = appRecommends;
    }

    public List<AppNode> getMarketRecommends() {
        return marketRecommends;
    }

    public void setMarketRecommends(List<AppNode> marketRecommends) {
        this.marketRecommends = marketRecommends;
    }

    public List<AppNode> getLocalFileRecommends() {
        return localFileRecommends;
    }

    public void setLocalFileRecommends(List<AppNode> localFileRecommends) {
        this.localFileRecommends = localFileRecommends;
    }

    public List<AppNode> getBroswerRecommends() {
        return broswerRecommends;
    }

    public void setBroswerRecommends(List<AppNode> broswerRecommends) {
        this.broswerRecommends = broswerRecommends;
    }

    public List<AppNode> getLiveRecommends() {
        return liveRecommends;
    }

    public void setLiveRecommends(List<AppNode> liveRecommends) {
        this.liveRecommends = liveRecommends;
    }

    @Override
    public String toString() {
        return "AppRecommend [localFileRecommends=" + localFileRecommends + ", broswerRecommends="
                + broswerRecommends + ", marketRecommends=" + marketRecommends
                + ", liveRecommends=" + liveRecommends + ", appRecommends=" + appRecommends + "]";
    }

}
