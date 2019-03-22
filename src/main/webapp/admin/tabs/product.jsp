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

		//对话框关闭则自动重置内部内容
		$('#dlg').dialog({
			onClose : function() {
				$("#fm").form('reset');
			}
		});
		$('#dlg-hot').dialog({
			onClose : function() {
				$("#fm-hot").form('reset');
			}
		});
		$('#dlg-onSale').dialog({
			onClose : function() {
				$("#fm-onSale").form('reset');
			}
		});

		//二级菜单
		$("#bigType").combobox(
				{
					onSelect : function(record) {
						$("#smallType").combobox("clear");
						$("#smallType").combobox(
								"reload",
								"type_getAllSmallType.action?bigType.id="
										+ record.id);
					}
				});
	});

	//查询
	function searchProduct() {
		$('#dg').datagrid('load', {
			's_product.name' : $('#s_productName').val()
		});
	}

	//批量删除商品
	function deleteProducts() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要删除的数据！');
			return;
		}
		var productIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			productIds.push(selectedRows[i].id);
		}
		var strIds = productIds.join(",");
		$.messager.confirm("系统提示", "确认要删除这<font color=red>"
				+ selectedRows.length + "</font>条数据吗？", function(r) {
			if (r) {
				$.post("product_delete.action", {
					'strIds' : strIds
				}, function(result) {
					if (result.success) {
						$("#dg").datagrid("reload");
					} else {
						$.messager.alert('系统提示', '删除失败！');
					}
				}, "json");
			}
		});
	}

	//打开添加商品对话框
	function openAddProductDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加商品");
	}

	//打开修改商品对话框
	function openModifyProductDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length > 1) {
			$.messager.alert('系统提示', '不能批量修改！');
			return;
		}
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要修改的数据！');
			return;
		}
		var row = selectedRows[0];
		$("#dlg").dialog("open").dialog("setTitle", "修改商品");
		//$("#fm").form("load", row); 因为struts2的原因传的是bean所以不能直接load
		$("#id").val(row.id);
		$("#name").textbox("setValue", row.name);
		$("#bigType").combobox("setValue", row.bigType.id);
		$("#smallType").combobox("setValue", row.smallType.id);
		$("#price").textbox("setValue", row.price);
		$("#stock").textbox("setValue", row.stock);
		$("#description").textbox("setValue", row.description);
	}

	//保存商品
	function saveProduct() {
		$("#fm").form("submit", {
			url : 'product_save.action',
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					closeProductDialog();
					$("#dg").datagrid("reload");
				} else
					$.messager.alert("系统提示", "保存失败！");
			}
		});
	}

	//关闭对话框
	function closeProductDialog() {
		$("#dlg").dialog("close");
	}

	//打开热卖对话框
	function openHotDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要设置的商品！');
			return;
		}
		$("#dlg-hot").dialog("open").dialog("setTitle", "设置热卖");
		$("#hot").checkbox({
			checked : true
		});
		$("#hotStartTime")
				.datetimebox(
						{
							value : (selectedRows.length == 1
									&& selectedRows[0].hotStartTime ? selectedRows[0].hotStartTime
									: getFormatDate())
						});
		$("#hotEndTime")
				.datetimebox(
						{
							value : (selectedRows.length == 1
									&& selectedRows[0].hotEndTime ? selectedRows[0].hotEndTime
									: '')
						});
	}

	//打开特价对话框
	function openOnSaleDialog() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length > 1) {
			$.messager.alert('系统提示', '不能批量设置特价！');
			return;
		}
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要设置的商品！');
			return;
		}
		$("#dlg-onSale").dialog("open").dialog("setTitle", "设置特价");
		$('#id-onSale').val(selectedRows[0].id);
		$("#onSale").checkbox({
			checked : true
		});
		$('#onSalePrice').numberbox("setValue",selectedRows[0].onSalePrice);
		$("#onSaleStartTime")
				.datetimebox(
						{
							value : (selectedRows[0].onSaleStartTime ? selectedRows[0].onSaleStartTime
									: getFormatDate())
						});
		$("#onSaleEndTime")
				.datetimebox(
						{
							value : (selectedRows[0].onSaleEndTime ? selectedRows[0].onSaleEndTime
									: '')
						});
	}

	//可批量设置热卖
	function setHot() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要设置的数据！');
			return;
		}
		var productIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			productIds.push(selectedRows[i].id);
		}
		var strIds = productIds.join(",");
		$("#fm-hot").form("submit", {
			url : 'product_setHot.action?strIds=' + strIds,
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					closeHotDialog();
					$("#dg").datagrid("reload");
				} else
					$.messager.alert("系统提示", "保存失败！");
			}
		});
	}

	//设置特价
	function setOnSale() {
		$("#fm-onSale").form("submit", {
			url : 'product_setOnSale.action',
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					closeOnSaleDialog();
					$("#dg").datagrid("reload");
				} else
					$.messager.alert("系统提示", "保存失败！");
			}
		});
	}

	//关闭热卖对话框
	function closeHotDialog() {
		$("#dlg-hot").dialog("close");
	}

	//关闭特价对话框
	function closeOnSaleDialog() {
		$("#dlg-onSale").dialog("close");
	}

	//datagrid前端优化数据显示
	function formatterImageName(value) {
		return "<img src='/market-data/product/"+value+"' style='width: 80px'></img>";
	}
	function formatterHot(value) {
		return value == true ? '是' : '否';
	}
	function formatterOnSale(value) {
		return value == true ? '是' : '否';
	}
	function formatterBigTypeId(value, row) {
		return row.bigType.id;
	}
	function formatterBigTypeName(value, row) {
		return row.bigType.name;
	}
	function formatterSmallTypeId(value, row) {
		return row.smallType.id;
	}
	function formatterSmallTypeName(value, row) {
		return row.smallType.name;
	}

	//js获取当前时间，并格式化为"yyyy-MM-dd HH:mm:ss"
	function getFormatDate() {
		var date = new Date();
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentDate = date.getFullYear() + "-" + month + "-" + strDate
				+ " " + date.getHours() + ":" + date.getMinutes() + ":"
				+ date.getSeconds();
		return currentDate;
	}
