<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="panel panel-default">
	<div class="panel-body">
		<ul class="nav nav-pills nav-stacked" id="nav">
			<c:forEach items="${bigTypeList }" var="bigType">
				<li><a
					href="product.action?s_product.bigType.id=${bigType.id }"><b>${bigType.name }</b></a></li>
				<div class="panel panel-default m-pop-list">
					<div class="panel-body">
						<a href="product.action?s_product.bigType.id=${bigType.id }"><b>${bigType.name }</b></a>
						<hr>
						<div class="m-pop-item">
							<c:forEach items="${bigType.smallTypes }" var="smallType">
								<a href="product.action?s_product.smallType.id=${smallType.id }"><b>${smallType.name }</b></a>
							</c:forEach>
						</div>
					</div>
				</div>
			</c:forEach>
		</ul>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		//自动弹出菜单
		$('#nav li').hover(function() {
			$(this).addClass('active');
			$(this).next().clearQueue().show(100);
		}, function() {
			$(this).removeClass('active');
			$(this).next().clearQueue().delay(500).hide(50);
		});
		$('.m-pop-list').hover(function() {
			$(this).clearQueue().show();
		}, function() {
			$(this).hide();
		});
	});
</script>

<c:if test="${productBrowsingHistory != null }">
	<div class="panel panel-default">
		<div class="panel-body">
			<h4>
				<b>已阅</b>
			</h4>
			<hr>
			<c:forEach items="${productBrowsingHistory }" var="product">
				<div style="text-align: justify;height: 60px;overflow: hidden;margin-bottom: 5px">
					<a href="product_showProduct.action?s_product.id=${product.id }"><img
						src="/market-data/product/${product.imageName }"
						style="width: 50px; float: left;"></a>
					<p>
						<a href="product_showProduct.action?s_product.id=${product.id }"
							style="font-size: 10px"><b>${product.name }</b></a>
					</p>
				</div>
			</c:forEach>
		</div>
	</div>
</c:if>