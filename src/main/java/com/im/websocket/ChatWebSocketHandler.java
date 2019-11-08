package com.im.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import com.im.entity.ConstantBean;
import com.im.entity.Message;
import com.im.exception.CloudServerNotFoundException;
import com.im.util.GsonUtils;

/**
 * 
 * 说明：WebSocket处理器
 * 
 * @author peach
 * @version 1.0
 * @date 2019/10/31
 */
//@Component("chatWebSocketHandler")
public class ChatWebSocketHandler extends TextWebSocketHandler {

	// 在线用户的SOCKETsession(存储了所有的通信通道)
	private static final Map<String, WebSocketSession> users; // Map来存储WebSocketSession，key用USER_ID 即在线用户列表

	// 用户标识
	private static final String USER_NAME = "WEBSOCKET_USERNAME"; // 对应监听器从的key

	// 存储所有的在线用户
	static {
		users = new HashMap<String, WebSocketSession>();
	}
	
	public ChatWebSocketHandler() {
	}

	/**
	 * webscoket建立好链接之后的处理函数--连接建立后的准备工作
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		// 将当前的连接的用户会话放入MAP,key是用户编号
		String username = (String) webSocketSession.getAttributes().get(USER_NAME);
		users.put(username, webSocketSession);
		// 每一个加入聊天室的用户的username和webSocketSession信息都会以key-value的形式存储在hashmap结构中，故users涵盖全部用户信息
		// 系统群发消息告知大家
		Message msg = new Message();
		msg.setText("用户" + username + "已进入聊天室！");
		msg.setDate(new Date());
		// 获取所有在线的WebSocketSession对象集合
		Set<Entry<String, WebSocketSession>> entrySet = users.entrySet(); // 获取映射表条目(键值对)entrySet
		// 将最新的所有的在线人列表放入消息对象的list集合中，用于页面显示
		for (Entry<String, WebSocketSession> entry : entrySet) { // 遍历映射表条目(键值对)entrySet,每个entry代表一个条目
			msg.getUsernamelist().add((String) entry.getValue().getAttributes().get(USER_NAME));
//将每个条目的值(此时为webSocketSession)再调用其getAttributes()返回键值对再取其键返回其值(此时为User)最终即用msg的userlist的add()方法添加User
		}

		// 将消息转换为json
		TextMessage message = new TextMessage(GsonUtils.toJson(msg));
		// 群发消息
		sendMessageToAll(message);

	}

	@Override
	/**
	 * 客户端发送服务器的消息时的处理函数，在这里收到消息之后可以分发消息
	 */
	// 处理消息：当一个新的WebSocket到达的时候，会被调用（在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理）
	public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) throws Exception {
		
		super.handleTextMessage(webSocketSession, message);
		
		// 如果消息没有任何内容，则直接返回
		if (message.getPayloadLength() == 0)
			return;
		// 反序列化服务端收到的json消息
		Message msg = GsonUtils.fromJson(message.getPayload().toString(), Message.class);
		System.out.println("msg.toName:"+msg.toName);
		msg.setDate(new Date());
		// 处理html的字符，转义：
		String text = msg.getText();
		// 转换为HTML转义字符表示
		String htmlEscapeText = HtmlUtils.htmlEscape(text);
		msg.setText(htmlEscapeText);
		System.out.println("消息（可存数据库作为历史记录）:" + message.getPayload().toString());
		// 判断是群发还是单发
		if (msg.getToName() == null) {
			// 群发
			sendMessageToAll(new TextMessage(GsonUtils.toJson(msg)));
		} else{
			// 单发
			sendMessageToUser(msg.getFromName(), msg.getToName(), new TextMessage(GsonUtils.toJson(msg)));
		}
	}

	@Override
	/**
	 * 消息传输过程中出现的异常处理函数 处理传输错误：处理由底层WebSocket消息传输过程中发生的异常
	 */
	public void handleTransportError(WebSocketSession webSocketSession, Throwable exception) throws Exception {
		// 记录日志，准备关闭连接
		System.out.println("Websocket异常断开:" + webSocketSession.getId() + "已经关闭"); // 每个用户将信息存webSocketSession时都自动生成一个id
		// 一旦发生异常，强制用户下线，关闭session
		if (webSocketSession.isOpen()) {
			webSocketSession.close();
		}

		// 群发消息告知大家
		Message msg = new Message();
		msg.setDate(new Date());

		// 获取异常的用户的会话中的用户名
		String username = (String) webSocketSession.getAttributes().get(USER_NAME);
		// 获取所有的用户的会话
		Set<Entry<String, WebSocketSession>> entrySet = users.entrySet();
		// 并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）从entrySet中移除
		for (Entry<String, WebSocketSession> entry : entrySet) {
			if (entry.getKey().equals(username)) {
				msg.setText("用户名为：" + username + "异常退出！");
				// 清除在线会话
				users.remove(entry.getKey());
				// 记录日志：
				System.out.println("Socket会话已经移除:用户名" + entry.getKey());
				break;
			}
		}

		// 此时的entrySet已经除去了出现异常的用户（username），重新再add进usernamelist中，以达到从msg的userlist中删除的效果
		for (Entry<String, WebSocketSession> entry : entrySet) {
			msg.getUsernamelist().add((String) entry.getValue().getAttributes().get(USER_NAME));
			// 每个entry都有其webSocketSession值，而每个webSocketSession的getAttributes()都对应一个session的键值对，故通过遍历可以得到每个用户的username
		}
		// 并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
		TextMessage message = new TextMessage(GsonUtils.toJson(msg));
		sendMessageToAll(message);

		throw new CloudServerNotFoundException(ConstantBean.ALREADY_ADMIN);
	}

	@Override
	/**
	 * websocket链接关闭的回调 连接关闭后：一般是回收资源等
	 */
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
		// 记录日志，准备关闭连接
		System.out.println("Websocket正常断开:" + webSocketSession.getId() + "已经关闭");

		// 群发消息告知大家
		Message msg = new Message();
		msg.setDate(new Date());

		// 获取异常的用户的会话中的用户编号
		String username = (String) webSocketSession.getAttributes().get(USER_NAME);
		Set<Entry<String, WebSocketSession>> entrySet = users.entrySet();
		// 并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
		for (Entry<String, WebSocketSession> entry : entrySet) {
			if (entry.getKey().equals(username)) {
				// 群发消息告知大家
				msg.setText("用户名为：" + username + "正常退出.......");
				// 清除在线会话
				users.remove(entry.getKey());
				// 记录日志：
				System.out.println("Socket会话已经移除:用户名" + entry.getKey());
				break;
			}
		}

		// 并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
		for (Entry<String, WebSocketSession> entry : entrySet) {
			msg.getUsernamelist().add((String) entry.getValue().getAttributes().get(USER_NAME));
		}

		TextMessage message = new TextMessage(GsonUtils.toJson(msg));
		sendMessageToAll(message);
	}

	@Override
	/**
	 * 是否支持处理拆分消息，返回true返回拆分消息
	 */
	// 是否支持部分消息：如果设置为true，那么一个大的或未知尺寸的消息将会被分割，并会收到多次消息（会通过多次调用方法handleMessage(WebSocketSession,
	// WebSocketMessage). ）
	// 如果分为多条消息，那么可以通过一个api：org.springframework.web.socket.WebSocketMessage.isLast()
	// 是否是某条消息的最后一部分。
	// 默认一般为false，消息不分割
	public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 
	 * 说明：给某个人发信息
	 * 
	 * @param id
	 * @param message
	 * @author peach
	 * @throws IOException
	 * @time：2019/10/30
	 */
	private void sendMessageToUser(String formname, String toname, TextMessage message) throws IOException {
		// 获取到要接收消息的用户的session
		WebSocketSession formwebSocketSession = users.get(formname);
		WebSocketSession towebSocketSession = users.get(toname);
		if (towebSocketSession != null && towebSocketSession.isOpen()) {
			// 发送消息
			towebSocketSession.sendMessage(message);
			formwebSocketSession.sendMessage(message);
		}
	}

	/**
	 * 消息类型：系统消息，群聊消息
	 * 说明：群发信息：给所有在线用户发送消息
	 * 
	 * @author peach
	 * @time：2019/10/30
	 */
	private void sendMessageToAll(final TextMessage message) {
		// 对用户发送的消息内容进行转义

		// 获取到所有在线用户的SocketSession对象
		Set<Entry<String, WebSocketSession>> entrySet = users.entrySet();
		for (Entry<String, WebSocketSession> entry : entrySet) {
			// 某用户的WebSocketSession
			final WebSocketSession webSocketSession = entry.getValue();
			// 判断连接是否仍然打开的
			if (webSocketSession.isOpen()) {
				// 开启多线程发送消息（效率高）
				new Thread(new Runnable() {
					public void run() {
						try {
							if (webSocketSession.isOpen()) {
								webSocketSession.sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}).start();

			}
		}
	}

}
