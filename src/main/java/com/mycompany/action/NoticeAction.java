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
import com.mycompany.entity.Notice;
import com.mycompany.entity.PageBean;
import com.mycompany.service.NoticeService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class NoticeAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;

	private HttpServletRequest request;

	@Resource
	private NoticeService noticeService;

	private Notice s_notice;

	private String mainContentJsp;
	private Notice resultNotice;
	private List<Breadcrumb> breadcrumbs;

	/** 下面后台域 **/

	private Notice notice;
	private int page;
	private int rows;
	private String strIds;

	/**
	 * 前台显示具体公告
	 * 
	 * @return
	 */
	public String showNotice() {
		mainContentJsp = "notice.jsp";
		resultNotice = noticeService.getNoticeById(s_notice.getId());
		this.setBreadcrumbs();
		return SUCCESS;
	}

	/**
	 * 设置bootstrap的路径导航
	 */
	private void setBreadcrumbs() {
		breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("首页", request.getContextPath()));
		if (s_notice != null) {
			breadcrumbs.add(new Breadcrumb("公告", "#"));
		}
		if (resultNotice != null)
			breadcrumbs.add(new Breadcrumb(resultNotice.getTitle(), "#"));
	}

	/** 下面是后台控制 **/

	/**
	 * 获取所有查询到的公告
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<Notice> noticeList = noticeService.getNoticeList(s_notice, pageBean);
		Long total = noticeService.getNoticeCount(s_notice);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(java.util.Calendar.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		JSONArray rows = JSONArray.fromObject(noticeList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存公告
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (notice != null) {
			if (notice.getId() == 0) {
				notice.setCreateTime(new GregorianCalendar());
			} else {
				s_notice = noticeService.getNoticeById(notice.getId());
				s_notice.setTitle(notice.getTitle());
				s_notice.setContent(notice.getContent());
				notice = s_notice;
			}
			noticeService.save(notice);
			JSONObject result = new JSONObject();
			result.put("success", true);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	/**
	 * 删除公告
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids)
			noticeService.delete(noticeService.getNoticeById(Integer.parseInt(id)));
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

	public Notice getS_notice() {
		return s_notice;
	}

	public void setS_notice(Notice s_notice) {
		this.s_notice = s_notice;
	}

	public Notice getResultNotice() {
		return resultNotice;
	}

	public void setResultNotice(Notice resultNotice) {
		this.resultNotice = resultNotice;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
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
