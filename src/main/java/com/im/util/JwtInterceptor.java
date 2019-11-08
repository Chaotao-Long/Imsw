package com.im.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.im.entity.ReMap;

@Component
public class JwtInterceptor implements HandlerInterceptor {
	
	public static String username = null;

	org.slf4j.Logger logger = LoggerFactory.getLogger("interceptor");

	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * Token validates the interceptor
	 * 
	 * @author peach
	 * @time 2019-10-19 19:19:32
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
	
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		
		ReMap reMap = new ReMap();
		PrintWriter out = null;
//		Cookie[] cookies = request.getCookies();		 //判断前端是否将cookie传过来，若没有cookie即没有token
//		if(ObjectUtils.isNull(cookies)) {
//			reMap.setStatus(401);
//			reMap.setMsg("Login verification failed, please login again!");
//			InfoUtil.returnMap(response, out, reMap);
//			return false;
//		}
			
		String token = request.getHeader("token");	
		System.out.println("token:" + token);
		if(token.length() == 0) {
			reMap.setStatus(401);
			reMap.setMsg("Login verification failed, please login again!");
			InfoUtil.returnMap(response, out, reMap);
			return false;
		}
		
		String[] strings = token.split("/");
		if (Long.parseLong(strings[2]) > System.currentTimeMillis()) {
			if (JwtUtil.sign(strings[0]).equals(strings[1]) && JwtUtil.unsign(strings[1]).equals(strings[0])) {
				
				username = strings[0];
				return true;
			} else {
				reMap.setStatus(401);
				reMap.setMsg("Login verification failed, please login again!");
				InfoUtil.returnMap(response, out, reMap);
				return false;
			}
		}
		reMap.setStatus(404);
		reMap.setMsg("Login expire, please login again!");
		InfoUtil.returnMap(response, out, reMap);
		return false;
	}

}