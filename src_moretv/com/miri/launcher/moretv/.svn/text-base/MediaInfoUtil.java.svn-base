/**
 * 
 */
package com.miri.launcher.moretv;

import android.text.Html;
import android.text.Spanned;

import com.miri.launcher.MoretvConstants;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.utils.Toolkit;

/**
 * 媒体信息详情工具类
 * 
 * @author zengjiantao
 * 
 */
public class MediaInfoUtil {

    private static final String COLOR_STRING = "<font color='#ff3600'>%1$s</font>";

    private static final String BOLD_STRING = "<b>%1$s</b>";

    /**
     * 使用明星信息更详情视图
     * 
     * @param mediaInfo
     * @return
     */
    public static Spanned createDesc(MediaInfo mediaInfo) {
        return createDesc(mediaInfo, false);
    }

    /**
     * 使用明星信息更详情视图
     * 
     * @param mediaInfo
     * @param isHistory
     * @return
     */
    public static Spanned createDesc(MediaInfo mediaInfo, boolean isHistory) {
        String score = mediaInfo.getScore();
        String contentType = mediaInfo.getContentType();
        String episode = mediaInfo.getEpisode();
        String episodeCount = mediaInfo.getEpisodeCount();
        boolean isPlayOver = mediaInfo.isPlayOver();
        int second = mediaInfo.getSecond();
        int totalSection = mediaInfo.getTotalSecond();
        if (MoretvConstants.PAGE_MOVIE.equals(contentType)) {
            if (isHistory && !isPlayOver) {
                int overplus = (totalSection - second) / 60;
                if (overplus > 0) {
                    String overStr = String.format(BOLD_STRING, overplus);
                    overStr = String.format(COLOR_STRING, overStr);
                    return Html.fromHtml("剩余" + overStr + "分钟");
                }else{
                    return Html.fromHtml("观看完毕");
                }
            }else{
                score = String.format(BOLD_STRING, score);
                score = String.format(COLOR_STRING, score);
                return Html.fromHtml(score + "分");
            }
        } else if (MoretvConstants.PAGE_TV.equals(contentType)
                || MoretvConstants.PAGE_COMIC.equals(contentType)
                || MoretvConstants.PAGE_JILU.equals(contentType)
                || MoretvConstants.PAGE_KIDS.equals(contentType)) {
            if (!Toolkit.isEmpty(episode) && episode.equals(episodeCount)) {
                episode = String.format(BOLD_STRING, episode);
                episode = String.format(COLOR_STRING, episode);
                if (isHistory) {
                    return Html.fromHtml("观看至" + episode + "集");
                } else {
                    return Html.fromHtml(episode + "集全");
                }
            } else {
                episode = String.format(BOLD_STRING, episode);
                episode = String.format(COLOR_STRING, episode);
                if (isHistory) {
                    return Html.fromHtml("观看至" + episode + "集");
                } else {
                    return Html.fromHtml("更新至" + episode + "集");
                }
            }
        } else if (MoretvConstants.PAGE_ZONGYI.equals(contentType)) {
            if (!Toolkit.isEmpty(episode) && episode.length() > 4) {
                episode = episode.substring(4);
            }
            episode = String.format(BOLD_STRING, episode);
            episode = String.format(COLOR_STRING, episode);
            if (isHistory) {
                return Html.fromHtml("观看至" + episode + "期");
            } else {
                return Html.fromHtml("更新至" + episode + "期");

            }
        }
        return Html.fromHtml("");
    }
}
