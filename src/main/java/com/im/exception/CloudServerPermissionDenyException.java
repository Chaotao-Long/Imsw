package com.im.exception;

/**
 * 自定义登录（绑定）权限，授权的PermissionDeny异常,会返回Error错误
 * 
 * @author Kevin-
 *
 */

public class CloudServerPermissionDenyException extends RuntimeException {

	private String operation;

	public CloudServerPermissionDenyException(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public String showExceptionInfo() {
		return (operation != null) ? operation + " permission denied" : "No operation info";
	}

}
