package com.mycompany.entity;

/**
 * 分页Model
 * 
 * @author
 *
 */
public class PageBean {

	private int page; // 当前页
	private int rows; // 一页多少条

	
	public PageBean() {
		super();
	}

	public PageBean(int page, int rows) {
		super();
		this.page = page;
		this.rows = rows;
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

	/**
	 * 获取起始查询的位置
	 * 
	 * @return
	 */
	public int getStart() {
		return page < 1 ? 0 : (page - 1) * rows;
	}
}
