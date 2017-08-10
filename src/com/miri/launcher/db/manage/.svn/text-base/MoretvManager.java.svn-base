package com.miri.launcher.db.manage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.moretv.MoretvParser;
import com.miri.launcher.moretv.model.History;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.moretv.model.Metadata;
import com.miri.launcher.moretv.model.Platform;
import com.miri.launcher.moretv.model.doc.HistoryDocument;
import com.miri.launcher.moretv.model.doc.PlatformDocument;
import com.miri.launcher.moretv.model.doc.RecommendDocument;
import com.miri.launcher.moretv.model.doc.SearchResultDocument;
import com.miri.launcher.utils.NetworkUtil;
import com.miri.launcher.utils.Toolkit;

public class MoretvManager {

    /**
     * 搜索电视猫内容
     * 
     * @param key
     * @return
     */
    public static List<MediaInfo> search(String key) {
        try {
            SearchResultDocument result = MoretvParser.search(key);
            List<MediaInfo> items = result.getItems();
            if (!Toolkit.isEmpty(items)) {
                return items;
            } else {
                List<MediaInfo> mediaInfos = new ArrayList<MediaInfo>();
                List<MediaInfo> hot = result.getHot();
                if (!Toolkit.isEmpty(hot)) {
                    mediaInfos.addAll(hot);
                }
                List<MediaInfo> mv = result.getMv();
                if (!Toolkit.isEmpty(mv)) {
                    mediaInfos.addAll(mv);
                }
                List<MediaInfo> zongyi = result.getZongyi();
                if (!Toolkit.isEmpty(zongyi)) {
                    mediaInfos.addAll(zongyi);
                }
                List<MediaInfo> comickids = result.getComickids();
                if (!Toolkit.isEmpty(comickids)) {
                    mediaInfos.addAll(comickids);
                }
                List<MediaInfo> movietv = result.getMovietv();
                if (!Toolkit.isEmpty(movietv)) {
                    mediaInfos.addAll(movietv);
                }
                return mediaInfos;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "search error!");
            return null;
        }
    }

    /**
     * 分类搜索电视猫影片
     * 
     * @param key
     * @return
     */
    public static SearchResultDocument searchDocument(String key) {
        try {
            SearchResultDocument result = MoretvParser.search(key);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "search error!");
            return null;
        }
    }

    /**
     * 获取观看历史
     * 
     * @param ctx
     * @return
     */
    public static List<MediaInfo> fetchHistory(Context ctx) {
        try {
            HistoryDocument document = MoretvParser.parseHistory();
            int status = document.getStatus();
            if (status == MoretvConstants.HISTORY_SUCCESS) {
                List<History> histories = document.getDatas();
                List<MediaInfo> mediaInfos = new ArrayList<MediaInfo>();
                if (histories != null) {
                    MediaInfo mediaInfo = null;
                    for (History history : histories) {
                        mediaInfo = new MediaInfo();
                        mediaInfo.setLinkData(history.getLinkData());
                        mediaInfo.setTitle(history.getTitle());
                        mediaInfo.setContentType(history.getContentType());
                        mediaInfo.setEpisode(history.getEpisode());
                        mediaInfo.setScore(history.getScore());
                        mediaInfo.setIcon1(history.getIcon1());
                        mediaInfo.setPlayOver(history.isPlayOver());
                        mediaInfo.setSecond(history.getSecond());
                        mediaInfo.setTotalSecond(history.getTotalSecond());
                        Metadata metadata = history.getMetadata();
                        mediaInfo.setIsHd(metadata.getIsHd());
                        mediaInfo.setYear(metadata.getYear());
                        mediaInfo.setArea(metadata.getArea());
                        mediaInfo.setDuration(metadata.getDuration());
                        mediaInfo.setVideoType(metadata.getVideoType());
                        mediaInfo.setEpisodeCount(metadata.getEpisodeCount());
                        mediaInfo.setImage1(metadata.getImage1());
                        mediaInfo.setImage2(metadata.getImage2());
                        mediaInfo.setImage3(metadata.getImage3());
                        mediaInfos.add(mediaInfo);
                    }
                }
                return mediaInfos;
            } else if (status == MoretvConstants.TOKEN_OUT_OF_DATE) {
                MoretvParser.login(NetworkUtil.getMac(ctx));
                return fetchHistory(ctx);
            } else {
                Log.d("Debug", "status:" + status);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "fetchHistor error!");
            return null;
        }
    }

    /**
     * 获取分类视频列表
     * 
     * @param ctx
     * @param page
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<MediaInfo> fetchPlatform(Context ctx, String page) {
        try {
            PlatformDocument document = MoretvParser.parsePlatform(page);
            int status = document.getStatus();
            if (status == MoretvConstants.RESPONSE_SUCCESS) {
                Platform platform = document.getPosition();
                if (platform != null) {
                    return platform.getPostionItems();
                }
                return Collections.EMPTY_LIST;
            } else if (status == MoretvConstants.TOKEN_OUT_OF_DATE) {
                MoretvParser.login(NetworkUtil.getMac(ctx));
                return fetchPlatform(ctx, page);
            } else {
                Log.d("Debug", "status:" + status);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "fetch History error!");
            return null;
        }
    }

    /**
     * 获取首页推荐
     * 
     * @return
     */
    public static List<MediaInfo> fetchRecommend() {
        try {
            RecommendDocument document = MoretvParser.parseRecommend();
            if (document != null) {
                return document.getMediaInfos();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "fetch recommend error!");
            return null;
        }

    }

    /**
     * 获取推荐文档
     * 
     * @return
     */
    public static RecommendDocument fetchRecommendDocument() {
        try {
            RecommendDocument document = MoretvParser.parseRecommend();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug", "fetch recommend error!");
            return null;
        }
    }

    /**
     * 获取默认的推荐列表
     * 
     * @return
     */
    public static List<MediaInfo> fetchDefaultRecommend(Context context) {
        RecommendDocument document = MoretvData.sRecommendDocument;
        if (document == null) {
            try {
                document = MoretvParser.parseDefaultRecommend(context);
            } catch (JsonParserException e) {
                e.printStackTrace();
                return null;
            }
        }
        return document.getMediaInfos();
    }

    /**
     * 获取默认推荐的版本
     * 
     * @return
     */
    public static String fetchDefaultVersion(Context context) {
        RecommendDocument document = MoretvData.sRecommendDocument;
        if (document == null) {
            try {
                document = MoretvParser.parseDefaultRecommend(context);
            } catch (JsonParserException e) {
                e.printStackTrace();
                return null;
            }
        }
        return document.getVersion();
    }
}
