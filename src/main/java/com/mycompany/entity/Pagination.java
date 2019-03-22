package com.mycompany.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页Model
 * 
 * @author WhisperKnight
 *
 */
public class Pagination {
	private List<Item> items = new ArrayList<>();

	private int maxPageView = 5;// 分页显示最大总页数，改动此数值需要联合改动分页算法

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public int getMaxPageView() {
		return maxPageView;
	}

	public void setMaxPageView(int maxPageView) {
		this.maxPageView = maxPageView;
	}

	/**
	 * 获取bootstrap的分页，每一个item对应一个分页上的按钮
	 * @param totalPage 总页数
	 * @param currentPage 当前页数
	 * @param preUrl 预置的url，例如 "product_list.action?typeId=3&pageBean.page="
	 * @return
	 */
	public Pagination setPagination(int totalPage, int currentPage, String preUrl) {
		items.add(new Item("首页", preUrl + 1, currentPage == 1));
		if (totalPage < maxPageView)
			for (int i = 1; i <= totalPage; i++)
				items.add(new Item(i + "", preUrl + i, false, currentPage == i));
		else if (currentPage <= 2)
			for (int i = 1; i <= maxPageView; i++)
				items.add(new Item(i + "", preUrl + i, false, currentPage == i));
		else if (totalPage - 1 <= currentPage && currentPage <= totalPage)
			for (int i = totalPage - maxPageView + 1; i <= totalPage; i++)
				items.add(new Item(i + "", preUrl + i, false, currentPage == i));
		else
			for (int i = currentPage - 2; i <= currentPage + 2; i++)
				items.add(new Item(i + "", preUrl + i, false, currentPage == i));
		items.add(new Item("尾页", preUrl + totalPage, currentPage == totalPage));

		return this;
	}

	public class Item {
		private String number;// 页码
		private String url;// 链接
		private boolean disabled;
		private boolean active;

		public Item() {
			super();
		}

		public Item(String number, String url, boolean disabled) {
			super();
			this.number = number;
			this.url = url;
			this.disabled = disabled;
		}

		public Item(String number, String url, boolean disabled, boolean active) {
			super();
			this.number = number;
			this.url = url;
			this.disabled = disabled;
			this.active = active;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}

		public boolean isActive() {
			return active;
		}

		public void setActive(boolean active) {
			this.active = active;
		}
	}
}
