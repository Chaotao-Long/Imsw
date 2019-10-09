<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注册页面</title>
</head>
<body>

	账户：
	<input type="text" id="username" placeholder="字符长度不超过20位" />
	<br> 密码：
	<input type="password" id="password" placeholder="字符长度不超过20位" />
	<br>
	<button type="submit" onclick="submit();">提交</button>
<!-- 	<input type="reset" value="重置"> -->

</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript">
	function submit() {
		var username = $("#username").val();
		var password = $("#password").val();
		console.log("username:" + username);
		console.log("password:" + password);
		if (username == "" || password == "") {
			alert("输入的账号或密码不能为空！");
			//window.location.reload();
		} else {
			$.ajax({
				url : "${pageContext.request.contextPath }/user/submit",
				type : "post",
				data : JSON.stringify({
					username : username,
					password : password
				}),
				contentType : "application/json;charset=UTF-8",
				dataType : "json",
				success : function(data) {
					if (data == "200") {
						alert("注册成功")
 						//window.location.href = "login.html";
					}
				},
				error : function() {
					alert("操作失败！");
				}
			});
		}
	}
</script>
</html>