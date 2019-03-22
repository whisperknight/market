<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ol class="breadcrumb">
		<c:forEach items="${breadcrumbs }" var="breadcrumb" varStatus="status">
			<c:choose>
				<c:when test="${status.last }">
					<li class="active">${breadcrumb.name }</li>
				</c:when>
				<c:otherwise>
					<li><a href="${breadcrumb.url }">${breadcrumb.name }</a></li>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</ol>
</div>
