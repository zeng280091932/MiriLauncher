package com.miri.launcher.moretv.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 媒体信息
 * 
 * @author zengjiantao
 * 
 */
public class MediaInfo extends Response implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3784214622198607986L;

    /**
     * 是否是更多按钮 更多：true 普通：false
     */
    private boolean isMore;

    @SerializedName("link_data")
    private String linkData;

    @SerializedName("title")
    private String title;

    @SerializedName("item_title")
    private String itemTitle;

    @SerializedName("item_contentType")
    private String contentType;

    @SerializedName("item_isHd")
    private String isHd;

    @SerializedName("item_year")
    private String year;

    @SerializedName("item_area")
    private String area;

    // @SerializedName("item_tag")
    // private List<String> tag;

    @SerializedName("item_duration")
    private String duration;

    @SerializedName("item_episodeCount")
    private String episodeCount;

    @SerializedName("item_episode")
    private String episode;

    @SerializedName("item_score")
    private String score;

    // @SerializedName("item_cast")
    // private List<String> cast;
    //
    // @SerializedName("item_director")
    // private List<String> director;
    //
    // @SerializedName("item_host")
    // private List<String> host;
    //
    // @SerializedName("item_guest")
    // private List<String> guest;

    @SerializedName("item_videoType")
    private String videoType;

    @SerializedName("item_image1")
    private String image1;

    @SerializedName("item_image2")
    private String image2;

    @SerializedName("item_image3")
    private String image3;

    @SerializedName("item_icon1")
    private String icon1;

    @SerializedName("customIcon")
    private boolean customIcon = false;

    @SerializedName("iconResource")
    private String iconResource;

    @SerializedName("playOver")
    private boolean playOver;

    @SerializedName("second")
    private int second;

    @SerializedName("totalSecond")
    private int totalSecond;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean isMore) {
        this.isMore = isMore;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

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

    public String getIsHd() {
        return isHd;
    }

    public void setIsHd(String isHd) {
        this.isHd = isHd;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    // public List<String> getTag() {
    // return tag;
    // }
    //
    // public void setTag(List<String> tag) {
    // this.tag = tag;
    // }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(String episodeCount) {
        this.episodeCount = episodeCount;
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

    // public List<String> getCast() {
    // return cast;
    // }
    //
    // public void setCast(List<String> cast) {
    // this.cast = cast;
    // }
    //
    // public List<String> getDirector() {
    // return director;
    // }
    //
    // public void setDirector(List<String> director) {
    // this.director = director;
    // }
    //
    // public List<String> getHost() {
    // return host;
    // }
    //
    // public void setHost(List<String> host) {
    // this.host = host;
    // }
    //
    // public List<String> getGuest() {
    // return guest;
    // }
    //
    // public void setGuest(List<String> guest) {
    // this.guest = guest;
    // }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getIcon1() {
        return icon1;
    }

    public void setIcon1(String icon1) {
        this.icon1 = icon1;
    }

    public boolean isCustomIcon() {
        return customIcon;
    }

    public void setCustomIcon(boolean customIcon) {
        this.customIcon = customIcon;
    }

    public String getIconResource() {
        return iconResource;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
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
        return "MediaInfo [isMore=" + isMore + ", linkData=" + linkData
                + ", title=" + title + ", itemTitle=" + itemTitle
                + ", contentType=" + contentType + ", isHd=" + isHd + ", year="
                + year + ", area=" + area + ", duration=" + duration
                + ", episodeCount=" + episodeCount + ", episode=" + episode
                + ", score=" + score + ", videoType=" + videoType + ", image1="
                + image1 + ", image2=" + image2 + ", image3=" + image3
                + ", icon1=" + icon1 + ", customIcon=" + customIcon
                + ", iconResource=" + iconResource + ", playOver=" + playOver
                + ", second=" + second + ", totalSecond=" + totalSecond + "]";
    }

}
