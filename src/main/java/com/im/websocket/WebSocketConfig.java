package com.im.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;


/**
 * 
 * 说明：WebScoket配置处理器
 * 把处理器和拦截器注册到spring websocket中
 * @author 传智.BoBo老师
 * @version 1.0
 * @date 2016年10月27日
 */
@Configuration//配置开启WebSocket服务用来接收ws请求
@EnableWebSocket
@EnableWebMvc
public class WebSocketConfig implements WebSocketConfigurer {

	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(),"/websocket/socketServer")
                .addInterceptors(new ChatHandshakeInterceptor()).setAllowedOrigins("*");
        
        registry.addHandler(webSocketHandler(), "/sockjs/socketServer").setAllowedOrigins("*")
               .addInterceptors(new ChatHandshakeInterceptor()).withSockJS();
    }
 
    @Bean
    public TextWebSocketHandler webSocketHandler(){
 
        return new ChatWebSocketHandler();
    }

}
