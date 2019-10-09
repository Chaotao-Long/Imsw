package com.im.entity;

import java.util.List;

import com.im.dbmodel.Group;

public class ReturnMap {

	private String status;
	private String msg;
	private List<UserInfo> userinfo;
	private List<Group> group;
	
	public ReturnMap() {
		status = "200";
		msg = "request successfully!";
		userinfo = null;
		group =null;
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

	public List<UserInfo> getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(List<UserInfo> userinfo) {
		this.userinfo = userinfo;
	}

	public List<Group> getGroup() {
		return group;
	}

	public void setGroup(List<Group> group) {
		this.group = group;
	}
	
	
}