</script>
<style type="text/css">
.lines-no td {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
}
</style>
<table id="dg" class="easyui-datagrid"
	data-options="url:'product_list.action',pagination:true,rownumbers:true,fitColumns:true,fit:true,border:false,toolbar:'#tb'">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'id'">ID</th>
			<th
				data-options="field:'bigType.id',formatter:formatterBigTypeId,hidden:true">所属大类ID</th>
			<th
				data-options="field:'bigType.name',formatter:formatterBigTypeName">所属大类</th>
			<th
				data-options="field:'smallType.id',formatter:formatterSmallTypeId,hidden:true">所属小类ID</th>
			<th
				data-options="field:'smallType.name',formatter:formatterSmallTypeName">所属小类</th>
			<th data-options="field:'imageName',formatter:formatterImageName">商品图片</th>
			<th data-options="field:'name'">商品名称</th>
			<th data-options="field:'price'">价格(元)</th>
			<th data-options="field:'stock'">库存(件)</th>
			<th data-options="field:'description',hidden:true">描述</th>
			<th data-options="field:'hot',formatter:formatterHot">是否热卖</th>
			<th data-options="field:'hotStartTime'">热卖开始时间</th>
			<th data-options="field:'hotEndTime'">热卖结束时间</th>
			<th data-options="field:'onSale',formatter:formatterOnSale">是否特价</th>
			<th data-options="field:'onSalePrice'">特价(元)</th>
			<th data-options="field:'onSaleStartTime'">特价开始时间</th>
			<th data-options="field:'onSaleEndTime'">特价结束时间</th>
		</tr>
	</thead>
</table>
<!-- 工具条 -->
<div id="tb">
	<a href="javascript:openAddProductDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-add',plain:true">添加</a> <a
		href="javascript:openModifyProductDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-edit',plain:true">修改</a> <a
		href="javascript:deleteProducts()" class="easyui-linkbutton"
		data-options="iconCls:'icon-cancel',plain:true">删除</a><a
		href="javascript:openHotDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-hot',plain:true">设置热卖</a><a
		href="javascript:openOnSaleDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-star',plain:true">设置特价</a><span
		style="margin-right: 40px"></span><input type="text"
		class="easyui-searchbox" id="s_productName"
		data-options="prompt:'商品名称',searcher:searchProduct" />
