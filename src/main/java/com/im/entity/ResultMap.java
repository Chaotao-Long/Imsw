package com.im.entity;

import java.util.List;

public class ResultMap {

	private String status;
	private String msg;
	private UserInfo userInfo;

	public ResultMap() {
		status = "200";
		msg = "request successfully!";
		userInfo = null;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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
