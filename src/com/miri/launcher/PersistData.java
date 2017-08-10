/* 
 * 文件名：PersistData.java
 * 版权：Copyright
 */
package com.miri.launcher;

import com.miri.launcher.msg.model.doc.AdvertList;
import com.miri.launcher.msg.model.doc.AppRecommend;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-10-24
 */
public class PersistData {

    /** 版本类型 */
    public static int VERSION_TYPE = Constants.GENERAL;

    public final static String msg_url = "http://8337.s30.javaidc.com/tvui.do";

    /** 广告持久化数据AdvertList */
    public static AdvertList mAdvertList;

    /** 应用推荐持久化数据 */
    public static AppRecommend mAppRecommend;

    /**
     * 升级服务器地址
     * @return
     */
    public static String getUpgradeUrl() {
        String upgradeUrl = null;
        if (VERSION_TYPE == Constants.KONGGE) {
            upgradeUrl = "http://wln1658.gotoip3.com/upgrade/kg360/upgrade.js";
        } else {
            upgradeUrl = "http://wln1658.gotoip3.com/upgrade/miri_upgrade_launcher.js";
        }

        return upgradeUrl;
    }

    /**
     * 推荐应用服务器地址
     * @return
     */
    public static String getAppRecommendUrl() {
        String appRecommendUrl = null;
        if (VERSION_TYPE == Constants.KONGGE) {
            appRecommendUrl = "http://wln1658.gotoip3.com/epg/kg360/recommend.js";
        } else {
            appRecommendUrl = "http://wln1658.gotoip3.com/epg/miri_recommend.js";
        }

        return appRecommendUrl;
    }

    /**
     * 广告服务器地址
     * @return
     */
    public static String getAdvertUrl() {
        String advertUrl = null;
        if (VERSION_TYPE == Constants.KONGGE) {
            advertUrl = "http://wln1658.gotoip3.com/advert/kg360/advert.js";
        } else {
            advertUrl = "http://wln1658.gotoip3.com/advert/advert.js";
        }

        return advertUrl;
    }

    /** 本地缓存推荐应用文件名称 */
    public static String getCacheAppRecomName() {
        String name = null;
        if (VERSION_TYPE == Constants.KONGGE) {
            name = "cache_miri_kongge_recommend.js";
        } else {
            name = "cache_miri_recommend.js";
        }
        return name;
    }

    /** 本地缓存广告文件名称 */
    public static String getCacheAdvertName() {
        String name = null;
        if (VERSION_TYPE == Constants.KONGGE) {
            name = "cache_miri_kongge_advert.js";
        } else {
            name = "cache_miri_advert.js";
        }
        return name;
    }

}
