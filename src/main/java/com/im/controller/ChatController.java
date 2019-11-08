package com.im.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.im.dbmodel.User;
import com.im.entity.ConstantBean;
import com.im.entity.ReMap;
import com.im.exception.CloudServerNotFoundException;
import com.im.log4j.Logger;
import com.im.service.UserService;
import com.im.util.ObjectUtils;

@RestController
@RequestMapping("/chat")
public class ChatController {

	public static Map<String, Object> sessionMap = new HashMap<String, Object>();
	
	public static Logger logger = Logger.getLogger(ChatController.class);
	
	@Autowired 
	private UserService userService;

//	post请求将username传过来存入session中并返回成功登录的状态码
	@RequestMapping(value = "/enterChat", method = RequestMethod.GET)
	public ReMap SingleChat(HttpServletRequest request, @RequestParam("username") String username) {
		System.out.println(username + "已传过来");
		
		User user = userService.getFromUserByUsername(username);
		if(ObjectUtils.isNull(user)) {
			logger.error("404, user not found!");
			throw new CloudServerNotFoundException(ConstantBean.USER_NOTFOUND); // 密码错误,权限异常
		}

		HttpSession session = request.getSession();
		// 登录操作
		// 判断是否是一个已经登录的用户，没有则登录
		if (null != session.getAttribute("SESSION_USERNAME")) {
			// 清除旧的用户
			session.removeAttribute("SESSION_USERNAME");
		}
		
		// 新登录,重新生成一个session
		session.setAttribute("SESSION_USERNAME", username);
		
		
		sessionMap.put("SESSION_USERNAME", username);
		System.out.println("session：" + session);
		System.out.println("新用户诞生了：" + username);
		ReMap reMap = new ReMap();
		return reMap;
	}

}
