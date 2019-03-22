package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.News;
import com.mycompany.entity.PageBean;

public interface NewsService {

	public List<News> getNewsList(News s_news, PageBean pageBean);

	public News getNewsById(int id);

	public Long getNewsCount(News s_news);

	public void save(News news);

	public void delete(News news);
}
