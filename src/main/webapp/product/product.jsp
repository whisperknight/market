<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
	<h4>
		<b>${resultProduct.name }</b>
	</h4>
	<hr>
</div>
<div style="display: none;">
	<p id="productId">${resultProduct.id }</p>
</div>
<div class="row">
	<div class="col-md-5">
		<img src="/market-data/product/${resultProduct.imageName }"
			style="width: 400px">
	</div>
	<div class="col-md-6 col-md-offset-1"
		style="font-size: 18px; margin-top: 100px">
		<p style="margin-bottom: 20px;">
			<b>售价：<span style="font-size: 40px; color: red">
					<fmt:formatNumber value="${resultProduct.onSale?resultProduct.onSalePrice:resultProduct.price }" pattern="#0.##"></fmt:formatNumber> 元
			</span></b>
		</p>
		<p style="margin-bottom: 40px;">
			<b>库存：${resultProduct.stock } 件</b>
		</p>
		<div style="postion: relative">
			<a
				href="javascript:buy(${resultProduct.id},${not empty currentUser?currentUser.id : 0})"
				class="btn btn-danger btn-lg" style="font-size: 22px"><b>立即购买</b></a>
			<a
				href="javascript:addToCart(${resultProduct.id},${not empty currentUser?currentUser.id : 0})"
				style="position: absolute; bottom: 0px; margin-left: 30px"><b>加入购物车</b></a>
		</div>
	</div>
</div>
<div class="page-header">
	<h4>
		<b>商品详情</b>
	</h4>
</div>
<div>${resultProduct.description }</div>
<hr>
<div id="commentPart"></div>
<script type="text/javascript">
	$(function() {
		$.post("comment.action", {
			'comment.product.id' : $('#productId').html()
		}, function(data) {
			$('#commentPart').html(data);
		})
	});

	function addToCart(productId, currentUserId) {
		if (currentUserId == 0) {
			location.href = "user_toLogin.action";
		} else {
			$.post("cart_addToCart.action", {
				'product.id' : productId
			}, function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					location.reload();
				} else {
					alert(result.success);
				}
			});
		}
	}

	function buy(productId, currentUserId) {
		if (currentUserId == 0)
			location.href = "user_toLogin.action";
		else
			location.href = "cart_buy.action?product.id=" + productId;
	}
</script>
