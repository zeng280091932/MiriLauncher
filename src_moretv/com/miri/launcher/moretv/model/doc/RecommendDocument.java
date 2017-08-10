package com.miri.launcher.moretv.model.doc;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.moretv.model.MediaInfo;
import com.miri.launcher.moretv.model.Response;

public class RecommendDocument extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6023590967162036177L;

	@SerializedName("version")
	private String version;

	@SerializedName("count")
	private String count;

	@SerializedName("positionItems")
	private List<MediaInfo> mediaInfos;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public List<MediaInfo> getMediaInfos() {
		return mediaInfos;
	}

	public void setMediaInfos(List<MediaInfo> mediaInfos) {
		this.mediaInfos = mediaInfos;
	}

	@Override
	public String toString() {
		return "RecommendDocument [version=" + version + ", count=" + count
				+ ", mediaInfos=" + mediaInfos + "]";
	}
}
