package com.im.exception;

/**
 * 自定义CloudServerNotFoundException异常
 * 
 * @author Kevin-
 *
 */

public class CloudServerNotFoundException extends RuntimeException {

	private String typeOfNotFound;

	public CloudServerNotFoundException(String type) {
		this.typeOfNotFound = type;
	}

	public String getTypeOfNotFound() {
		return typeOfNotFound;
	}

	public String showExceptionInfo() {
		return (typeOfNotFound != null) ? typeOfNotFound + " Not Found" : "No type information";
	}

}
