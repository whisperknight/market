package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.InnerComment;
import com.mycompany.entity.PageBean;
import com.mycompany.service.InnerCommentService;
import com.mycompany.util.StringUtil;

@Service("innerCommentService")
@Transactional
public class InnerCommentServiceImpl implements InnerCommentService {

	@Resource
	BaseDao<InnerComment> baseDao;

	@Override
	public void saveInnerComment(InnerComment innerComment) {
		if (innerComment.getId() != 0)
			baseDao.merge(innerComment);
		else {
			baseDao.persist(innerComment);
		}
	}

	@Override
	public List<InnerComment> getInnerCommentList(InnerComment s_innerComment, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from InnerComment");

		if (s_innerComment != null) {
			if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and user.userName like ?2");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
				sb.append(" and content like ?3");
				params.add("%" + s_innerComment.getContent() + "%");
			} else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and user.userName like ?2");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
			} else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and content like ?2");
				params.add("%" + s_innerComment.getContent() + "%");
			} else if (s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
				sb.append(" and content like ?2");
				params.add("%" + s_innerComment.getContent() + "%");
			}

			else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
			} else if (s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and content like ?1");
				params.add("%" + s_innerComment.getContent() + "%");
			}

		}
		sb.append(" order by createTime desc");

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), InnerComment.class,
					pageBean, params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), InnerComment.class,
					params.toArray());
	}

	@Override
	public Long getInnerCommentCount(InnerComment s_innerComment) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from InnerComment");

		if (s_innerComment != null) {
			if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and user.userName like ?2");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
				sb.append(" and content like ?3");
				params.add("%" + s_innerComment.getContent() + "%");
			} else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and user.userName like ?2");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
			} else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
				sb.append(" and content like ?2");
				params.add("%" + s_innerComment.getContent() + "%");
			} else if (s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
				sb.append(" and content like ?2");
				params.add("%" + s_innerComment.getContent() + "%");
			}

			else if (s_innerComment.getParentComment() != null
					&& s_innerComment.getParentComment().getId() != 0) {
				sb.append(" and parentComment.id = ?1");
				params.add(s_innerComment.getParentComment().getId());
			} else if (s_innerComment.getUser() != null
					&& StringUtil.isNotEmpty(s_innerComment.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_innerComment.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_innerComment.getContent())) {
				sb.append(" and content like ?1");
				params.add("%" + s_innerComment.getContent() + "%");
			}

		}
		sb.append(" order by createTime desc");

		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public InnerComment getInnerCommentById(int id) {
		return baseDao.find(InnerComment.class, id);
	}

	@Override
	public void deleteInnerComment(InnerComment innerComment) {
		if(innerComment != null && innerComment.getId() != 0)
			baseDao.remove(innerComment);
	}

}
