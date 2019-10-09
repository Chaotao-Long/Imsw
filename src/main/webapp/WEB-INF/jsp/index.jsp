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
	<h1>查看用户信息</h1>
	用户：${USER_SESSION.username} 你好



	<form id="edit_customer_form">
		<input type="hidden" id="id" name="id" />
		<div class="form-group">
			<label for="nickname" class="col-sm-2 control-label">昵称</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="nickname"
					placeholder="昵称" name="nickname" />
			</div>
		</div>
		<div class="form-group">
			<label for="photo" class="col-sm-2 control-label">头像</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="photo" placeholder="头像"
					name="photo" />
			</div>
		</div>
		<div class="form-group">
			<label for="sex" class="col-sm-2 control-label">性别</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="sex" placeholder="性别"
					name="sex" />
			</div>
		</div>
		<div class="form-group">
			<label for="age" class="col-sm-2 control-label">年龄</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="age" placeholder="年龄"
					name="age" />
			</div>
		</div>
		<div class="form-group">
			<label for="phone" class="col-sm-2 control-label">手机</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="phone" placeholder="手机"
					name="phone" />
			</div>
		</div>
		<div class="form-group">
			<label for="email" class="col-sm-2 control-label">邮箱</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="email" placeholder="邮箱"
					name="email" />
			</div>
		</div>
		<div class="form-group">
			<label for="des" class="col-sm-2 control-label">简介</label>
			<div class="col-sm-10">
				<input type="text" class="form-control" id="des" placeholder="简介"
					name="des" />
			</div>
		</div>
	</form>
	<div class="modal-footer">
<!-- 		<button type="reset" class="btn btn-default" >重置</button> -->
		<button type="submit" class="btn btn-primary"
			onclick="submit();">保存修改</button>
	</div>

</body>
<!-- 引入js文件 -->
<!-- jQuery -->
<script src="<%=basePath%>js/jquery-1.11.3.min.js"></script>
<!-- Bootstrap Core JavaScript -->
<script src="<%=basePath%>js/bootstrap.min.js"></script>
<!-- Metis Menu Plugin JavaScript -->
<script src="<%=basePath%>js/metisMenu.min.js"></script>
<!-- DataTables JavaScript -->
<script src="<%=basePath%>js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>js/dataTables.bootstrap.min.js"></script>
<!-- Custom Theme JavaScript -->
<script src="<%=basePath%>js/sb-admin-2.js"></script>
<!-- 编写js代码 -->

<script type="text/javascript">
// 执行修改客户操作
	function updateCustomer() {
		$.post("<%=basePath%>user/changeUserInfo", $("#edit_customer_form").serialize(), function(data) {
			if (data.status == "200") {
				alert("客户信息更新成功！");
				//window.location.reload();
			} else {
				alert("客户信息更新失败！");
				window.location.reload();
			}
		});
	}
	
	
	
	
	function submit() {
		var nickname = $("#nickname").val();
		var photo = $("#photo").val();
		var sex = $("#sex").val();
		var age = $("#age").val();
		var phone = $("#phone").val();
		var email = $("#email").val();
		var des = $("#des").val();
		console.log(11111);
		
			$.ajax({
				url : "<%=basePath%>/user/changeUserInfo",
				type : "post",
				data : JSON.stringify({
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
					
						console.log(data);
						alert("修改成功")
 						//window.location.href = "login.html";
					
				},
				error : function() {
					alert("操作失败！");
				}
			});
		
	}
</script>
</html>