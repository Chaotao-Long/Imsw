package com.im.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.socket.WebSocketMessage;

public class ServerMessage {
//interface WebSocketMessage{

	private String content;

	private String time;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
//		return "消息: " + content + ", 时间： " + time;
		return "time:" + time + "\n" + "msg:" + content+"\n\n";
	}

}
