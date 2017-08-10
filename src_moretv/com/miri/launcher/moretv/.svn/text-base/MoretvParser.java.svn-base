package com.miri.launcher.moretv;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.MoretvData;
import com.miri.launcher.http.NetWorkInfoException;
import com.miri.launcher.json.JsonParser;
import com.miri.launcher.json.JsonParserException;
import com.miri.launcher.moretv.model.LoginInfo;
import com.miri.launcher.moretv.model.doc.HistoryDocument;
import com.miri.launcher.moretv.model.doc.PlatformDocument;
import com.miri.launcher.moretv.model.doc.RecommendDocument;
import com.miri.launcher.moretv.model.doc.SearchResultDocument;
import com.miri.launcher.msg.MsgParser;
import com.miri.launcher.utils.Toolkit;

/**
 * MoreTv结构解析器
 * @author zengjiantao
 */
public class MoretvParser {

    /**
     * 登录MoreTv服务器
     * @throws NetWorkInfoException
     * @throws JsonParserException
     */
    public static LoginInfo login(String mac) throws JsonParserException, NetWorkInfoException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mac", mac);
        String url = MsgParser.wrapGetParameter(MoretvConstants.LOGIN_URL, params);
        LoginInfo loginInfo = (LoginInfo) JsonParser.parse(url, LoginInfo.class);
        MoretvData.sLoginInfo = loginInfo;
        return loginInfo;
    }

    /**
     * 解析首页推荐影片
     * @return
     * @throws JsonParserException
     * @throws NetWorkInfoException
     */
    public static RecommendDocument parseRecommend() throws JsonParserException,
            NetWorkInfoException {
        return (RecommendDocument) JsonParser.parse(MoretvConstants.RECOMMEND_URL,
                RecommendDocument.class);
    }

    /**
     * 解析默认首页推荐内容
     * @param Context
     * @return
     * @throws JsonParserException
     */
    public static RecommendDocument parseDefaultRecommend(Context context)
            throws JsonParserException {
        if (MoretvData.sRecommendDocument == null) {
            String json = Toolkit.getFromAssets(context, MoretvConstants.DEFAULT_RECOMMEND_FILE);
            MoretvData.sRecommendDocument = (RecommendDocument) JsonParser.parseForString(json,
                    RecommendDocument.class, -1);
        }
        return MoretvData.sRecommendDocument;
    }

    /**
     * 搜索
     * @param key
     * @return
     * @throws JsonParserException
     * @throws NetWorkInfoException
     */
    public static SearchResultDocument search(String key) throws JsonParserException,
            NetWorkInfoException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", key);
        String url = MsgParser.wrapGetParameter(MoretvConstants.SEARCH_URL, params);
        return (SearchResultDocument) JsonParser.parse(url, SearchResultDocument.class);
    }

    /**
     * 获取历史记录
     * @return
     * @throws JsonParserException
     * @throws NetWorkInfoException
     */
    @SuppressWarnings("unchecked")
    public static HistoryDocument parseHistory() throws JsonParserException, NetWorkInfoException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userType", "tv");
        if (MoretvData.sLoginInfo != null) {
            params.put("uid", MoretvData.sLoginInfo.getUserId());
            params.put("token", MoretvData.sLoginInfo.getToken());
        }
        String url = MsgParser.wrapGetParameter(MoretvConstants.HISTORY_URL, params);
        HistoryDocument doc = (HistoryDocument) JsonParser.parse(url, HistoryDocument.class);
        /** 按播放时间排序 */
        //        if (doc != null) {
        //            List<History> datas = doc.getDatas();
        //            if (!Toolkit.isEmpty(datas)) {
        //                Collections.sort(datas, new Comparator() {
        //
        //                    @Override
        //                    public int compare(Object o1, Object o2) {
        //                        Date date1 = ((History) o1).getPlayTime();
        //                        Date date2 = ((History) o2).getPlayTime();
        //                        if (date1 == null && date2 != null) {
        //                            return 1;
        //                        }
        //                        if (date1 != null && date2 != null) {
        //                            return date2.compareTo(date1);
        //                        }
        //                        return 0;
        //                    }
        //                });
        //                doc.setDatas(datas);
        //            }
        //        }
        return doc;
    }

    /**
     * 获取分类视频
     * @param page
     * @return
     * @throws JsonParserException
     * @throws NetWorkInfoException
     */
    public static PlatformDocument parsePlatform(String page) throws JsonParserException,
            NetWorkInfoException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page);
        if (MoretvData.sLoginInfo != null) {
            params.put("token", MoretvData.sLoginInfo.getToken());
            params.put("userId", MoretvData.sLoginInfo.getUserId());
        }
        String url = MsgParser.wrapGetParameter(MoretvConstants.PLATFORM_URL, params);
        return (PlatformDocument) JsonParser.parse(url, PlatformDocument.class);
    }

}
