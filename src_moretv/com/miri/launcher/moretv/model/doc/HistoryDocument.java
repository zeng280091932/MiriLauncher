package com.miri.launcher.moretv.model.doc;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.moretv.model.History;
import com.miri.launcher.moretv.model.Response;

/**
 * 播放历史文档
 * @author zengjiantao
 */
public class HistoryDocument extends Response implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 455737485812063370L;

    @SerializedName("data")
    private List<History> datas;

    public List<History> getDatas() {
        return datas;
    }

    public void setDatas(List<History> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "HistoryDocument [datas=" + datas + "]";
    }

}
