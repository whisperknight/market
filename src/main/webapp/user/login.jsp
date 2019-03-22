<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="col-md-6 col-md-offset-3">
	<div class="panel panel-default">
		<div class="panel-body">
			<h1 style="display: inline-block; margin-right: 20px">
				<b>登录</b>
			</h1>
			<a href="user_toAdminLogin.action">管理员登录</a>
			<hr>
			<form action="user_login.action" method="post"
				onsubmit="return checkForm()" style="margin-bottom: 30px;"
				class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-2 control-label">用户名：</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="user.userName"
							id="userName" value="${user.userName }">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">密码：</label>
					<div class="col-sm-10">
						<input type="password" class="form-control" name="user.password"
							id="password" value="${user.password }">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">验证码：</label>
					<div class="col-sm-10">
						<input class="form-control"
							style="width: 100px; display: inline; margin-right: 20px;"
							type="text" value="${imageCode }" name="imageCode" id="imageCode"><img
							onclick="javascript:loadImage()" title="看不清，换一张" id="randImage"
							src="user/verificationCode.jsp" width="80" height="28"
							align="middle">
					</div>
				</div>
				<div class="col-sm-offset-2">
					<span id="error" class="help-block" style="color: red">${error }</span>
				</div>
				<div class="col-sm-offset-9">
					<button type="submit" class="btn btn-primary">登录</button>
					<a type="button" class="btn btn-default"
						href="user_toRegister.action">注册</a>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
	function checkForm() {
		if ($('#userName').val() == "") {
			$('#error').html('*用户名为空！');
			return false;
		}

		if ($('#password').val() == "") {
			$('#error').html('*密码为空！');
			return false;
		}

		if ($('#imageCode').val() == "") {
			$('#error').html('*验证码为空！');
			return false;
		}

		return true;
	}

	function loadImage() {
		$('#randImage').prop('src',
				"user/verificationCode.jsp?" + Math.random());
	}
</script>