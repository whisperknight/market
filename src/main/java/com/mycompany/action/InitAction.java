package com.mycompany.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.mycompany.entity.BigType;
import com.mycompany.entity.News;
import com.mycompany.entity.Notice;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Product;
import com.mycompany.entity.Tag;
import com.mycompany.service.BigTypeService;
import com.mycompany.service.NewsService;
import com.mycompany.service.NoticeService;
import com.mycompany.service.ProductService;
import com.mycompany.service.TagService;

@Component
public class InitAction implements ServletContextAware{
	
	@Resource
	private BigTypeService bigTypeService;
	@Resource
	private TagService tagService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private NewsService newsService;
	@Resource
	private ProductService productService;
	
	
	/**
	 * 通过ServletContextAware获取application范围
	 */
	private ServletContext application;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.application = servletContext;
	}
	
	/**
	 *  初始化，被@PostConstruct注解的方法在依赖注入完成后被自动调用
	 */
	@PostConstruct
	public void init()  {
		System.out.println("init 执行前");
		
		List<BigType> bigTypeList = bigTypeService.getAllBigTypes();
		application.setAttribute("bigTypeList", bigTypeList);
		
		List<Tag> tagList = tagService.getAllTags();
		application.setAttribute("tagList", tagList);
		
		List<News> newsList = newsService.getNewsList(null, new PageBean(1, 8));
		application.setAttribute("newsList", newsList);
		
		List<Notice> noticeList = noticeService.getNoticeList(null, new PageBean(1, 8));
		application.setAttribute("noticeList", noticeList);
		
		List<Product> hotProductList = productService.getProducts(new Product(true, false), new PageBean(1, 12));
		application.setAttribute("hotProductList", hotProductList);

		List<Product> onSaleProductList = productService.getProducts(new Product(false, true), new PageBean(1, 6));
		application.setAttribute("onSaleProductList", onSaleProductList);
		
		System.out.println("init 执行完毕");
	}
}
