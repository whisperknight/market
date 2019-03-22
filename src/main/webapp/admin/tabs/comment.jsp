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
	function searchComment() {
		$('#dg').datagrid('load', {
			'comment.product.id' : $('#s_productId').val(),
			'comment.user.userName' : $('#s_comment_userName').val(),
			'comment.content' : $('#s_comment_content').val()
		});
	}
	function searchInnerComment() {
		if ($('#s_parentCommentId').val() == "") {
			$.messager.alert("提示信息", "请先填写父评论ID！");
			return;
		}
		$('#dg').datagrid('load', {
			'innerComment.parentComment.id' : $('#s_parentCommentId').val(),
			'innerComment.user.userName' : $('#s_innerComment_userName').val(),
			'innerComment.content' : $('#s_innerComment_content').val()
		});
	}

	//批量删除评论
	function deleteCommentOrInnerComment() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要删除的数据！');
			return;
		}
		var ids = [];
		for (var i = 0; i < selectedRows.length; i++) {
			ids.push(selectedRows[i].id);
		}
		var strIds = ids.join(",");
		if (selectedRows[0].parentComment != null) {
			$.messager.confirm("系统提示", "确认要删除这<font color=red>"
					+ selectedRows.length + "</font>条数据吗？", function(r) {
				if (r) {
					$.post("comment_deleteInnerComment.action", {
						'strIds' : strIds
					}, function(result) {
						if (result.success) {
							$("#dg").datagrid("reload");
						} else {
							$.messager.alert('系统提示', "删除失败！");
						}
					}, "json");
				}
			});
		} else
			$.messager.confirm("系统提示", "确认要删除这<font color=red>"
					+ selectedRows.length + "</font>条数据吗？", function(r) {
				if (r) {
					$.post("comment_deleteComment.action", {
						'strIds' : strIds
					}, function(result) {
						if (result.success) {
							$("#dg").datagrid("reload");
						} else {
							$.messager.alert('系统提示', "删除失败！");
						}
					}, "json");
				}
			});
	}

	//打开修改评论对话框(感觉不如分成两个对话框)
	function openModifyCommentOrInnerCommentDialog() {
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
		if (row.parentComment != null) {
			$("#dlg").dialog("open").dialog("setTitle", "修改内部评论");
			$('#innerCommentId').val(row.id);
			$("#innerComment-content").textbox("setValue", row.content);
			
			$("#comment-content").textbox({"novalidate": true});
			$("#innerComment-content").textbox({"novalidate": false});
			$('#commentDiv').hide();
			$('#innerCommentDiv').show();
		} else {
			$("#dlg").dialog("open").dialog("setTitle", "修改评论");
			$('#commentId').val(row.id);
			$("#comment-content").textbox("setValue", row.content);
			
			$("#comment-content").textbox({"novalidate": false});
			$("#innerComment-content").textbox({"novalidate": true});
			$('#innerCommentDiv').hide();
			$('#commentDiv').show();
		}
	}

	//保存评论
	function saveCommentOrInnerComment() {
		if ($('#commentId').val() != "")
			$("#fm").form("submit", {
				url : 'comment_updateComment.action',
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.success) {
						closeCommentOrInnerCommentDialog();
						$("#dg").datagrid("reload");
					} else
						$.messager.alert("系统提示", "保存失败！");
				}
			});
		else if ($('#innerCommentId').val() != "")
			$("#fm").form("submit", {
				url : 'comment_updateInnerComment.action',
				onSubmit : function() {
					return $(this).form("validate");
				},
				success : function(result) {
					var result = eval('(' + result + ')');
					if (result.success) {
						closeCommentOrInnerCommentDialog();
						$("#dg").datagrid("reload");
					} else
						$.messager.alert("系统提示", "保存失败！");
				}
			});
	}

	//关闭对话框
	function closeCommentOrInnerCommentDialog() {
		$("#dlg").dialog("close");
	}

	function formatterProductId(value, row) {
		if (row.product != null)
			return row.product.id;
		else
			return "无";
	}
	function formatterParentCommentId(value, row) {
		if (row.parentComment != null)
			return row.parentComment.id;
		else
			return "无";
	}
	function formatterUserName(value, row) {
		if (row.user != null)
			return row.user.userName;
	}
	function formatterReplyToUserName(value, row) {
		if (row.replyToUser != null)
			return row.replyToUser.userName;
		else
			return "无";
	}
</script>
<style type="text/css">
.lines-no td {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
}
</style>
<table id="dg" class="easyui-datagrid"
	data-options="url:'comment_list.action',pagination:true,pageSize:20,rownumbers:true,fitColumns:true,fit:true,border:false,toolbar:'#tb'">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'product.id',formatter:formatterProductId">商品ID</th>
			<th
				data-options="field:'parentComment.id',formatter:formatterParentCommentId">父评论ID</th>
			<th data-options="field:'id'">评论ID</th>
			<th data-options="field:'user.userName',formatter:formatterUserName">评论用户</th>
			<th
				data-options="field:'replyToUser.userName',formatter:formatterReplyToUserName">被评论用户</th>
			<th data-options="field:'content'" style="width: 200px">内容</th>
			<th data-options="field:'createTime'">评论时间</th>
		</tr>
	</thead>
</table>
<!-- 工具条 -->
<div id="tb">
	<div>
		<input type="text" class="easyui-numberbox" id="s_productId"
			data-options="prompt:'商品ID'" /><input type="text"
			class="easyui-textbox" id="s_comment_userName"
			data-options="prompt:'评论用户'" /><input type="text"
			class="easyui-textbox" id="s_comment_content"
			data-options="prompt:'评论内容'" /><a onclick="searchComment()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true">搜索父评论</a>
	</div>
	<div>
		<input type="text" class="easyui-numberbox" id="s_parentCommentId"
			data-options="prompt:'所属父评论ID'" /><input type="text"
			class="easyui-textbox" id="s_innerComment_userName"
			data-options="prompt:'评论用户'" /><input type="text"
			class="easyui-textbox" id="s_innerComment_content"
			data-options="prompt:'评论内容'" /><a onclick="searchInnerComment()"
			class="easyui-linkbutton"
			data-options="iconCls:'icon-search',plain:true">搜索子评论</a>
	</div>
	<a href="javascript:openModifyCommentOrInnerCommentDialog()"
		class="easyui-linkbutton"
		data-options="iconCls:'icon-edit',plain:true">修改</a> <a
		href="javascript:deleteCommentOrInnerComment()"
		class="easyui-linkbutton"
		data-options="iconCls:'icon-cancel',plain:true">删除</a>

</div>
<!-- 对话框 -->
<div id="dlg" class="easyui-dialog"
	style="width: 600px; height: 300px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons'">
	<form id="fm" method="post">
		<table>
			<tr style="display: none;">
				<td><input type="text" id="commentId" name="comment.id" /></td>
				<td><input type="text" id="innerCommentId"
					name="innerComment.id" /></td>
			</tr>
			<tr>
				<td valign="top">评论内容：</td>
				<td><div id="commentDiv">
						<input class="easyui-textbox" id="comment-content"
							name="comment.content" data-options="multiline:true"
							style="width: 400px; height: 150px" required="required">
					</div>
					<div id="innerCommentDiv">
						<input class="easyui-textbox" id="innerComment-content"
							name="innerComment.content" data-options="multiline:true"
							style="width: 400px; height: 150px" required="required">
					</div></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons">
		<a href="javascript:saveCommentOrInnerComment()"
			class="easyui-linkbutton" data-options="iconCls:'icon-ok'">保存</a> <a
			href="javascript:closeCommentOrInnerCommentDialog()"
			class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>