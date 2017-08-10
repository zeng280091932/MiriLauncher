package com.miri.launcher.moretv.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/**
 * MoreTv登录信息
 * 
 * @author zengjiantao
 * 
 */
public class LoginInfo extends Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -634193951539781649L;

	@SerializedName("userId")
	private String userId;

	@SerializedName("token")
	private String token;

	public LoginInfo() {
	}

	public LoginInfo(String userId, String token) {
		super();
		this.userId = userId;
		this.token = token;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "LoginInfo [status=" + status + ", userId=" + userId + ", token="
				+ token + "]";
	}
}
