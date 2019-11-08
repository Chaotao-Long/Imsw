<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注册页面</title>
</head>
<body>
<form>
	<input type="hidden" id="id" name="id" />

	<label>帐号：</label>
	<input type="text" id="username" placeholder="帐号" />
	<br>
	<label>密码：</label>
	<input type="password" id="password" placeholder="密码" />
	<br>
	<label>昵称：</label>
	<input type="text" id="nickname" placeholder="昵称" />
	<br>
	<label>头像：</label>
	<input type="text" id="photo" placeholder="头像" />
	<br>
	<label>性别：</label>
	<input value="true" type="radio" checked="checked" name="per" />男
	<input value="false" type="radio" name="per" />女
	<br>
	<label>年龄：</label>
	<input type="text" id="age" placeholder="年龄" />
	<br>
	<label>手机：</label>
	<input type="text" id="phone" placeholder="手机" />
	<br>
	<label>邮箱：</label>
	<input type="text" id="email" placeholder="邮箱" />
	<br>
	<label>简介：</label>
	<input type="text" id="des" placeholder="简介" />
	</form>
	<br>
	<button type="submit" onclick="submit();">提交</button>
	<!-- 	<input type="reset" value="重置"> -->

</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript">
	function check() {
		var radio = document.getElementsByName("per");
		for (i = 0; i < radio.length; i++) {
			if (radio[i].checked) {
				return radio[i].value;

			}
		}
	}

	function submit() {
		var username = $("#username").val();
		var password = $("#password").val();
		var nickname = $("#nickname").val();
		var photo = $("#photo").val();
		var sex = check();
		var age = $("#age").val();
		var phone = $("#phone").val();
		var email = $("#email").val();
		var des = $("#des").val();
		//console.log("sex:" + sex);
		//var registertime = new Date();
		//console.log("registertime:"+registertime);
		
		if (username == "" || password == "") {
			alert("输入的账号或密码不能为空！");

		} else {
			$.ajax({
				url : "${pageContext.request.contextPath }/user/submit",
				type : "post",
				data : JSON.stringify({
					username : username,
					password : password,
					nickname : nickname,
					photo : photo,
					sex : sex,
					age : age,
					phone : phone,
					email : email,
					des : des
					//registertime : registertime
				}),
				contentType : "application/json;charset=UTF-8",
				dataType : "json",
				success : function(data) {
					if (data.status == "200") {
						alert("注册成功")
						window.location.href = "${pageContext.request.contextPath }/user/logout";
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