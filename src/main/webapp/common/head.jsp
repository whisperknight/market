<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="m-head">
	<div class="m-head-contain">
		<h1>
			<b><a href="${pageContext.request.contextPath}"
				style="color: white; text-decoration: none;">Market商城网</a></b>
		</h1>
		<div class="m-head-line"></div>
		<h3>卖你想买，圆你所愿</h3>
	</div>
</div>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#my-navbar-1">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}">首页</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="my-navbar-1">
			<ul class="nav navbar-nav">
				<c:forEach items="${bigTypeList }" var="bigType">
					<li><a
						href="product.action?s_product.bigType.id=${bigType.id }">${bigType.name }</a></li>
				</c:forEach>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<c:choose>
					<c:when test="${not empty currentUser }">
						<li><a href="cart_toList.action" style="color: red">购物车(${currentUser.cart.products.size() })</a></li>
						<li><div class="userImageHead">
								<a href="user_userCenter.action"> <img
									src="/market-data/user/${empty currentUser?'default.jpg': currentUser.userImage }"
									alt="${currentUser.nickName }"></a>
							</div></li>
							<c:if test="${currentUser.status == -1 }">
								<li><a href="user_adminMain.action">管理</a></li>
							</c:if>
						<li><a href="user_logout.action">注销</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="user_toLogin.action" style="color: red">购物车</a></li>
						<li><a href="user_toLogin.action">登录</a></li>
						<li><a href="user_toRegister.action">注册</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<!-- /.navbar-collapse -->
	</div>
	<!-- /.container-fluid -->
</nav>
<script type="text/javascript">
	$(function() {
		$('.navbar-nav li').hover(function() {
			$(this).addClass('active');
		}, function() {
			$(this).removeClass('active');
		});

		setUserImagesPosition();
	});
</script>
