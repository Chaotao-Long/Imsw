<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>imsw登录界面</title>
</head>
<body>
	<h2>表单提交</h2>
	<form action="./websocket/login" method="post" autocomplete="true">
		登录名：<input type="text" name="username" /> <input type="submit"
			value="登录聊天室" />
	</form>

	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>

	<span id="message">${msg}</span>
	<form action="${pageContext.request.contextPath }/user/login"
		method="post" onclick="return check()">
		账户：<input type="text" name="username" id="username" placeholder="字符长度不超过20位" /><br> 
		密码：<input type="password" name="password" id="password" placeholder="字符长度不超过20位" /><br> 
			<input type="submit" value="登录" name="登录" /> 
			<input type="button" value="注册" id="注册" onclick="location.href='${pageContext.request.contextPath }/user/toRegister'" />
		<%-- 			<a href='${pageContext.request.contextPath}/toRegister'> --%>
		<!-- 			<input type="button" value="注册" id="注册" /> -->
		<!-- 			</a> -->
	</form>
</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript">
	function check() {
		var username = $("#username").val();
		var password = $("#password").val();
		console.log(username+password);
		if (username == "" || password == "") {
			$("#message").text("账号或密码不能为空！");
			return false;
		}
		return true;
	}
</script>
</html>