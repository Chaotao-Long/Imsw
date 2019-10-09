package com.im.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import com.im.entity.ServerMessage;
import com.im.websocket.SpringWebSocketHandler;

@RestController
//@Controller
@RequestMapping("/websocket")
public class WebSocketController {

	@Bean
	public SpringWebSocketHandler infoHandler() {
		return new SpringWebSocketHandler();
	}

//	post请求将username传过来存入session中并返回成功登录的状态码
	@RequestMapping(value = "/login", method = RequestMethod.POST)
//	@RequestMapping(value ="/login")
//	@ResponseBody
	public String tologin(//HttpServletRequest request, 
			HttpSession session, @RequestParam("username") String username) {
//        String username = request.getParameter("username");
		System.out.println(username + "登录");
//        HttpSession session = request.getSession(false);
		session.setAttribute("SESSION_USERNAME", username); // 一般直接保存user实体
		return "200";
	}

////	get请求将session中的username取出并返回给前端
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public Map<String, Object> login(HttpServletRequest request) {
//
//		HttpSession session = request.getSession();
//		String username = (String) session.getAttribute("SESSION_USERNAME");
//		System.out.println(username + "登录");
//		
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("username", username);
//		return map;
//	}
	
	@RequestMapping(value = "/broad", method = RequestMethod.POST)
//	@ResponseBody
	public String broad(@RequestParam("context") String context) {
		ServerMessage msg = new ServerMessage();
		msg.setContent(context);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String time = sdf.format(date);
//		long timestamp = date.getTime();
		msg.setTime(time);
		
		TextMessage textMessage = new TextMessage(msg.toString());
//		TextMessage textMessage = new TextMessage(context);
		System.out.println("1212");
		infoHandler().sendMessageToUsers(textMessage);
		System.out.println("群发成功");
		return "200";
	}
}
