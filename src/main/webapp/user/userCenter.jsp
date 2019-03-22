<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="panel panel-default">
	<div class="panel-body">
		<div>
			<!-- Nav tabs -->
			<ul class="nav nav-tabs" role="tablist" id="myTabs">
				<li role="presentation" class="active"><a href="#userCenter"
					aria-controls="userCenter" role="tab" data-toggle="tab">个人信息</a></li>
				<li role="presentation"><a href="#orderCenter"
					aria-controls=orderCenter role="tab" data-toggle="tab">订单中心</a></li>
			</ul>
			<!-- Tab panes -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="userCenter">
					<div class="col-sm-8 col-sm-offset-2">
						<div class="form-group">
							<label class="col-sm-offset-2 control-label"><span
								class="m-star">*</span>为必填项！ </label>
						</div>
						<form class="form-horizontal" id="modifyForm">
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="m-star">*</span>用户名：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										value="${currentUser.userName }" readonly="readonly"
										id="userName">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">新密码：</label>
								<div class="col-sm-10">
									<input type="password" class="form-control"
										name="user.password" id="password" onblur="checkPassword()"
										placeholder="不填则不修改密码"> <span id="password-error"
										class="help-block" style="color: red"></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">确认新密码：</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="rePassword"
										onblur="checkRePassword()" placeholder="确认新密码"> <span
										id="rePassword-error" class="help-block" style="color: red"></span>
								</div>
							</div>
							<!-- 隐藏的头像上传区域 -->
							<div class="form-group" id="viewDiv">
								<label class="col-sm-2 control-label">头像：</label>
								<div class="col-sm-10">
									<div class="row" style="position: relative;">
										<div class="col-sm-5">
											<img
												src="/market-data/user/${empty currentUser?'default.jpg': currentUser.userImage }"
												class="img-thumbnail" id="img-view">
										</div>
										<div class="col-sm-7"
											style="position: absolute; bottom: 0px; right: 0px">
											<div class="input-group">
												<span class="input-group-btn"> <a
													class="btn btn-primary" id="upload-button" type="button">上传</a>
												</span> <input type="text" class="form-control" id="upload-view"
													placeholder="头像路径..." readonly="readonly"> <input
													type="text" name="user.userImage" id="imageName"
													class="hidden">
											</div>
										</div>
									</div>
								</div>
							</div>
							<input type="file" id="imageFile" name="imageFile" class="hidden">
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="m-star">*</span>昵称：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="user.nickName"
										id="nickName" onblur="checkNickName()"
										value="${currentUser.nickName }"> <span
										id="nickName-error" class="help-block" style="color: red"></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">真实姓名：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="user.realName"
										value="${currentUser.realName }">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">性别：</label>
								<div class="col-sm-10 radio">
									<label> <input type="radio" name="user.sex" value="男"
										${currentUser.sex=='男'?'checked':'' }>男
									</label> <label> <input type="radio" name="user.sex" value="女"
										${currentUser.sex=='女'?'checked':'' }>女
									</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">出生日期：</label>
								<div class="col-sm-6">
									<div class='input-group date' id='datetimepicker1'>
										<input type='text' class="form-control" name="user.birthday"
											value="<fmt:formatDate value="${currentUser.birthday.time }" pattern="yyyy-MM-dd" />" />
										<span class="input-group-addon"> <span
											class="glyphicon glyphicon-calendar"></span>
										</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">身份证：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control"
										name="user.identityCode" value="${currentUser.identityCode }"
										${not empty currentUser.identityCode?'readonly="readonly"':'' }>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">邮箱：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="user.email"
										value="${currentUser.email }">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="m-star">*</span>手机号：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="user.mobile"
										id="mobile" onblur="checkMobile()"
										value="${currentUser.mobile }"> <span
										id="mobile-error" class="help-block" style="color: red"></span>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label"><span
									class="m-star">*</span>收货地址：</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" name="user.address"
										id="address" onblur="checkAddress()"
										value="${currentUser.address }"> <span
										id="address-error" class="help-block" style="color: red"></span>
								</div>
							</div>
							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<button type="button" class="btn btn-primary" id="modifyBtn">保存</button>
									<button type="button" class="btn btn-default"
										onclick="javascript:location.href='index.jsp'">返回</button>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div role="tabpanel" class="tab-pane" id="orderCenter"
					style="padding-top: 20px">
					<c:choose>
						<c:when test="${orders.size() == 0}">
							<div style="height: 350px; padding-top: 50px; color: #afafaf">
								<h1 class="text-center">
									<b>请去购物车下单(´；ω；`)</b>
								</h1>
							</div>
						</c:when>
						<c:otherwise>
							<c:forEach items="${orders }" var="order">
								<table class="table" style="background-color: #F5F5F5">
									<thead>
										<tr style="background-color: #e2e2e2; font-size: 18px">
											<td>单号：${order.orderCode }</td>
											<td>下单时间：<fmt:formatDate
													value="${order.createTime.time }"
													pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td>
												<div class="status_parent">
													状态：<span class="status" style="display: none">${order.status }</span>
													<span class="status_1" style="display: none"><span
														style="color: #ef9017">立即付款</span></span> <span class="status_2"
														style="display: none"><span style="color: #ef9017">等待发货</span></span>
													<span class="status_3" style="display: none"><button
															type="button" class="btn btn-danger btn-xs"
															style="font-size: 18px; font-weight: bold;"
															onclick="confirm(this,${order.id})">确认收货</button></span> <span
														class="status_4" style="display: none"><span
														style="color: green">交易完成</span></span>
												</div>
											</td>
											<td>总额：<span style="color: red"><fmt:formatNumber
														value="${order.cost }" pattern="#0.##"></fmt:formatNumber>
													元 </span></td>
										</tr>
										<tr>
											<th>商品</th>
											<th>单价</th>
											<th>数量</th>
											<th>合计</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${order.products }" var="op">
											<tr class="productRow">
												<td><div
														style="text-align: justify; height: 60px; width: 400px; overflow: hidden">
														<p class="hidden productId">${op.product.id }</p>
														<a
															href="product_showProduct.action?s_product.id=${op.product.id }"
															target="_blank"><img
															src="/market-data/product/${op.product.imageName }"
															style="width: 60px; float: left;"></a>
														<p style="margin-left: 80px;">
															<a
																href="product_showProduct.action?s_product.id=${op.product.id }"
																target="_blank"><b>${op.product.name }</b></a>
														</p>
													</div></td>
												<td><b style="color: red; font-size: 18px"><span
														class="productPrice"><fmt:formatNumber
																value="${op.price }"
																pattern="#0.##"></fmt:formatNumber></span> 元</b></td>
												<td class="productNum">${op.num }</td>
												<td><b style="color: red; font-size: 18px"><span
														class="productTotalPrice"></span> 元</b></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Small modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog">
	<div class="modal-dialog modal-sm" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">信息</h4>
			</div>
			<div class="modal-body"></div>
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

		//file样式的隐式替代+文件异步上传
		$("#upload-button").click(function() {
			$("#imageFile").click().change(function() {
				if (checkImageFile()){
					var formFile = new FormData();
					formFile.append("imageFile", $('#imageFile')[0].files[0]); //加入文件对象
					
					//ajax上传头像图片
					$.ajax({
						url : "user_saveImageFile.action",
						data : formFile,
						type : 'post',
						dataType: "json",
						contentType : false,
						cache: false,//上传文件无需缓存
						processData: false,//用于对data参数进行序列化处理 这里必须false
						contentType: false, //必须
						success : function(result) {
							//图片预览
							$('#img-view').attr('src', '/market-data/user/'+result.imageFileFileName);
							//图片路径显示
							$("#upload-view").val($('#imageFile').val());
							//成功上传的图片名
							$('#imageName').val(result.imageFileFileName);
						}
					});
				}
			});
		});

		//表单异步提交
		$('#modifyBtn').click(function() {
			if (!checkForm())
				return;
			
			var ajax_data = $('#modifyForm').serialize(); //获取表单数据 
			$.post('user_modify.action',ajax_data,function(result){
				var result = eval('(' + result + ')');
				if (result.success) {
					$('.modal-body').html('修改成功！');
					$('#myModal').modal('show');
				} else {
					$('.modal-body').html('修改失败！');
					$('#myModal').modal('show');
				}
			});
		});
		
		//跳转订单中心
		if('${toOrder}' == 'true')
			$('#myTabs a[href="#orderCenter"]').tab('show');
		
		function setTotalMoney() {
			$('.productRow').each(function() {
				var price = $(this).find('.productPrice').html();
				var num = $(this).find('.productNum').html();
				$(this).find('.productTotalPrice').html(
						Math.round(price * num * 100) / 100);
			});
		}

		setTotalMoney();
		
		setStatus();
	});

	var postFile;//防止重复提交图片
	
	//单独验证图片文件
	function checkImageFile() {
		var file = $(':file')[0].files[0];
		if (file == null)
			return false;//为空不异步提交
		if(file != postFile)
			postFile = file;
		else
			return false;////防止重复提交图片
		
		var size = file.size;
		var type = file.type;
		if (size >= 2097152) {
			$('.modal-body').html('上传失败！文件必须小于2MB！');
			$('#myModal').modal('show');
			return false;
		}
		if (type != "image/png" && type != "image/jpeg" && type != "image/gif") {
			$('.modal-body').html('上传失败！只能上传png、jpg、gif图像文件！');
			$('#myModal').modal('show');
			return false;
		}
		return true;
	}
	
	//表单验证
	function checkPassword() {
		var userName = $('#userName').val();
		var password = $('#password').val();
		if (password == "")
			return true;//为空表示不修改密码
		else {
			var changed;
			$.ajaxSettings.async = false;//设置同步，exist才能取得post回到函数的值，同时必须验证完了才能提交
			$.post("user_ifPasswordchanged.action", {
				'user.userName' : userName,
				'user.password' : password
			}, function(data) {
				var data = eval('(' + data + ')');
				if (data.changed) {
					$('#password-error').html('');
					changed = true;
				} else {
					$('#password-error').html('与原密码相同！');
					changed = false;
				}
			});
			$.ajaxSettings.async = true;
			return changed;
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
		if (checkPassword() && checkRePassword() && checkNickName()
				&& checkMobile() && checkAddress())
			return true;
		return false;
	}
	
	//设置订单状态
	function setStatus() {
		$('.status').each(function() {
			var status = $(this).html();
			$(this).closest('.status_parent').find("[class^='status']").attr('style','display:none');
			$(this).closest('.status_parent').find('.status_' + status).attr('style','display:inline');
		});
	}
	
	//确认收货
	function confirm(comfirmBtn,orderId){
		$.post('order_confirm.action?',{'s_order.id':orderId},function(data){
			var data = eval('('+data+')');
			if(data.success){
				//location.href="order_toOrders.action";
				$(comfirmBtn).closest('.status_parent').find('.status').html(4);
				setStatus();
			}
			else
				alert('收货失败！');
		});
	}
</script>