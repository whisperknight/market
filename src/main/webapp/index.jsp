<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
		<div class="row" align="center" style="margin-bottom: 20px">
			<c:forEach items="${tagList }" var="tag">
				<a href="${tag.url }" class="label label-danger" target="_blank">${tag.name }</a>
			</c:forEach>
		</div>
		<div class="row">
			<div class="col-md-2">
				<jsp:include page="common/nav.jsp"></jsp:include>
			</div>

			<div class="col-md-7">
				<div class="panel panel-default">
					<div class="panel-body">
						<div>
							<a style="font-size: 18px; color: red"
								href="product.action?s_product.onSale=true"><b>全场特价</b></a>
							<hr>
						</div>
						<div class="row">
							<c:forEach items="${onSaleProductList }" var="onSaleProduct">
								<div class="col-md-4 text-center" style="margin-bottom: 20px">
									<div class="m-product-item">
										<a
											href="product_showProduct.action?s_product.id=${onSaleProduct.id }"><img
											src="/market-data/product/${onSaleProduct.imageName }"></a>
										<p>
											<a
												href="product_showProduct.action?s_product.id=${onSaleProduct.id }"><b>${onSaleProduct.name }</b></a>
										</p>
										<p style="font-size: 20px; color: red">
											<b><fmt:formatNumber value="${onSaleProduct.onSale?onSaleProduct.onSalePrice:onSaleProduct.price }" pattern="#0.##"></fmt:formatNumber> 元</b>
										</p>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</div>
			</div>

			<div class="col-md-3">
				<div class="panel panel-default">
					<div class="panel-body">
						<div>
							<p>
								<b style="font-size: 18px">商城头条</b>&nbsp;&nbsp;为您带来最新的资讯！
							</p>
							<hr>
						</div>
						<c:forEach items="${newsList }" var="news">
							<div>
								<a href="news_showNews.action?s_news.id=${news.id }">${news.title }</a>
							</div>
						</c:forEach>
					</div>
				</div>

				<div class="panel panel-default">
					<div class="panel-body">
						<div>
							<p>
								<b style="font-size: 18px">商城公告</b>&nbsp;&nbsp;你想知道的都在这里！
							</p>
							<hr>
						</div>
						<c:forEach items="${noticeList }" var="notice">
							<div>
								<a href="notice_showNotice.action?s_notice.id=${notice.id }">${notice.title }</a>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>

		<div class="row">
			<div class="panel panel-default">
				<div class="panel-body">
					<div>
						<a style="font-size: 18px; color: red"
							href="product.action?s_product.hot=true"><b>火热爆款</b></a>
						<hr>
					</div>
					<div class="row">
						<c:forEach items="${hotProductList }" var="hotProduct">
							<div class="col-md-2 text-center" style="margin-bottom: 20px">
								<div class="m-product-item">
									<a
										href="product_showProduct.action?s_product.id=${hotProduct.id }"><img
										src="/market-data/product/${hotProduct.imageName }"></a>
									<p>
										<a
											href="product_showProduct.action?s_product.id=${hotProduct.id }"><b>${hotProduct.name }</b></a>
									</p>
									<p style="font-size: 20px; color: red">
										<b><fmt:formatNumber value="${hotProduct.onSale?hotProduct.onSalePrice:hotProduct.price }" pattern="#0.##"></fmt:formatNumber> 元</b>
									</p>
								</div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="common/foot.jsp"></jsp:include>
</body>
</html>