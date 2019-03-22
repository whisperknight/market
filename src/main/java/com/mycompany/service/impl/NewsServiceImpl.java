package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.News;
import com.mycompany.entity.PageBean;
import com.mycompany.service.NewsService;
import com.mycompany.util.StringUtil;

@Service("newsService")
@Transactional
public class NewsServiceImpl implements NewsService {

	@Resource
	private BaseDao<News> baseDao;

	@Override
	public News getNewsById(int id) {
		return baseDao.find(News.class, id);
	}
	
	@Override
	public List<News> getNewsList(News s_news, PageBean pageBean){
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from News");

		if (s_news != null) {
			if (StringUtil.isNotEmpty(s_news.getTitle())) {
				sb.append(" and title like ?1");
				params.add("%" + s_news.getTitle() + "%");
			}
		}

		sb.append(" order by createTime desc");

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), News.class, pageBean,
					params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), News.class,
					params.toArray());
	}

	

	@Override
	public Long getNewsCount(News s_news) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from News");

		if (s_news != null) {
			if (StringUtil.isNotEmpty(s_news.getTitle())) {
				sb.append(" and title like ?1");
				params.add("%" + s_news.getTitle() + "%");
			}
		}
		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void save(News news) {
		if (news != null) {
			if (news.getId() == 0)
				baseDao.persist(news);
			else
				baseDao.merge(news);
		}
	}

	@Override
	public void delete(News news) {
		if (news != null && news.getId() != 0)
			baseDao.remove(news);
	}

}
