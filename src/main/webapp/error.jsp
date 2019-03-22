<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="${pageContext.request.contextPath}/favicon.ico"
	rel="shortcut icon">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/market.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/js/bootstrap/css/bootstrap.min.css">
<script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/js/bootstrap/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/market.js"></script>
<title>Market商城网</title>
</head>
<body class="m-body">
	<jsp:include page="common/head.jsp"></jsp:include>
	<jsp:include page="common/search.jsp"></jsp:include>
	<div class="container">
		<div class="row">
			<div class="panel panel-default">
				<div class="panel-body text-center">
					<div style="height: 400px; padding-top: 150px;">
						<h1>
							<b>很可惜，未找到该页面！</b>
						</h1>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="common/foot.jsp"></jsp:include>
</body>
</html>