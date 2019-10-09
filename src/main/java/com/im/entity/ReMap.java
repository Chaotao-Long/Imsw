package com.im.entity;

public class ReMap {
	private String status;
	private String msg;
	
	public ReMap() {
		status = "200";
		msg = "request successfully!";
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

}
