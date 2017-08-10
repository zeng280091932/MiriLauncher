package com.miri.launcher.moretv.model.doc;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.miri.launcher.moretv.model.Platform;
import com.miri.launcher.moretv.model.Response;

/**
 * 平台文档对象
 * 
 * @author zengjiantao
 * 
 */
public class PlatformDocument extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 342431561332397902L;

	@SerializedName("position")
	private Platform position;

	public Platform getPosition() {
		return position;
	}

	public void setPosition(Platform position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "PlatformDocument [position=" + position + "]";
	}

}
