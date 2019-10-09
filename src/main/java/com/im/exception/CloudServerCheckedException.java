package com.im.exception;

/**
 * 自定义CloudServerDatabaseException异常，以代表java中检查型的异常
 * 
 * @author Kevin-
 *
 */

public class CloudServerCheckedException extends RuntimeException {

	private String lable;
	private Exception e;

	public CloudServerCheckedException(String lable, Exception e) {
		this.lable = lable;
		this.e = e;
	}

	public String getTableName() {
		return lable;
	}

	public Exception getE() {
		return e;
	}

	public String showExceptionInfo() {
		return (lable != null) ? lable : "No lable info";
	}
}
