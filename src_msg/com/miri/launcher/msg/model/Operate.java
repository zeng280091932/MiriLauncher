/* 
 * 文件名：OperateInfo.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class Operate implements Serializable {

    private static final long serialVersionUID = 2063597477628007868L;

    String type;

    String poster;

    //描述信息
    String info;

    String value;

    @SerializedName("package")
    String pkgName;

    String param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "OperateInfo [type=" + type + ", poster=" + poster + ", info=" + info + ", value="
                + value + ", pkgName=" + pkgName + ", param=" + param + "]";
    }

}
