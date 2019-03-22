<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/common/path_nav.jsp"></jsp:include>
<c:choose>
	<c:when test="${cartProducts.size() == 0 }">
		<div style="height: 350px; padding-top: 50px; color: #afafaf">
			<h1 class="text-center">
				<b>购物车空空如也(´；ω；`)</b>
			</h1>
		</div>
	</c:when>
	<c:otherwise>
		<font size="5"><b>确认订单</b></font>
		<hr />
		<table class="table table-hover" style="font-size: 18px">
			<thead>
				<tr>
					<th>商品</th>
					<th>单价</th>
					<th>总额</th>
					<th>数量</th>
					<th>删除</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${cartProducts }" var="cp">
					<tr class="productRow">
						<td><div
								style="text-align: justify; height: 60px; width: 400px; overflow: hidden">
								<p class="hidden productId">${cp.product.id }</p>
								<a
									href="product_showProduct.action?s_product.id=${cp.product.id }"><img
									src="/market-data/product/${cp.product.imageName }"
									style="width: 60px; float: left;"></a>
								<p style="margin-left: 80px;">
									<a
										href="product_showProduct.action?s_product.id=${cp.product.id }"><b>${cp.product.name }</b></a>
								</p>
							</div></td>
						<td><b style="color: red"><span class="productPrice"><fmt:formatNumber
										value="${cp.product.onSale?cp.product.onSalePrice:cp.product.price }"
										pattern="#0.##"></fmt:formatNumber></span> 元</b></td>
						<td><b style="color: red"><span class="productTotalPrice"></span>
								元</b></td>
						<td><div>
								<div class="input-group"
									style="width: 120px; font-weight: bold;">
									<span class="input-group-btn">
										<button class="btn btn-default subtract" type="button">-</button>
									</span> <input type="text" value="${cp.num }"
										class="form-control text-center productNum"> <span
										class="input-group-btn">
										<button class="btn btn-default add" type="button">+</button>
									</span>
								</div>
							</div></td>
						<td><button class="btn btn-default delete" type="button"><b>删除</b></button></td>
					</tr>
				</c:forEach>
			</tbody>
			<tfoot>
				<tr style="background-color: #F5F5F5">
					<td colspan="5" class="text-right"><b
						style="color: red; font-size: 18px">总额：<span id="totalPrice"></span>
							元
					</b></td>
				</tr>
				<tr>
					<td colspan="5" class="text-center"><b
						style="margin-right: 40px">收货地址：${currentUser.address }</b><b
						style="margin-right: 40px">收货人：${empty currentUser.realName?currentUser.nickName :currentUser.realName }</b><b>联系方式：${currentUser.mobile }</b></td>
				</tr>
				<tr>
					<td colspan="5" class="text-right"><button type="button"
							class="btn btn-success" id="generateOrder">
							<font size="5"><b>提交订单</b></font>
						</button></td>
				</tr>
			</tfoot>
		</table>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	$(function() {
		function setTotalMoney() {
			var totalPrice = 0;
			$('.productRow').each(
					function() {
						var price = $(this).find('.productPrice').html();
						var num = $(this).find('.productNum').val();
						$(this).find('.productTotalPrice').html(
								Math.round(price * num * 100) / 100);
						totalPrice += price * num;
					});
			$('#totalPrice').html(Math.round(totalPrice * 100) / 100);
		}

		setTotalMoney();

		$('.add').click(function() {
			var inputNum = $(this).closest('.productRow').find('.productNum');
			if (inputNum.val() < 10000) {
				$.post("cart_updateCartProductNum.action", {
					'product.id' : $(this).closest('.productRow').find('.productId').html(),
					'num' : (parseFloat(inputNum.val()) + 1)
				}, function() {
					inputNum.val(parseFloat(inputNum.val()) + 1);
					setTotalMoney();
				});
			}
		});
		
		$('.subtract').click(function() {
			var inputNum = $(this).closest('.productRow').find('.productNum');
			if (inputNum.val() > 1) {
				$.post("cart_updateCartProductNum.action", {
					'product.id' : $(this).closest('.productRow').find('.productId').html(),
					'num' : (parseFloat(inputNum.val()) - 1)
				}, function() {
					inputNum.val(parseFloat(inputNum.val()) - 1);
					setTotalMoney();
				});
			}
		});

		$('.productNum').blur(function() {
			if ($(this).val() > 0 && $(this).val() < 10000){
				$(this).val(Math.floor($(this).val()));
			} else
				$(this).val(1);
			$.post("cart_updateCartProductNum.action", {
				'product.id' : $(this).closest('.productRow').find('.productId').html(),
				'num' : $(this).val()
			}, function() {
				setTotalMoney();
			});
		});

		$('.delete').click(function() {
			var deleteBtn = $(this);
			$.post("cart_deleteCartProduct.action", {
				'product.id' : $(this).closest('.productRow').find('.productId').html(),
			}, function() {
				//deleteBtn.closest('.productRow').remove();
				//setTotalMoney();
				window.location.reload();
			});
		});
		
		$('#generateOrder').click(function (){
			location.href = "order_save.action";
		});
	});
</script>