/* 
 * 文件名：AdvertList.java
 * 版权：Copyright
 */
package com.miri.launcher.msg.model.doc;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.msg.model.Operate;

/**
 * @author penglin
 * @version TVLAUNCHER001, 2013-6-15
 */
public class AdvertList extends BaseDocument {

    private static final long serialVersionUID = -2246230016383907068L;

    @SerializedName("AdvertOne")
    List<Operate> advertOne;

    @SerializedName("AdvertTwo")
    List<Operate> advertTwo;

    @SerializedName("AdvertThree")
    List<Operate> advertThree;

    public List<Operate> getAdvertOne() {
        return advertOne;
    }

    public void setAdvertOne(List<Operate> advertOne) {
        this.advertOne = advertOne;
    }

    public List<Operate> getAdvertTwo() {
        return advertTwo;
    }

    public void setAdvertTwo(List<Operate> advertTwo) {
        this.advertTwo = advertTwo;
    }

    public List<Operate> getAdvertThree() {
        return advertThree;
    }

    public void setAdvertThree(List<Operate> advertThree) {
        this.advertThree = advertThree;
    }

    @Override
    public String toString() {
        return "AdvertDocument [advertOne=" + advertOne + ", advertTwo=" + advertTwo
                + ", advertThree=" + advertThree + ", version=" + version + ", publicDate="
                + publicDate + ", lastBuildDate=" + lastBuildDate + "]";
    }

}
