package com.im.entity;

import java.util.List;

//群组成员信息
public class GroupMember {

	private List<UserInfo> userInfo;

	public List<UserInfo> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List<UserInfo> userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "GroupMember [userInfo=" + userInfo + "]";
	}

}
