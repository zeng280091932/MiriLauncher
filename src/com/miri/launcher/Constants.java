package com.miri.launcher;

/**
 * 常量定义
 */
public interface Constants {

    /** 本地assets默认推荐应用文件名称 */
    public final static String DEFAULT_RECOMMEND_FILENAME = "miri_recommend.js";

    /** 本地assets默认广告文件名称 */
    public final static String DEFAULT_ADVERT_FILENAME = "miri_advert.js";

    /***********************************************/
    /** 终端类型 */
    /***********************************************/
    /** 通用 */
    public static final int GENERAL = 0;

    /** 空格数码 */
    public static final int KONGGE = 1;

    /***********************************************/
    /** wifi状态常量 */
    /***********************************************/
    public static final int WIFI_STATE_DISCONNECT = 10001;

    public static final int WIFI_STATE_CONNECTED = 10002;

    public static final int WIFI_RSSI_CHANGED = 10003;

    public static final int WIFI_STATE_UNKNOWN = 10004;

    public static final int WIFI_AP_CONNECTED = 10005;

    public static final int WIFI_AP_ERROR = 10006;

    public static final int WIFI_AP_CHECK = 10007;

    // 广域网错误
    public static final int WAN_ERROR = 10008;

    public static final int WIFI_AP_NEEDLOGIN = 10009;

    /***********************************************/
    /** 有线网络状态常量 */
    /***********************************************/

    public static final int WIRED_STATE_INFO = 20000;

    public static final int WIRED_STATE_DISCONNECT = 20001;

    public static final int WIRED_STATE_CONNECTED = 20002;

    public static final int WIRED_STATE_UNKNOWN = 20003;

    /***********************************************/
    /** 搜索常量 */
    /***********************************************/

    public static final String KEY_SEARCH_KEY = "search_key";

    public static final String KEY_MOVIE_LIST_TYPE = "movie_list_type";

    public static final int MOVIE_LIST_TYPE_SEARCH = 0;

    public static final int MOVIE_LIST_TYPE_HISTORY = 1;

    public static final int MOVIE_LIST_TYPE_MOVIE = 2;

    public static final int MOVIE_LIST_TYPE_TV = 3;

    public static final int MOVIE_LIST_TYPE_CARTOON = 4;

    public static final int MOVIE_LIST_TYPE_VARIETY = 5;

    /***********************************************/
    /** 照片、视频常量 */
    /***********************************************/

    public static final String KEY_FOLDER = "folder";

    public static final String KEY_PHOTO_POSITION = "photo_position";

    public static final String KEY_VIDEO_POSITION = "video_position";

    /***********************************************/
    /** 视频显示模式 */
    /***********************************************/

    public static final int SHOW_MODE_VERTICAL = 0;

    public static final int SHOW_MODE_HORIZONTAL = 1;
}
