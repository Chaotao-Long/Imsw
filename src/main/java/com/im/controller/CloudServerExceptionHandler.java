package com.im.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.im.entity.ConstantBean;
import com.im.entity.Error;
import com.im.exception.CloudServerCheckedException;
import com.im.exception.CloudServerDatabaseException;
import com.im.exception.CloudServerNotFoundException;
import com.im.exception.CloudServerPermissionDenyException;
import com.sun.corba.se.impl.orbutil.closure.Constant;

/**
 * 异常处理的类，会返回Error实体
 * 
 *
 */
@ControllerAdvice
public class CloudServerExceptionHandler {

	/**
	 * 捕获控制器中抛出的CloudServerNotFoundException，再处理
	 * 
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(CloudServerNotFoundException.class)
	
	public Map<String, Object> notFound(CloudServerNotFoundException e) {

		Map<String, Object> returnMap = new HashMap<>();
		String message = e.showExceptionInfo();
		
//		Error error = new Error(ConstantBean.NOT_FOUND_CODE, message);
		returnMap.put("status", ConstantBean.NOT_FOUND_CODE);
		returnMap.put("msg", message);
		return returnMap;
	}

	/**
	 * 捕获控制器抛出的CloudServerDatabaseException异常，再处理
	 * 
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(CloudServerDatabaseException.class)
	public Map<String, Object> databaseExcep(CloudServerDatabaseException e) {

		Map<String, Object> returnMap = new HashMap<>();
		String message = e.showExceptionInfo();
		
//		Error error = new Error(ConstantBean.DATABASE_EXCE_CODE, message);
		returnMap.put("status", ConstantBean.DATABASE_EXCE_CODE);
		returnMap.put("msg", message);
		return returnMap;
	}

	/**
	 * 捕获控制器抛出的CloudServerDatabaseException异常，再处理
	 * 
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(CloudServerPermissionDenyException.class)
	public Map<String, Object> permissionDeny(CloudServerPermissionDenyException e) {

		Map<String, Object> returnMap = new HashMap<>();
		String message = e.showExceptionInfo();

//		Error error = new Error(ConstantBean.PERMISSION_DENY_CODE, message);
		returnMap.put("status", ConstantBean.PERMISSION_DENY_CODE);
		returnMap.put("msg", message);
		return returnMap;
	}

	/**
	 * 捕获控制器抛出的CloudServerDatabaseException异常，再处理
	 * 
	 * @param e
	 * @return
	 */
	@ResponseBody
	@ExceptionHandler(CloudServerCheckedException.class)
	public Map<String, Object> ckeckedExcep(CloudServerCheckedException e) {
		
		Map<String, Object> returnMap = new HashMap<>();
		String message = e.showExceptionInfo();

//		Error error = new Error(ConstantBean.CHECKED_EXCE_CODE, message);
		returnMap.put("msg", ConstantBean.CHECKED_EXCE_CODE);
		returnMap.put("msg", message);
		return returnMap;
	}

}
