package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.Comment;
import com.mycompany.entity.PageBean;
import com.mycompany.service.CommentService;
import com.mycompany.util.StringUtil;

@Service("commentService")
@Transactional
public class CommentServiceImpl implements CommentService {

	@Resource
	BaseDao<Comment> baseDao;

	@Override
	public List<Comment> getCommentsByProductId(int id, PageBean pageBean) {
		String jpql = "from Comment where product.id = ?1 order by createTime desc";
		List<Comment> comments = baseDao.query(jpql, Comment.class, pageBean, id);
		return comments;
	}

	@Override
	public Long getCommentCountByProductId(int id) {
		String jpql = "select count(*) from Comment where product.id = ?1";
		return baseDao.count(jpql, id);
	}

	@Override
	public void saveComment(Comment comment) {
		if (comment.getId() != 0)
			baseDao.merge(comment);
		else {
			baseDao.persist(comment);
		}
	}

	/** 下面是后台服务 **/

	@Override
	public List<Comment> getCommentList(Comment s_comment, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from Comment");

		if (s_comment != null) {
			if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and product.id = ?1");
				sb.append(" and user.userName like ?2");
				sb.append(" and content like ?3");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getUser().getUserName() + "%");
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())) {
				sb.append(" and product.id = ?1");
				sb.append(" and user.userName like ?2");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getUser().getUserName() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and product.id = ?1");
				sb.append(" and content like ?2");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and user.userName like ?1");
				sb.append(" and content like ?2");
				params.add("%" + s_comment.getUser().getUserName() + "%");
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0) {
				sb.append(" and product.id = ?1");
				params.add(s_comment.getProduct().getId());
			} else if (s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_comment.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and content like ?1");
				params.add("%" + s_comment.getContent() + "%");
			}

		}
		sb.append(" order by createTime desc");

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Comment.class,
					pageBean, params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Comment.class,
					params.toArray());
	}

	@Override
	public Long getCommentCount(Comment s_comment) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from Comment");

		if (s_comment != null) {
			if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and product.id = ?1");
				sb.append(" and user.userName like ?2");
				sb.append(" and content like ?3");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getUser().getUserName() + "%");
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())) {
				sb.append(" and product.id = ?1");
				sb.append(" and user.userName like ?2");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getUser().getUserName() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and product.id = ?1");
				sb.append(" and content like ?2");
				params.add(s_comment.getProduct().getId());
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and user.userName like ?1");
				sb.append(" and content like ?2");
				params.add("%" + s_comment.getUser().getUserName() + "%");
				params.add("%" + s_comment.getContent() + "%");
			} else if (s_comment.getProduct() != null && s_comment.getProduct().getId() != 0) {
				sb.append(" and product.id = ?1");
				params.add(s_comment.getProduct().getId());
			} else if (s_comment.getUser() != null
					&& StringUtil.isNotEmpty(s_comment.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_comment.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_comment.getContent())) {
				sb.append(" and content like ?1");
				params.add("%" + s_comment.getContent() + "%");
			}

		}
		sb.append(" order by createTime desc");

		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public Comment getCommentById(int id) {
		return baseDao.find(Comment.class, id);
	}

	@Override
	public void deleteComment(Comment comment) {
		if (comment != null && comment.getId() != 0)
			baseDao.remove(comment);
	}

}
