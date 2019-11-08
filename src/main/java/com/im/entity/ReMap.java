package com.im.entity;

public class ReMap {
	private int status;
	private String msg;

	public ReMap() {
		status = 200;
		msg = "request successfully!";
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

}
