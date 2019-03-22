package com.mycompany.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.Breadcrumb;
import com.mycompany.entity.News;
import com.mycompany.entity.PageBean;
import com.mycompany.service.NewsService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class NewsAction extends ActionSupport implements ServletRequestAware{

	private static final long serialVersionUID = 1L;
	
	private HttpServletRequest request;

	@Resource
	private NewsService newsService;

	private News s_news;
	
	private String mainContentJsp;
	private News resultNews;
	private List<Breadcrumb> breadcrumbs;
	
	/** 下面后台域 **/

	private News news;
	private int page;
	private int rows;
	private String strIds;
	
	public String showNews() {
		mainContentJsp = "news.jsp";
		resultNews = newsService.getNewsById(s_news.getId());
		this.setBreadcrumbs();
		return SUCCESS;
	}
	
	/**
	 * 设置bootstrap的路径导航
	 */
	private void setBreadcrumbs() {
		breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("首页", request.getContextPath()));
		if (s_news != null) {
			breadcrumbs.add(new Breadcrumb("新闻", "#"));
		}
		if(resultNews != null)
			breadcrumbs.add(new Breadcrumb(resultNews.getTitle(), "#"));
	}
	
	/** 下面是后台控制 **/

	/**
	 * 获取所有查询到的新闻
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<News> newsList = newsService.getNewsList(s_news, pageBean);
		Long total = newsService.getNewsCount(s_news);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(java.util.Calendar.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray rows = JSONArray.fromObject(newsList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存新闻
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (news != null) {
			if (news.getId() == 0) {
				news.setCreateTime(new GregorianCalendar());
			} else {
				s_news = newsService.getNewsById(news.getId());
				s_news.setTitle(news.getTitle());
				s_news.setContent(news.getContent());
				news = s_news;
			}
			newsService.save(news);
			JSONObject result = new JSONObject();
			result.put("success", true);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	/**
	 * 删除新闻
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids)
			newsService.delete(newsService.getNewsById(Integer.parseInt(id)));
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	
	public String getMainContentJsp() {
		return mainContentJsp;
	}

	public void setMainContentJsp(String mainContentJsp) {
		this.mainContentJsp = mainContentJsp;
	}

	public News getS_news() {
		return s_news;
	}

	public void setS_news(News s_news) {
		this.s_news = s_news;
	}

	public News getResultNews() {
		return resultNews;
	}

	public void setResultNews(News resultNews) {
		this.resultNews = resultNews;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getStrIds() {
		return strIds;
	}

	public void setStrIds(String strIds) {
		this.strIds = strIds;
	}
}
