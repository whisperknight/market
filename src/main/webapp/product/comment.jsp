<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h4>
	<b>用户评论</b>
</h4>
<div class="row" style="margin-bottom: 20px">
	<div class="col-md-1">
		<div class="userImageDiv">
			<img
				src="/market-data/user/${empty currentUser?'default.jpg': currentUser.userImage }"
				alt="${currentUser.nickName }">
		</div>
	</div>
	<div class="col-md-10">
		<textarea id="textArea_currentUser" class="form-control" rows="3"
			placeholder="请自觉遵守互联网相关的政策法规，严禁发布色情、暴力、反动的言论。"></textarea>
		<p id="textArea_currentUser_helpBlock" class="text-danger"
			style="display: none;">
			<span>请至少输入2个字符！</span>
		</p>
	</div>
	<div class="col-md-1">
		<button onclick="saveComment(${currentUser.id})" type="button"
			class="btn btn-primary" style="margin-top: 8px">
			发表<br>评论
		</button>
	</div>
</div>
<c:forEach items="${commentList }" var="comment">
	<div class="row">
		<div class="col-md-1">
			<div class="userImageDiv">
				<img src="/market-data/user/${comment.user.userImage }"
					alt="${comment.user.nickName }" class="img-circle">
			</div>
		</div>
		<div class="col-md-11">
			<div>
				<p>
					<b style="margin-right: 10px; color: #FB7A9C">${comment.user.stringStatus }</b><b><a
						href="#">${comment.user.nickName }</a></b>
				</p>
				<p>${comment.content }</p>
				<p class="help-block">
					<span style="margin-right: 10px"><fmt:formatDate
							pattern="yyyy-MM-dd HH:mm" value="${comment.createTime.time }"></fmt:formatDate></span><a
						href="javascript:void(0)" onclick="showTextArea(${comment.id})">回复</a>
				</p>
			</div>
		</div>
	</div>
	<c:forEach items="${comment.innerComments }" var="innerComment">
		<div class="row">
			<div class="col-md-offset-1 col-md-1">
				<div class="userImageDiv">
					<img src="/market-data/user/${innerComment.user.userImage }"
						alt="${innerComment.user.nickName }"
						alt="${innerComment.user.nickName }" class="img-circle">
				</div>
			</div>
			<div class="col-md-10">
				<p>
					<b style="margin-right: 10px; color: #FB7A9C">${innerComment.user.stringStatus }</b><b
						style="margin-right: 10px"><a href="#">${innerComment.user.nickName }</a></b>
					<c:if test="${not empty innerComment.replyToUser }">
						回复 <a href="#">@${innerComment.replyToUser.nickName }：</a>
					</c:if>
					${innerComment.content }
				</p>

				<p class="help-block">
					<span style="margin-right: 10px"><fmt:formatDate
							pattern="yyyy-MM-dd HH:mm"
							value="${innerComment.createTime.time }"></fmt:formatDate></span><a
						href="javascript:void(0)"
						onclick="showTextArea(${comment.id},${innerComment.user.id },'${innerComment.user.nickName }')">回复</a>
				</p>
			</div>
		</div>
	</c:forEach>
	<div class="row" style="margin-bottom: 20px; display: none"
		id="textAreaDiv_${comment.id }">
		<div class="col-md-1">
			<div class="userImageDiv">
				<img
					src="/market-data/user/${empty currentUser?'default.jpg': currentUser.userImage }"
					alt="${currentUser.nickName }" class="img-circle">
			</div>
		</div>
		<div class="col-md-10">
			<textarea class="form-control" rows="3"
				placeholder="请自觉遵守互联网相关的政策法规，严禁发布色情、暴力、反动的言论。"
				id="textArea_${comment.id }"></textarea>
			<p id="textArea_${comment.id }_helpBlock" class="text-danger"
				style="display: none;">
				<span>请至少输入2个字符！</span>
			</p>
			<div class="hidden" id="replyToUser_${comment.id }"></div>
		</div>
		<div class="col-md-1">
			<button
				onclick="saveComment(${not empty currentUser?currentUser.id : 0},${comment.id })"
				type="button" class="btn btn-primary" style="margin-top: 8px">
				发表<br>评论
			</button>
		</div>
	</div>
	<hr>
</c:forEach>
<div class="row text-center">
	<ul class="pagination">
		<c:forEach items="${pagination.items }" var="item">
			<li
				class="${item.disabled?'disabled':'' } ${item.active?'active':''}"><a
				href="${item.disabled || item.active ? 'javascript:void(0)' : item.url }"><span>${item.number }</span></a></li>
		</c:forEach>
	</ul>
</div>

<script type="text/javascript">
	function showTextArea(commentId, replyToUserId, replyToUserNickName){
		if(replyToUserId == null){
			$('#textArea_' + commentId).attr('placeholder','请自觉遵守互联网相关的政策法规，严禁发布色情、暴力、反动的言论。');
			$('#replyToUser_' + commentId).html('');			
		}
		else{
			$('#textArea_' + commentId).attr('placeholder','回复 @'+replyToUserNickName+'：');
			$('#replyToUser_' + commentId).html(replyToUserId);
		}
			
		$('#textAreaDiv_' + commentId).show();
	}
	
	function validate(commentId){
		if(commentId == null){
			if($('#textArea_currentUser').val().length < 2){
				$('#textArea_currentUser_helpBlock').show();
				return false;
			}else
				return true;
		}
		else{
			if($('#textArea_' + commentId).val().length < 2){
				$('#textArea_'+commentId+'_helpBlock').show();
				return false;
			}else
				return true;
		}
	}
	
	function saveComment(currentUserId,commentId){
		var productId = $('#productId').html();
		
		if(currentUserId == null || currentUserId == 0)
			location.href = "user_toLogin.action";
		else if(!validate(commentId))
			return;
		else if(commentId == null){
			$.post('comment_saveComment.action',{
				'comment.product.id':productId,
				'comment.user.id':currentUserId,
				'comment.content':$('#textArea_currentUser').val(),
			}, function(data) {
				$('#commentPart').html(data);
			});
		}else if($('#replyToUser_' + commentId).html() == null){
			$.post('comment_saveInnerComment.action',{
				'comment.product.id':productId,
				'innerComment.user.id':currentUserId,
				'innerComment.content':$('#textArea_' + commentId).val(),
				'innerComment.parentComment.id':commentId
			}, function(data) {
				$('#commentPart').html(data);
			});
		}else{
			$.post('comment_saveInnerComment.action',{
				'comment.product.id':productId,
				'innerComment.user.id':currentUserId,
				'innerComment.content':$('#textArea_' + commentId).val(),
				'innerComment.parentComment.id':commentId,
				'innerComment.replyToUser.id':$('#replyToUser_' + commentId).html()
			}, function(data) {
				$('#commentPart').html(data);
			});
		}
	}
	
	$(function(){
		setUserImagesPosition();
	});
</script>
