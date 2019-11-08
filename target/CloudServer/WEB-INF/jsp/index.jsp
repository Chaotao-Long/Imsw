<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改用户信息</title>
</head>
<body>
	<h1>标识</h1>
	用户：${USER_SESSION.username} 你好
	密码：${USER_SESSION.password}
	余额：${USER_SESSION.balance}
<%-- 	昵称：${USER_SESSION.nickname} --%>
<%-- 	头像：${USER_SESSION.photo} --%>
<%-- 	年龄：${USER_SESSION.age} --%>
<%-- 	手机：${USER_SESSION.phone} --%>
<%-- 	邮箱：${USER_SESSION.email} --%>
<%-- 	简介：${USER_SESSION.des} --%>
	<form>
		<input type="hidden" id="username" name="username" /> <label>昵称：</label>
		<input type="text" id="nickname" placeholder="昵称" /> <br> <label>头像：</label>
		<input type="text" id="photo" placeholder="头像" /> <br> <label>性别：</label>
		<input value="true" type="radio" checked="checked" name="per" />男 <input
			value="false" type="radio" name="per" />女 <br> <label>年龄：</label>
		<input type="text" id="age" placeholder="年龄" /> <br> <label>手机：</label>
		<input type="text" id="phone" placeholder="手机" /> <br> <label>邮箱：</label>
		<input type="text" id="email" placeholder="邮箱" /> <br> <label>简介：</label>
		<input type="text" id="des" placeholder="简介" />
	</form>
	<div class="modal-footer">
		<!-- 		<button type="reset" class="btn btn-default" >重置</button> -->
		<button type="submit" class="btn btn-primary" onclick="submit();">保存修改</button>
	</div>
	<br><br><br><br><br><br><br><br><br><br>
	<div><input type="button" value="退出登录" onclick="location.href='${pageContext.request.contextPath }/user/logout'" /></div>

</body>
<script type="text/javascript"
	src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript">

//	加载页面时触发的函数
// window.onload = function() {

    $("#nickname").val("${USER_SESSION.nickname}");
    $("#photo").val("${USER_SESSION.photo}");
    $("#age").val("${USER_SESSION.age}");
    $("#phone").val("${USER_SESSION.phone}");
    $("#email").val("${USER_SESSION.email}");
    $("#des").val("${USER_SESSION.des}");

// }
	
function check() {
	var radio = document.getElementsByName("per");
	for (i = 0; i < radio.length; i++) {
		if (radio[i].checked) {
			return radio[i].value;

		}
	}
}
	
	
	function submit() {
		var username = "${USER_SESSION.username}";
		var nickname = $("#nickname").val();
		var photo = $("#photo").val();
		var sex = check();
		var age = $("#age").val();
		var phone = $("#phone").val();
		var email = $("#email").val();
		var des = $("#des").val();

			$.ajax({
				url : "<%=basePath%>/user/changeUserInfo",
				type : "post",
				data : JSON.stringify({		
					username : username,
					nickname : nickname,
					photo : photo,
					sex : sex,
					age : age,
					phone : phone,
					email : email,
					des : des
				}),
				contentType : "application/json;charset=UTF-8",
				dataType : "json",
				success : function(data) {
					if(data.status == "200")
						{
						console.log(data.msg);
						alert("修改成功")
 						window.location.href = "${pageContext.request.contextPath }/user/logout";
						}else if(data.status == "402"){
							console.log(data.msg);
							alert("修改失败！");
						}else{
							console.log(data.msg);
							alert("修改失败！");
						}
					
				},
				error : function() {
					alert("操作失败！");
				}
			});
		
	}
</script>
</html>