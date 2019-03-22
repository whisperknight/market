package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.Notice;
import com.mycompany.entity.PageBean;
import com.mycompany.service.NoticeService;
import com.mycompany.util.StringUtil;

@Service("noticeService")
@Transactional
public class NoticeServiceImpl implements NoticeService {

	@Resource
	private BaseDao<Notice> baseDao;

	@Override
	public Notice getNoticeById(int id) {
		return baseDao.find(Notice.class, id);
	}

	@Override
	public List<Notice> getNoticeList(Notice s_notice, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from Notice");

		if (s_notice != null) {
			if (StringUtil.isNotEmpty(s_notice.getTitle())) {
				sb.append(" and title like ?1");
				params.add("%" + s_notice.getTitle() + "%");
			}
		}

		sb.append(" order by createTime desc");

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Notice.class, pageBean,
					params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Notice.class,
					params.toArray());
	}

	@Override
	public Long getNoticeCount(Notice s_notice) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from Notice");

		if (s_notice != null) {
			if (StringUtil.isNotEmpty(s_notice.getTitle())) {
				sb.append(" and title like ?1");
				params.add("%" + s_notice.getTitle() + "%");
			}
		}
		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void save(Notice notice) {
		if (notice != null) {
			if (notice.getId() == 0)
				baseDao.persist(notice);
			else
				baseDao.merge(notice);
		}
	}

	@Override
	public void delete(Notice notice) {
		if (notice != null && notice.getId() != 0)
			baseDao.remove(notice);
	}

}
