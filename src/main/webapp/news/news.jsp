<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/common/path_nav.jsp"></jsp:include>
<div class="text-center">
	<h1>
		<b>${resultNews.title }</b>
	</h1>
	<div>
		发布日期：
		<fmt:formatDate value="${resultNews.createTime.time }"
			pattern="yyyy年MM月dd日 HH:mm" />
	</div>
	<hr>
</div>
${resultNews.content }
