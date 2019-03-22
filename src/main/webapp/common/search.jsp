<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="row" style="margin-bottom: 20px">
		<div class="col-md-8 col-md-offset-2">
			<form action="product.action" method="post">
				<div class="input-group">
					<input type="text" class="form-control" placeholder="Search for..."
						name="s_product.name" value="${s_product.name }"> <span class="input-group-btn">
						<button class="btn btn-primary" type="submit">搜索</button>
					</span>
					<!-- /input-group -->
				</div>
			</form>
		</div>
	</div>
</div>