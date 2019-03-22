<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="panel panel-default">
	<div class="panel-body">
		<div>
			<h2 class="text-center">
				<b>用户注册</b>
			</h2>
			<hr>
			<div class="m-register col-sm-8 col-sm-offset-2">
				<div class="form-group">
					<label class="col-sm-offset-2 control-label"><span
						class="m-star">*</span>为必填项！ </label>
				</div>
				<form class="form-horizontal" action="user_register.action"
					method="post" onsubmit="return checkForm()">
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>用户名：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.userName"
								id="userName" onblur="checkUserName()"> <span
								id="userName-error" class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>密码：</label>
						<div class="col-sm-10">
							<input type="password" class="form-control" name="user.password"
								id="password" onblur="checkPassword()"> <span
								id="password-error" class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>确认密码：</label>
						<div class="col-sm-10">
							<input type="password" class="form-control" id="rePassword"
								onblur="checkRePassword()"> <span id="rePassword-error"
								class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>昵称：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.nickName"
								id="nickName" onblur="checkNickName()"> <span
								id="nickName-error" class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">真实姓名：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.realName">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">性别：</label>
						<div class="col-sm-10 radio">
							<label> <input type="radio" name="user.sex" value="男"
								checked>男
							</label> <label> <input type="radio" name="user.sex" value="女">女
							</label>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">出生日期：</label>
						<div class="col-sm-6">
							<div class='input-group date' id='datetimepicker1'>
								<input type='text' class="form-control" name="user.birthday" />
								<span class="input-group-addon"> <span
									class="glyphicon glyphicon-calendar"></span>
								</span>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">身份证：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.identityCode">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">邮箱：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.email">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>手机号：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.mobile"
								id="mobile" onblur="checkMobile()"> <span
								id="mobile-error" class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><span class="m-star">*</span>收货地址：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="user.address"
								id="address" onblur="checkAddress()"> <span
								id="address-error" class="help-block" style="color: red"></span>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary">注册</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		//设置日期控件
		$('#datetimepicker1').datetimepicker({
			format : 'yyyy-mm-dd',
			minView : 'month',
			pickerPosition : 'bottom-left',
			language : 'zh-CN',
			autoclose : true
		});
	});

	//表单验证
	function checkUserName() {
		var userName = $('#userName').val();
		if (userName == "") {
			$('#userName-error').html('用户名不能为空！');
			return false;
		}
		var exist;
		$.ajaxSettings.async = false;//设置同步，exist才能取得post回到函数的值，同时必须验证完了才能提交
		$.post("user_existUserWithUserName.action", {
			'user.userName' : userName
		}, function(data) {
			var data = eval('(' + data + ')');
			if (data.exist) {
				$('#userName-error').html('用户名已存在！');
				exist = true;
			} else {
				$('#userName-error').html('');
				exist = false;
			}
		});
		$.ajaxSettings.async = true;
		return !exist;
	}

	function checkPassword() {
		if ($('#password').val() == "") {
			$('#password-error').html('密码不能为空！');
			return false;
		} else {
			$('#password-error').html('');
			return true;
		}
	}

	function checkRePassword() {
		if ($('#password').val() != $('#rePassword').val()) {
			$('#rePassword-error').html('密码不一致！');
			return false;
		} else {
			$('#rePassword-error').html('');
			return true;
		}
	}

	function checkNickName() {
		if ($('#nickName').val() == "") {
			$('#nickName-error').html('昵称不能为空！');
			return false;
		} else {
			$('#nickName-error').html('');
			return true;
		}
	}

	function checkMobile() {
		if ($('#mobile').val() == "") {
			$('#mobile-error').html('手机号不能为空！');
			return false;
		} else {
			$('#mobile-error').html('');
			return true;
		}
	}

	function checkAddress() {
		if ($('#address').val() == "") {
			$('#address-error').html('地址不能为空！');
			return false;
		} else {
			$('#address-error').html('');
			return true;
		}
	}

	function checkForm() {
		if (checkUserName() && checkPassword() && checkRePassword()
				&& checkNickName() && checkMobile() && checkAddress())
			return true;
		return false;
	}
</script>