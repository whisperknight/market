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
<script src="${pageContext.request.contextPath}/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript">
	$(function() {
		//网格线变成实线
		$('#dg').datagrid('getPanel').addClass('lines-no');

		//对话框关闭则自动重置内部内容
		$('#dlg').dialog({
			onClose : function() {
				$("#fm").form('reset');
				CKEDITOR.instances.content.setData('');
			}
		});
		
		CKEDITOR.replace('content');
	});

	//查询
	function searchNotice() {
		$('#dg').datagrid('load', {
			's_notice.title' : $('#s_noticeName').val()
		});
	}

	//批量删除公告
	function deleteNotices() {
		var selectedRows = $("#dg").datagrid('getSelections');
		if (selectedRows.length == 0) {
			$.messager.alert('系统提示', '请选择要删除的数据！');
			return;
		}
		var noticeIds = [];
		for (var i = 0; i < selectedRows.length; i++) {
			noticeIds.push(selectedRows[i].id);
		}
		var strIds = noticeIds.join(",");
		$.messager.confirm("系统提示", "确认要删除这<font color=red>"
				+ selectedRows.length + "</font>条数据吗？", function(r) {
			if (r) {
				$.post("notice_delete.action", {
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

	//打开添加公告对话框
	function openAddNoticeDialog() {
		$("#dlg").dialog("open").dialog("setTitle", "添加公告");
	}

	//打开修改公告对话框
	function openModifyNoticeDialog() {
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
		$("#dlg").dialog("open").dialog("setTitle", "修改公告");
		$("#id").val(row.id);
		$("#title").textbox("setValue", row.title);
		CKEDITOR.instances.content.setData(row.content);
	}

	//保存公告
	function saveNotice() {
		$("#fm").form("submit", {
			url : 'notice_save.action',
			onSubmit : function() {
				var content=CKEDITOR.instances.content.getData();
				if(content==null || $.trim(content)==""){
					$.messager.alert("系统提示","内容不能为空！");
					return false;
				}
				return $(this).form("validate");
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.success) {
					closeNoticeDialog();
					$("#dg").datagrid("reload");
				} else
					$.messager.alert("系统提示", "保存失败！");
			}
		});
	}

	//关闭对话框
	function closeNoticeDialog() {
		$("#dlg").dialog("close");
	}

	function formatterContent(val, row) {
		if (val.length > 40)
			return val.substr(0, 40) + '...';
		else
			return val;
	}
</script>
<style type="text/css">
.lines-no td {
	border-right: 1px solid #ebebeb;
	border-bottom: 1px solid #ebebeb;
}
</style>
<table id="dg" class="easyui-datagrid"
	data-options="url:'notice_list.action',pagination:true,pageSize:20,rownumbers:true,fitColumns:true,fit:true,border:false,toolbar:'#tb'">
	<thead>
		<tr>
			<th data-options="field:'cb',checkbox:true"></th>
			<th data-options="field:'id'">ID</th>
			<th data-options="field:'title'" width="100px">标题</th>
			<th data-options="field:'content',formatter:formatterContent"
				width="200px">内容</th>
		</tr>
	</thead>
</table>
<!-- 工具条 -->
<div id="tb">
	<a href="javascript:openAddNoticeDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-add',plain:true">添加</a> <a
		href="javascript:openModifyNoticeDialog()" class="easyui-linkbutton"
		data-options="iconCls:'icon-edit',plain:true">修改</a> <a
		href="javascript:deleteNotices()" class="easyui-linkbutton"
		data-options="iconCls:'icon-cancel',plain:true">删除</a><span
		style="margin-right: 40px"></span><input type="text"
		class="easyui-searchbox" id="s_noticeName"
		data-options="prompt:'公告名称',searcher:searchNotice" />
</div>
<!-- 对话框 -->
<div id="dlg" class="easyui-dialog"
	style="width: 800px; height: 600px; padding: 20px 20px"
	data-options="closed:true,buttons:'#dlg-buttons'">
	<form id="fm" method="post">
		<table>
			<tr style="display: none;">
				<td><input type="text" id="id" name="notice.id" /></td>
			</tr>
			<tr>
				<td>标题：</td>
				<td><input type="text" id="title" name="notice.title"
					class="easyui-textbox" data-options="validType:'length[1,50]'"
					style="width: 250px" required="required" /></td>
			</tr>
			<tr>
				<td valign="top">内容：</td>
				<td><textarea name="notice.content" id="content"></textarea></td>
			</tr>
		</table>
	</form>
	<div id="dlg-buttons">
		<a href="javascript:saveNotice()" class="easyui-linkbutton"
			data-options="iconCls:'icon-ok'">保存</a> <a
			href="javascript:closeNoticeDialog()" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">关闭</a>
	</div>
</div>