package com.im.entity;

public class ResultMap {

	private int status;
	private String msg;
	private UserInfo userInfo;

	public ResultMap() {
		status = 200;
		msg = "request successfully!";
		userInfo = null;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
