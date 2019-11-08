package com.im.websocket;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import com.im.controller.ChatController;
import com.im.util.ObjectUtils;

/**
 * websocket的链接建立是基于http握手协议，我们可以添加一个拦截器处理握手之前和握手之后过程
 * 
 * @author BoBo
 *
 */
@Component
//@ServerEndpoint(value ="/chatWebSocketHandler",configurator=GetHttpSessionConfigurator.class)
public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	//private HttpSession session;
	
	/**
	 * 握手之前，若返回false，则不建立链接
	 */
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, 
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

		if (request instanceof ServletServerHttpRequest) { // 判断request是否为ServletServerHttpRequest实例
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request; // 将request强转为ServletServerHttpRequest类型
			HttpSession session1 = servletRequest.getServletRequest().getSession(false);
			//session = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
			Map<String, Object> session = ChatController.sessionMap;
			System.out.println("拦截器里的session：" + session);
			// 如果用户已经登录，允许聊天
			// 有这个session
			if (ObjectUtils.isNotNull(session)) {
				// 获取登录的用户
				String username = (String) session.get("SESSION_USERNAME");
				// 有这个session中键对应的值
				if (ObjectUtils.isNotNull(username)) {
					// 将用户放入socket处理器的会话(WebSocketSession)中
					attributes.put("WEBSOCKET_USERNAME", username);
					System.out.println("Websocket:用户" + attributes.get("WEBSOCKET_USERNAME") + "要建立连接");
				}
				else {
					System.out.println("session中没有这个值，条目为空或键对应的值为空");
					return false;
				}
			} 
			else {
				// 用户没有登录，拒绝聊天
				// 握手失败！
				System.out.println("session 为空--------------握手已失败...");
				return false;
			}
		}
		System.out.println("--------------握手开始...");
		return super.beforeHandshake(request, response, wsHandler, attributes);

	}
	
	

	/**
	 * 握手之后
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
		System.out.println("--------------握手成功啦...");
        super.afterHandshake(request, response, wsHandler, exception);
	}

}