</div>
<!-- 对话框 -->
<div id="dlg" class="easyui-dialog"
	style="width: 780px; height: 550px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons'">
	<form id="fm" method="post" enctype="multipart/form-data">
		<table>
			<tr style="display: none;">
				<td><input type="text" id="id" name="product.id" /></td>
			</tr>
			<tr>
				<td>商品名称：</td>
				<td><input class="easyui-textbox" id="name" name="product.name"
					data-options="validType:'length[2,50]'" required="required"
					style="width: 400px"></td>
			</tr>
			<tr>
				<td>商品图片：</td>
				<td><input class="easyui-filebox" id="picFile" name="picFile"
					data-options="buttonText:'选择商品图片'" style="width: 400px"></td>
			</tr>
			<tr>
				<td>所属大类：</td>
				<td><input id="bigType" class="easyui-combobox"
					name="product.bigType.id"
					data-options="panelHeight:'auto',editable:false,valueField:'id',textField:'name',url:'type_getAllBigType.action'"
					required="required" style="width: 200px"></td>
			</tr>
			<tr>
				<td>所属小类：</td>
				<td><input id="smallType" class="easyui-combobox"
					name="product.smallType.id"
					data-options="panelHeight:'auto',editable:false,valueField:'id',textField:'name'"
					required="required" style="width: 200px"></td>
			</tr>
			<tr>
				<td>价格(元)：</td>
				<td><input class="easyui-numberbox" id="price"
					name="product.price" data-options="min:0.01,precision:2"
					required="required" style="width: 100px"></td>
			</tr>
			<tr>
				<td>库存(件)：</td>
				<td><input class="easyui-numberbox" id="stock"
					name="product.stock" data-options="min:0,max:99999"
					required="required" style="width: 100px"></td>
			</tr>
			<tr>
				<td valign="top">描述：</td>
				<td><input class="easyui-textbox" id="description"
					name="product.description"
					data-options="multiline:true,validType:'length[0,2000]'"
					style="width: 600px; height: 200px"></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons">
		<a href="javascript:saveProduct()" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'">保存</a> <a
			href="javascript:closeProductDialog()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>
<!-- 热卖对话框 -->
<div id="dlg-hot" class="easyui-dialog"
	style="width: 500px; height: 300px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons-hot'">
	<form id="fm-hot" method="post">
		<table>
			<tr>
				<td>是否热卖：</td>
				<td><input class="easyui-checkbox" id="hot" name="product.hot"
					value="true"></td>
			</tr>
			<tr>
				<td>开始时间：</td>
				<td><input class="easyui-datetimebox" id="hotStartTime"
					name="hotStartTime" required="required"
					style="width: 200px"></td>
			</tr>
			<tr>
				<td>结束时间：</td>
				<td><input class="easyui-datetimebox" id="hotEndTime"
					name="hotEndTime" required="required" style="width: 200px"></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons-hot">
		<a href="javascript:setHot()" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'">确定</a> <a
			href="javascript:closeHotDialog()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>
<!-- 特价对话框 -->
<div id="dlg-onSale" class="easyui-dialog"
	style="width: 500px; height: 300px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons-onSale'">
	<form id="fm-onSale" method="post">
		<table>
			<tr style="display: none;">
				<td><input type="text" id="id-onSale" name="product.id" /></td>
			</tr>
			<tr>
				<td>是否特价：</td>
				<td><input class="easyui-checkbox" id="onSale"
					name="product.onSale" value="true"></td>
			</tr>
			<tr>
				<td>特价(元)：</td>
				<td><input class="easyui-numberbox" id="onSalePrice"
					name="product.onSalePrice" data-options="min:0.01,precision:2"
					required="required" style="width: 200px"></td>
			</tr>
			<tr>
				<td>开始时间：</td>
				<td><input class="easyui-datetimebox" id="onSaleStartTime"
					name="onSaleStartTime" required="required"
					style="width: 200px"></td>
			</tr>
			<tr>
				<td>结束时间：</td>
				<td><input class="easyui-datetimebox" id="onSaleEndTime"
					name="onSaleEndTime" required="required"
					style="width: 200px"></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons-onSale">
		<a href="javascript:setOnSale()" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'">确定</a> <a
			href="javascript:closeOnSaleDialog()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>