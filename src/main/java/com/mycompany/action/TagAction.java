package com.mycompany.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.PageBean;
import com.mycompany.entity.Tag;
import com.mycompany.service.TagService;
import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class TagAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	private TagService tagService;

	/** 下面后台域 **/

	private Tag s_tag;
	private Tag tag;
	private int page;
	private int rows;
	private String strIds;

	/** 下面是后台控制 **/

	/**
	 * 获取所有查询到的标签
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<Tag> tagList = tagService.getTagList(s_tag, pageBean);
		Long total = tagService.getTagCount(s_tag);

		JSONArray rows = JSONArray.fromObject(tagList);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存标签
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (tag != null) {
			if (tag.getId() != 0) {
				s_tag = tagService.getTagById(tag.getId());
				s_tag.setName(tag.getName());
				s_tag.setUrl(tag.getUrl());
				tag = s_tag;
			}
			tagService.save(tag);
			JSONObject result = new JSONObject();
			result.put("success", true);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	/**
	 * 删除标签
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids)
			tagService.delete(tagService.getTagById(Integer.parseInt(id)));
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}
	
	
	public Tag getS_tag() {
		return s_tag;
	}

	public void setS_tag(Tag s_tag) {
		this.s_tag = s_tag;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
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
