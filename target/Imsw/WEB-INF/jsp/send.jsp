
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<!--
	<link rel="stylesheet" href="/css/style.css"/>
    -->
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript"
	src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
<script type="text/javascript">
	var websocket = null;
	if ('WebSocket' in window) {
		websocket = new WebSocket(
				"ws://localhost:8080/Vehicleye/websocket/socketServer");
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket(
				"ws://localhost:8080/Vehicleye/websocket/socketServer");
	} else {
		websocket = new SockJS(
				"http://localhost:8080/Vehicleye/sockjs/socketServer");
	}

	websocket.onopen = function (openEvt) {
		alert("连接状态为："+openEvt.type);
		console.log(openEvt.timeStamp);
	};

	websocket.onmessage = function (evt) {
		alert("super is:" + evt.data);
	}
	
	websocket.onerror = function () {
		alert("连接异常，已断开！");
	};
	websocket.onclose = function () {
		alert("连接已关闭");
	};



	function doSendUser() {

		alert(websocket.readyState + ":" + websocket.OPEN);
		if (websocket.readyState == websocket.OPEN) {
			var msg = document.getElementById("inputMsg").value;
			websocket.send("#anyone#" + msg);//调用后台handleTextMessage方法
			alert("发送成功!");
		} else {
			alert("连接失败!");
		}
	}

	function doSendUsers() {
		if (websocket.readyState == websocket.OPEN) {
			var msg = document.getElementById("inputMsg").value;
			websocket.send("#everyone#" + msg);//调用后台handleTextMessage方法
			alert("发送成功!");
		} else {
			alert("连接失败!");
		}
	}

	function closeWebSocket() {
		websocket.close();
	}

	window.close = function() {
		websocket.onclose();
		alert("disconnect!");
	}
</script>

</head>
<body>

	请输入：
	<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
	<button onclick="doSendUser();">发送</button>
	<button onclick="doSendUsers();">群发</button>
	<button onclick="closeWebSocket();">关闭连接</button>
</body>
</html>