package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.Comment;
import com.mycompany.entity.PageBean;

public interface CommentService {
	public List<Comment> getCommentsByProductId(int id, PageBean pageBean);
	
	public Long getCommentCountByProductId(int id);
	
	public void saveComment(Comment comment);
	
	/** 下面是后台服务 **/
	
	public List<Comment> getCommentList(Comment s_comment, PageBean pageBean);
	
	public Long getCommentCount(Comment s_comment);
	
	public Comment getCommentById(int id);
	
	public void deleteComment(Comment comment);
}
