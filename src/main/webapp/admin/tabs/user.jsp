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
	});

	//查询
	function searchUser() {
		$('#dg').datagrid('load', {
			's_user.userName' : $('#s_userName').val()
		});
	}

	//批量删除用户
	function deleteUsers() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要删除的数据！');
			return;
		}
		var userIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			userIds.push(selectedRows[i].id);
		}
		var strIds = userIds.join(",");
		$.messager.confirm("系统提示", "确认要删除这<font color=red>"
				+ selectedRows.length + "</font>条数据吗？", function(r) {
			if (r) {
				$.post("user_deleteUser.action", {
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

	//打开添加用户对话框
	function openAddUserDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加用户");
		$("#userName").textbox("readonly", false);
		//打开用户名验证
		$('#userName').textbox({
			novalidate : false
		});
	}

	//打开修改用户对话框
	function openModifyUserDialog() {
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
		$("#dlg").dialog("open").dialog("setTitle", "修改用户");
		//$("#fm").form("load", row); 因为struts2的原因传的是bean所以不能直接load
		$("#id").val(row.id);
		$("#status").combobox("setValue", row.status);
		$("#userName").textbox("setValue", row.userName);
		$("#userName").textbox("readonly", true);
		//去除用户名验证
		$('#userName').textbox({
			novalidate : true
		});
		$("#password").textbox("setValue", row.password);
		$("#nickName").textbox("setValue", row.nickName);
		$("#realName").textbox("setValue", row.realName);
		$("#userImage").textbox("setValue", row.userImage);
		$("#sex").combobox("setValue", row.sex);
		$("#birthday").datebox("setValue", row.birthday);
		$("#identityCode").textbox("setValue", row.identityCode);
		$("#email").textbox("setValue", row.email);
		$("#mobile").textbox("setValue", row.mobile);
		$("#address").textbox("setValue", row.address);
	}

	//保存用户
	function saveUser() {
		$("#fm").form("submit", {
			url : 'user_saveUser.action',
			onSubmit : function() {
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					closeUserDialog();
					$("#dg").datagrid("reload");
				} else
					$.messager.alert("系统提示", "保存失败！");
			}
		});
	}

	//关闭对话框
	function closeUserDialog() {
		$("#dlg").dialog("close");
	}
</script>
<style type="text/css">
.lines-no td {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
}
</style>
<table id="dg" class="easyui-datagrid"
	data-options="url:'user_list.action',pagination:true,pageSize:20,rownumbers:true,fitColumns:true,fit:true,border:false,toolbar:'#tb'">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'id'">ID</th>
			<th data-options="field:'stringStatus'">身份</th>
			<th data-options="field:'userName'">用户名</th>
			<th data-options="field:'password'">密码</th>
			<th data-options="field:'nickName'">昵称</th>
			<th data-options="field:'realName'">姓名</th>
			<th data-options="field:'userImage'">头像图片</th>
			<th data-options="field:'sex'">性别</th>
			<th data-options="field:'birthday'">生日</th>
			<th data-options="field:'identityCode'">身份证</th>
			<th data-options="field:'email'">邮箱</th>
			<th data-options="field:'mobile'">手机号</th>
			<th data-options="field:'address'">地址</th>
		</tr>
	</thead>
</table>
<!-- 工具条 -->
<div id="tb">
	<a href="javascript:openAddUserDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-add',plain:true">添加</a> <a
		href="javascript:openModifyUserDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-edit',plain:true">修改</a> <a
		href="javascript:deleteUsers()" class="easyui-linkbutton"
		data-options="iconCls:'icon-cancel',plain:true">删除</a><span
		style="margin-right: 40px"></span><input type="text"
		class="easyui-searchbox" id="s_userName"
		data-options="prompt:'用户名',searcher:searchUser" />
</div>
<!-- 对话框 -->
<div id="dlg" class="easyui-dialog"
	style="width: 750px; height: 350px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons'">
	<form id="fm" method="post">
		<table>
			<tr style="display: none;">
				<td><input type="text" id="id" name="user.id" /></td>
			</tr>
			<tr>
				<td>身份：</td>
				<td><select class="easyui-combobox" id="status"
					name="user.status" data-options="panelHeight:'auto',editable:false"
					style="width: 250px">
						<option value="0">普通用户</option>
						<option value="1">VIP用户</option>
				</select></td>
				<td><div style="padding: 8px"></div></td>
				<td>用户名：</td>
				<td><input type="text" id="userName" name="user.userName"
					class="easyui-textbox" style="width: 250px"
					data-options="
						required:true,
						validType:{remote:['user_notExistUserName.action','user.userName']},
						invalidMessage:'用户名已存在'
					" /></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="text" id="password" name="user.password"
					class="easyui-textbox" style="width: 250px" required="required" /></td>
				<td><div style="padding: 8px"></div></td>
				<td>昵称：</td>
				<td><input type="text" id="nickName" name="user.nickName"
					class="easyui-textbox" style="width: 250px" required="required" /></td>
			</tr>
			<tr>
				<td>姓名：</td>
				<td><input type="text" id="realName" name="user.realName"
					class="easyui-textbox" style="width: 250px" /></td>
				<td><div style="padding: 8px"></div></td>
				<td>头像：</td>
				<td><input type="text" id="userImage" name="user.userImage"
					class="easyui-textbox" style="width: 250px" value="default.jpg" /></td>
			</tr>
			<tr>
				<td>性别：</td>
				<td><select class="easyui-combobox" id="sex" name="user.sex"
					data-options="panelHeight:'auto',editable:false"
					style="width: 250px">
						<option value="男">男</option>
						<option value="女">女</option>
				</select></td>
				<td><div style="padding: 8px"></div></td>
				<td>生日：</td>
				<td><input type="text" id="birthday" name="user.birthday"
					class="easyui-datebox" style="width: 250px" /></td>
			</tr>
			<tr>
				<td>身份证：</td>
				<td><input type="text" id="identityCode"
					name="user.identityCode" class="easyui-textbox"
					style="width: 250px" /></td>
				<td><div style="padding: 8px"></div></td>
				<td>邮箱：</td>
				<td><input type="text" id="email" name="user.email"
					class="easyui-textbox" style="width: 250px" /></td>
			</tr>
			<tr>
				<td>手机号：</td>
				<td><input type="text" id="mobile" name="user.mobile"
					class="easyui-textbox" style="width: 250px" required="required" /></td>
				<td><div style="padding: 8px"></div></td>
				<td>地址：</td>
				<td><input type="text" id="address" name="user.address"
					class="easyui-textbox" style="width: 250px" required="required" /></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons">
		<a href="javascript:saveUser()" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'">保存</a> <a
			href="javascript:closeUserDialog()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>