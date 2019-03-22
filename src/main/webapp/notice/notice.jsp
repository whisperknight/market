<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<jsp:include page="/common/path_nav.jsp"></jsp:include>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="text-center">
	<h1>
		<b>${resultNotice.title }</b>
	</h1>
	<div class="text-center">
		发布日期：
		<fmt:formatDate value="${resultNotice.createTime.time }"
			pattern="yyyy年MM月dd日 HH:mm" />
	</div>
	<hr>
</div>
${resultNotice.content }
