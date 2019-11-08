package com.im.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * DTO类，用来存放聊天的消息
 * 
 * @author BoBo
 *
 */
public class Message {

	// 发送者名称
	@Expose
	public String fromName;
	// 接收者
	@Expose
	public String toName; // 接收者名称,无则为群聊,有则为单聊
	// 接收群组
	@Expose
	public String toGroupId;
	// 发送的文本
	@Expose
	public String text;
	// 发送日期
	@Expose
	public Date date;

	// 在线用户列表
	@Expose
	List<String> usernamelist = new ArrayList<>(); // 存储在线聊天的用户名单列表

	public List<String> getUsernamelist() {
		return usernamelist;
	}

	public void setUsernamelist(List<String> usernamelist) {
		this.usernamelist = usernamelist;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToGroupId() {
		return toGroupId;
	}

	public void setToGroupId(String toGroupId) {
		this.toGroupId = toGroupId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
