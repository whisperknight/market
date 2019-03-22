package com.mycompany.action;

import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.Comment;
import com.mycompany.entity.InnerComment;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Pagination;
import com.mycompany.entity.Product;
import com.mycompany.entity.User;
import com.mycompany.service.CommentService;
import com.mycompany.service.InnerCommentService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.JsonUtil;
import com.mycompany.util.ObjectJsonValueProcessor;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class CommentAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	private CommentService commentService;

	@Resource
	private InnerCommentService innerCommentService;

	private PageBean pageBean;// 获取分页
	private Comment comment;// 获取评论
	private InnerComment innerComment;// 获取内部评论

	private List<Comment> commentList;
	private Pagination pagination;

	/** 下面是后台域 **/

	private int page;
	private int rows = 15;// 前台每页记录数
	private String strIds;

	/**
	 * AJAX获取评论
	 * 
	 * @throws Exception
	 */
	@Override
	public String execute() {
		if (pageBean == null)
			pageBean = new PageBean(1, rows);
		else
			pageBean.setRows(rows);

		this.setPagination();
		commentList = commentService.getCommentsByProductId(comment.getProduct().getId(), pageBean);
		return "toComment";
	}

	public String saveComment() {
		if (comment.getId() == 0)
			comment.setCreateTime(new GregorianCalendar());
		commentService.saveComment(comment);
		return "showComments";
	}

	public String saveInnerComment() {
		if (innerComment.getId() == 0)
			innerComment.setCreateTime(new GregorianCalendar());
		if (innerComment.getReplyToUser().getId() == 0)
			innerComment.setReplyToUser(null);
		innerCommentService.saveInnerComment(innerComment);
		return "showComments";
	}

	/** 下面是后台控制 **/

	/**
	 * 查询评论列表
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		if (innerComment != null && innerComment.getParentComment() != null
				&& innerComment.getParentComment().getId() != 0) {
			List<InnerComment> innerCommentList = innerCommentService
					.getInnerCommentList(innerComment, pageBean);
			Long total = innerCommentService.getInnerCommentCount(innerComment);

			JsonConfig jc = new JsonConfig();
			jc.registerJsonValueProcessor(java.util.Calendar.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
			jc.registerJsonValueProcessor(User.class,
					new ObjectJsonValueProcessor(new String[] { "id", "userName" }, User.class));
			jc.registerJsonValueProcessor(Comment.class,
					new ObjectJsonValueProcessor(new String[] { "id" }, Comment.class));
			JSONArray rows = JSONArray.fromObject(innerCommentList, jc);

			JSONObject result = new JSONObject();
			result.put("rows", rows);
			result.put("total", total);
			JsonUtil.write(result, ServletActionContext.getResponse());
		} else {
			List<Comment> commentList = commentService.getCommentList(comment, pageBean);
			Long total = commentService.getCommentCount(comment);

			JsonConfig jc = new JsonConfig();
			jc.registerJsonValueProcessor(java.util.Calendar.class,
					new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
			jc.registerJsonValueProcessor(User.class,
					new ObjectJsonValueProcessor(new String[] { "id", "userName" }, User.class));
			jc.registerJsonValueProcessor(Product.class,
					new ObjectJsonValueProcessor(new String[] { "id" }, Product.class));
			jc.setExcludes(new String[] { "innerComments" });
			JSONArray rows = JSONArray.fromObject(commentList, jc);

			JSONObject result = new JSONObject();
			result.put("rows", rows);
			result.put("total", total);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	public void updateComment() throws Exception {
		if (comment != null && comment.getId() != 0) {
			Comment s_comment = commentService.getCommentById(comment.getId());
			s_comment.setContent(comment.getContent());
			commentService.saveComment(s_comment);
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	public void updateInnerComment() throws Exception {
		if (innerComment != null && innerComment.getId() != 0) {
			InnerComment s_innerComment = innerCommentService
					.getInnerCommentById(innerComment.getId());
			s_innerComment.setContent(innerComment.getContent());
			innerCommentService.saveInnerComment(s_innerComment);
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	public void deleteComment() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids) {
			commentService.deleteComment(commentService.getCommentById(Integer.parseInt(id)));
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	public void deleteInnerComment() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids) {
			innerCommentService.deleteInnerComment(
					innerCommentService.getInnerCommentById(Integer.parseInt(id)));
		}
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination() {
		long total = commentService.getCommentCountByProductId(comment.getProduct().getId());
		int totalPage = (int) (total % rows == 0 ? total / rows : (total / rows + 1));
		int currentPage = pageBean.getPage();
		String url = "comment.action?productId=" + comment.getProduct().getId() + "$pageBean.page=";
		pagination = new Pagination().setPagination(totalPage, currentPage, url);
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public InnerComment getInnerComment() {
		return innerComment;
	}

	public void setInnerComment(InnerComment innerComment) {
		this.innerComment = innerComment;
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
