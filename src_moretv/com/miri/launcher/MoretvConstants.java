package com.miri.launcher;

/**
 * MoreTv常量
 * @author zengjiantao
 */
public interface MoretvConstants {

    /** --------------------------------------- */
    /** ------------------请求地址--------------- */
    /** --------------------------------------- */

    public static final String LOGIN_URL = "http://openapi.moretv.com.cn/openApi/Service/logon";

    public static final String RECOMMEND_URL = "http://wln1658.gotoip3.com/main.js";

    public static final String SEARCH_URL = "http://openapi.moretv.com.cn/openApi/Service/keyword";

    public static final String HISTORY_URL = "http://openapi.moretv.com.cn/openApi/Service/getHistories";

    public static final String PLATFORM_URL = "http://openapi.moretv.com.cn/openApi/Service/Position";

    /** --------------------------------------- */
    /** ------------------响应状态码--------------- */
    /** --------------------------------------- */

    public static final int LOGIN_SUCCESS = 4200;

    public static final int RESPONSE_SUCCESS = 200;

    public static final int HISTORY_SUCCESS = 0;

    public static final int TOKEN_OUT_OF_DATE = -499;

    /** ------------------------------------------------------------- */
    /** ---分类:movie:电影、tv：电视剧、comic:动漫、zongyi:综艺、jilu：纪实------ */
    /** ------------------------------------------------------------- */

    public static final String PAGE_MOVIE = "movie";

    public static final String PAGE_TV = "tv";

    public static final String PAGE_COMIC = "comic";

    public static final String PAGE_ZONGYI = "zongyi";

    public static final String PAGE_JILU = "jilu";

    public static final String PAGE_KIDS = "kids";

    /** --------------------------------------- */
    /** ------------------linkdata--------------- */
    /** --------------------------------------- */

    public static final String LINKDATA_MOVIE = "page=list&contentType=movie&type=0";

    public static final String LINKDATA_TV = "page=list&contentType=tv&type=0";

    public static final String LINKDATA_COMIC = "page=list&contentType=comic&type=0";

    public static final String LINKDATA_ZONGYI = "page=list&contentType=zongyi&type=0";

    public static final String LINKDATA_JILU = "page=list&contentType=jilu&type=0";

    /** --------------------------------------- */
    /** ------------------电视猫信息--------------- */
    /** --------------------------------------- */
    public static final String APK_URL = "http://pic.moretv.com.cn/download/MoreTV_Gen_Latest.apk";

    public static final String APK_NAME = "MoreTv";

    public static final String PACKAGE_NAME = "com.moretv.tvapp";

    public static final String FILE_NAME = "moretv.apk";

    /** --------------------------------------- */
    /** ------------------小鹰信息--------------- */
    /** --------------------------------------- */
    //    public static final String LIVE_APK_URL = "http://live.eagleapp.tv/data/eaglelive_alpha_20130815.apk";
    //
    //    public static final String LIVE_APK_NAME = "小鹰直播";
    //
    //    public static final String LIVE_PACKAGE_NAME = "com.eagletv.live";
    //
    //    public static final String LIVE_FILE_NAME = "eaglelive.apk";

    /**
     * 保存首页推荐版本号的配置文件
     */
    public static final String PREFERENCES_RECOMMEND_VERSION = "recommend_version_pref";

    public static final String KEY_RECOMMEND_VERSION = "recommend_version";

    public static final String DEFAULT_RECOMMEND_FILE = "miri_home.js";

    /** --------------------------------------- */
    /** ------------------是否高清--------------- */
    /** --------------------------------------- */
    public static final String HD_YES = "1";

    public static final String HD_NO = "0";
}
