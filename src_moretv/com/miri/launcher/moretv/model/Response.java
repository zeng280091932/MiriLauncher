package com.miri.launcher.moretv.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * 访问应答
 * 
 * @author zengjiantao
 * 
 */
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4050869254174691390L;

	/**
	 * 响应码
	 */
	@SerializedName("status")
	protected int status;

	@SerializedName("message")
	protected String message;

	public Response() {
	}

	public Response(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Response [status=" + status + ", message=" + message + "]";
	}

}
