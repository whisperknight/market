<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="panel panel-default">
	<div class="panel-body">
		<div class="text-center" style="height: 350px;">
			<h2>
				<b>注册成功</b>
			</h2>
			<hr>
			<h3 style="margin-top: 50px;">
				<b>您已成功注册为Market商城网普通会员！</b>
			</h3>
			<h3 style="margin-top: 50px;">
				<b>正在跳转至主页...</b>
			</h3>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		window.setTimeout(function() {
			location.href = "index.jsp";
		}, 3000);
	});
</script>

