/*
 * 文件名：AppRecommend.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class AppNode implements Serializable {

    private static final long serialVersionUID = -6912171624602092440L;

    @SerializedName("name")
    String name;

    @SerializedName("poster")
    String poster;

    @SerializedName("package")
    String pkgName;

    @SerializedName("url")
    String url;

    boolean customIcon = false;

    String iconResource;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCustomIcon() {
        return customIcon;
    }

    public String getIconResource() {
        return iconResource;
    }

    @Override
    public String toString() {
        return "AppNode [name=" + name + ", poster=" + poster + ", pkgName=" + pkgName + ", url="
                + url + ", customIcon=" + customIcon + ", iconResource=" + iconResource + "]";
    }

}
