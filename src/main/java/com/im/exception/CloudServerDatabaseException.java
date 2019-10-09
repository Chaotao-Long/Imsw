package com.im.exception;

/**
 * 自定义CloudServerDatabaseException异常
 * 
 * @author Kevin-
 *
 */

public class CloudServerDatabaseException extends RuntimeException {

	private String tableName;

	public CloudServerDatabaseException(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public String showExceptionInfo() {
		return (tableName != null) ? tableName + " table unexpected exception " : "No table name info";
	}

}
