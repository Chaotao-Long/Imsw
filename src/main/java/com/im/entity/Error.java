package com.im.entity;

/**
 * 错误返回实体类
 * 
 * @author Kevin-
 *
 */

public class Error {

	private int status;
	private String msg;

	public Error(int status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public int getStatus() {
		return status;
	}

}
