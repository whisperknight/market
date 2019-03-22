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
	function addTab(title, url) {
		if ($('#tt').tabs('exists', title)) {
			$('#tt').tabs('select', title);
		} else {
			var content = "<iframe scrolling='auto' frameborder='0' src='${pageContext.request.contextPath}/admin/tabs/"
					+ url + "' style='width:100%;height:99%'></iframe>";
			$('#tt').tabs('add', {
				title : title,
				content : content,
				closable : true
			});
		}
	}

	function refreshSystem() {
		$.post('sys_refresh.action', function(result) {
			var result = eval('(' + result + ')');
			if (result.success)
				$.messager.alert('系统提示', '刷新成功！');
			else
				$.messager.alert('系统提示', '刷新系统失败！');
		});
	}
</script>
<div style="margin-bottom: 20px">
	<div class="easyui-layout" style="width: auto; height: 700px;">
		<div data-options="region:'west'" style="width: 150px;">

			<div class="easyui-accordion" data-options="fit:true,border:false">
				<div title="用户管理" style="padding: 10px">
					<a href="javascript:addTab('所有用户','user.jsp')"
						class="easyui-linkbutton" data-options="plain:true">所有用户</a>
				</div>
				<div title="商品管理" style="padding: 10px" data-options="selected:true">
					<a href="javascript:addTab('所有商品','product.jsp')"
						class="easyui-linkbutton" data-options="plain:true">所有商品</a> <a
						href="javascript:addTab('商品大类','bigType.jsp')"
						class="easyui-linkbutton" data-options="plain:true">商品大类</a> <a
						href="javascript:addTab('商品小类','smallType.jsp')"
						class="easyui-linkbutton" data-options="plain:true">商品小类</a>
				</div>
				<div title="订单管理" style="padding: 10px">
					<a href="javascript:addTab('所有订单','order.jsp')"
						class="easyui-linkbutton" data-options="plain:true">所有订单</a>
				</div>
				<div title="评论管理" style="padding: 10px">
					<a href="javascript:addTab('所有评论','comment.jsp')"
						class="easyui-linkbutton" data-options="plain:true">所有评论</a>
				</div>
				<div title="公告管理" style="padding: 10px">
					<a href="javascript:addTab('商城公告','notice.jsp')"
						class="easyui-linkbutton" data-options="plain:true">商城公告</a>
				</div>
				<div title="新闻管理" style="padding: 10px">
					<a href="javascript:addTab('商城新闻','news.jsp')"
						class="easyui-linkbutton" data-options="plain:true">商场新闻</a>
				</div>
				<div title="标签管理" style="padding: 10px">
					<a href="javascript:addTab('外链标签','tag.jsp')"
						class="easyui-linkbutton" data-options="plain:true">外链标签</a>
				</div>
				<div title="系统管理" style="padding: 10px">
					<a href="javascript:refreshSystem()" class="easyui-linkbutton"
						data-options="plain:true">刷新系统缓存</a>
				</div>
			</div>

		</div>
		<div data-options="region:'center'">
			<div class="easyui-tabs" data-options="fit:true,border:false" id="tt">
				<div title="首页"
					style="padding: 10px; background: url('image/back-main.png') no-repeat right bottom; background-size: 400px"
					data-options="closable:true">
					<p style="font-size: 18px; text-indent: 2em">
						欢迎<font color="red"> ${currentUser.stringStatus }：${currentUser.nickName }
						</font>进入Market商城网后台管理系统！
					</p>
				</div>
			</div>
		</div>
	</div>
</div>
