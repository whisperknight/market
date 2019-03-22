<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/js/easyui/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyui/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
	$(function() {
		//网格线变成实线
		$('#dg').datagrid('getPanel').addClass('lines-no');
		$('#dg-orderproduct').datagrid('getPanel').addClass('lines-no');

		//对话框关闭则自动重置内部内容
		$('#dlg').dialog({
			onClose : function() {
				$("#fm").form('reset');
			}
		});
	});

	//查询
	function searchOrder() {
		$('#dg').datagrid('load', {
			's_order.orderCode' : $('#s_orderCode').val(),
			's_order.user.userName' : $('#s_userName').val()
		});
	}

	//打开订单详情对话框
	function openOrderDetail() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length > 1) {
			$.messager.alert('系统提示', '只能选择一条数据！');
			return;
		}
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要查看的数据！');
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "订单详情");
		$("#orderCode").html(row.orderCode);
		$("#userName").html(row.user.userName);
		$("#createTime").html(row.createTime);
		$("#cost").html(row.cost + " 元");
		$("#status").html(formatterStatus(row.status));

		$("#dg-orderproduct").datagrid('load', {
			's_order.id' : row.id
		});
	}

	//发货
	function send() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要发货的订单！');
			return;
		}
		var orderIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			if(selectedRows[i].status == 1){
				$.messager.alert('发货失败', '存在未付款商品！');
				return;
			}
			if(selectedRows[i].status == 3){
				$.messager.alert('发货失败', '存在已发货的商品！');
				return;
			}
			if(selectedRows[i].status == 4){
				$.messager.alert('发货失败', '存在交易完成的商品！');
				return;
			}
			orderIds.push(selectedRows[i].id);
		}
		var strIds = orderIds.join(",");
		$.messager.confirm("系统提示", "确认要对这<font color=red>"
				+ selectedRows.length + "</font>条订单发货吗？", function(r) {
			if (r) {
				$.post("order_send.action", {
					'strIds' : strIds
				}, function(result) {
					if (result.success) {
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', '发货失败！');
					}
				}, "json");
			}
		});
	}

	//关闭对话框
	function closeOrderDetail() {
		$("#dlg").dialog("close");
	}

	function formatterUserName(value, row) {
		return row.user.userName;
	}

	function formatterStatus(value, row) {
		if (value == 1)
			return "等待付款(暂时没有实现付款功能，此状态直接跳过)";
		if (value == 2)
			return "<font color='red'>等待发货</font>";
		if (value == 3)
			return "等待收货";
		if (value == 4)
			return "交易完成";
	}

	function formatterImageName(value) {
		return "<img src='/market-data/product/"+value+"' style='width: 80px'></img>";
	}
</script>
<style type="text/css">
.lines-no td {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
}
</style>
<table id="dg" class="easyui-datagrid"
	data-options="url:'order_list.action',pagination:true,pageSize:20,rownumbers:true,fitColumns:true,fit:true,border:false,toolbar:'#tb'">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'id'">ID</th>
			<th data-options="field:'orderCode'" style="width: 100px">订单号</th>
			<th data-options="field:'user.userName',formatter:formatterUserName"
				style="width: 50px">用户名</th>
			<th data-options="field:'createTime'" style="width: 100px">下单时间</th>
			<th data-options="field:'cost'" style="width: 30px">总金额</th>
			<th data-options="field:'status',formatter:formatterStatus"
				style="width: 30px">订单状态</th>

		</tr>
	</thead>
</table>
<!-- 工具条 -->
<div id="tb">
	<a href="javascript:openOrderDetail()" class="easyui-linkbutton"
		data-options="iconCls:'icon-more',plain:true">订单详情</a> <a
		href="javascript:send()" class="easyui-linkbutton"
		data-options="iconCls:'icon-ok',plain:true">发货</a><span
		style="margin-right: 40px"></span><input type="text"
		class="easyui-textbox" id="s_orderCode" data-options="prompt:'订单号'" /><input
		type="text" class="easyui-textbox" id="s_userName"
		data-options="prompt:'用户名'" /><a href="javascript:searchOrder()"
		class="easyui-linkbutton"
		data-options="iconCls:'icon-search',plain:true">搜索</a>
</div>
<!-- 对话框 -->
<div id="dlg" class="easyui-dialog"
	style="width: 900px; height: 630px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons'">
	<table>
		<tr>
			<td>订单号：</td>
			<td style="padding-right: 130px"><div id="orderCode"></div></td>
			<td><div style="padding: 8px"></div></td>
			<td>用户名：</td>
			<td><div id="userName"></div></td>
		</tr>
		<tr>
			<td>下单时间：</td>
			<td><div id="createTime"></div></td>
			<td><div style="padding: 8px"></div></td>
			<td>总金额(元)：</td>
			<td><div id="cost"></div></td>
		</tr>
		<tr>
			<td>订单状态：</td>
			<td><div id="status"></div></td>
		</tr>
	</table>
	<div style="margin-top: 20px; width: 99%; height: 400px">
		<table id="dg-orderproduct" class="easyui-datagrid"
			data-options="url:'order_orderDetail.action',pagination:true,rownumbers:true,fitColumns:true,fit:true,border:false">
			<thead>
				<tr>
					<th data-options="field:'name'">商品名称</th>
					<th data-options="field:'imageName',formatter:formatterImageName">商品图片</th>
					<th data-options="field:'price'">当时价格(元)</th>
					<th data-options="field:'num'">订购数量(件)</th>
					<th data-options="field:'total'">总计(元)</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dlg-buttons">
		<a href="javascript:closeOrderDetail()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>