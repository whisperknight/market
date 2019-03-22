package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Tag;
import com.mycompany.service.TagService;
import com.mycompany.util.StringUtil;

@Service("tagService")
@Transactional
public class TagServiceImpl implements TagService {

	@Resource
	private BaseDao<Tag> baseDao;

	@Override
	public List<Tag> getAllTags() {
		return baseDao.query("from Tag", Tag.class);
	}

	@Override
	public List<Tag> getTagList(Tag s_tag, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from Tag");

		if (s_tag != null) {
			if (StringUtil.isNotEmpty(s_tag.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_tag.getName() + "%");
			}
		}

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Tag.class, pageBean,
					params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Tag.class,
					params.toArray());
	}

	@Override
	public Long getTagCount(Tag s_tag) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from Tag");

		if (s_tag != null) {
			if (StringUtil.isNotEmpty(s_tag.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_tag.getName() + "%");
			}
		}

		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void save(Tag tag) {
		if(tag != null) {
			if(tag.getId() != 0)
				baseDao.merge(tag);
			else
				baseDao.persist(tag);
		}
	}

	@Override
	public void delete(Tag tag) {
		if(tag != null && tag.getId() != 0)
			baseDao.remove(tag);
	}

	@Override
	public Tag getTagById(int id) {
		return baseDao.find(Tag.class, id);
	}

}
