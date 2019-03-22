package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.InnerComment;
import com.mycompany.entity.PageBean;

public interface InnerCommentService {
	public void saveInnerComment(InnerComment innerComment);

	/** 下面是后台服务 **/

	public List<InnerComment> getInnerCommentList(InnerComment s_innerComment, PageBean pageBean);

	public Long getInnerCommentCount(InnerComment s_innerComment);

	public InnerComment getInnerCommentById(int id);

	public void deleteInnerComment(InnerComment innerComment);
}
