<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="/common/path_nav.jsp"></jsp:include>
<div class="row">
	<c:forEach items="${productList }" var="product">
		<div class="col-md-3 text-center" style="margin-bottom: 20px">
			<div class="m-product-item">
				<a href="product_showProduct.action?s_product.id=${product.id }"><img
					src="/market-data/product/${product.imageName }"></a>
				<p>
					<a href="product_showProduct.action?s_product.id=${product.id }"><b>${product.name }</b></a>
				</p>
				<p style="font-size: 20px; color: red">
					<b><fmt:formatNumber value="${product.onSale?product.onSalePrice:product.price }" pattern="#0.##"></fmt:formatNumber> 元</b>
				</p>
			</div>
		</div>
	</c:forEach>
</div>
<div class="row text-center">
	<ul class="pagination">
		<c:forEach items="${pagination.items }" var="item">
			<li
				class="${item.disabled?'disabled':'' } ${item.active?'active':''}"><a
				href="${item.disabled || item.active ? 'javascript:void(0)' : item.url }"><span>${item.number }</span></a></li>
		</c:forEach>
	</ul>
</div>
