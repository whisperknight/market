package com.mycompany.action;

import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.BigType;
import com.mycompany.entity.Breadcrumb;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Pagination;
import com.mycompany.entity.Product;
import com.mycompany.entity.SmallType;
import com.mycompany.service.BigTypeService;
import com.mycompany.service.ProductService;
import com.mycompany.service.SmallTypeService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.DateUtil;
import com.mycompany.util.JsonUtil;
import com.mycompany.util.ObjectJsonValueProcessor;
import com.mycompany.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class ProductAction extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 1L;

	@Resource
	private ProductService productService;

	@Resource
	private BigTypeService bigTypeService;

	@Resource
	private SmallTypeService smallTypeService;

	private HttpServletRequest request;

	private Product s_product;// 获取查询条件
	private PageBean pageBean;// 获取分页

	private String mainContentJsp;// 返回主页内容是哪一个jsp页面
	private List<Breadcrumb> breadcrumbs;// 返回路径导航
	private List<Product> productList;// 返回搜索结果
	private Pagination pagination;// 返回封装后的分页
	private Product resultProduct;// 返回单个商品详情

	/** 下面是后台域 **/

	private int page;// 后台接受datagrid的传值
	private int rows = 8;// 前台默认每页记录数，后台接受datagrid的传值
	private File picFile;
	private String picFileFileName;
	private Product product;
	private String strIds;

	//不想使用struts2的标签，那么下面这四个日期需要设置时分秒不能直接通过Calendar或Date设置，此处采用传递字符串的形式来设置日期时间
	private String hotStartTime;
	private String hotEndTime;
	private String onSaleStartTime;
	private String onSaleEndTime;

	/**
	 * 分页显示商品列表
	 */
	@Override
	public String execute() throws Exception {
		if (pageBean == null)
			pageBean = new PageBean(1, rows);
		else
			pageBean.setRows(rows);

		mainContentJsp = "list.jsp";
		this.setBreadcrumbs();
		productList = productService.getProducts(s_product, pageBean);
		this.setPagination();
		return SUCCESS;
	}

	/**
	 * 显示单个商品详细内容
	 * 
	 * @return
	 */
	public String showProduct() {
		mainContentJsp = "product.jsp";
		resultProduct = productService.getProductById(s_product.getId());
		this.setProductHistory(resultProduct);
		return SUCCESS;
	}

	/**
	 * 设置浏览历史记录
	 * 
	 * @param product
	 */
	@SuppressWarnings("unchecked")
	private void setProductHistory(Product product) {
		HttpSession session = request.getSession();
		LinkedList<Product> productBrowsingHistory = (LinkedList<Product>) session
				.getAttribute("productBrowsingHistory");
		if (productBrowsingHistory == null)
			productBrowsingHistory = new LinkedList<>();

		boolean addToList = true;
		for (Product p : productBrowsingHistory)
			if (p.getId() == product.getId())
				addToList = false;

		if (addToList) {
			productBrowsingHistory.addFirst(product);
			if (productBrowsingHistory.size() > 5)
				productBrowsingHistory.removeLast();
		}
		session.setAttribute("productBrowsingHistory", productBrowsingHistory);
	}

	/**
	 * 设置bootstrap的路径导航
	 */
	private void setBreadcrumbs() {
		breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("首页", request.getContextPath()));
		if (s_product != null) {
			if (s_product.getBigType() != null) {
				BigType bigType = bigTypeService.getBigType(s_product.getBigType().getId());
				breadcrumbs.add(new Breadcrumb(bigType.getName(), "#"));
			} else if (s_product.getSmallType() != null) {
				SmallType smallType = smallTypeService
						.getSmallType(s_product.getSmallType().getId());
				breadcrumbs.add(new Breadcrumb(smallType.getBigType().getName(),
						"product.action?s_product.bigType.id=" + smallType.getBigType().getId()));
				breadcrumbs.add(new Breadcrumb(smallType.getName(), "#"));
			} else if (StringUtil.isNotEmpty(s_product.getName())) {
				breadcrumbs.add(new Breadcrumb("商品搜索", "#"));
			} else if (s_product.isHot()) {
				breadcrumbs.add(new Breadcrumb("火热爆款", "#"));
			} else if (s_product.isOnSale()) {
				breadcrumbs.add(new Breadcrumb("全场特价", "#"));
			} else
				breadcrumbs.add(new Breadcrumb("全部商品", "#"));
		} else
			breadcrumbs.add(new Breadcrumb("全部商品", "#"));
	}

	/**
	 * 设置bootstrap的分页
	 */
	public void setPagination() {
		long total = productService.getProductCount(s_product);
		int totalPage = (int) (total % rows == 0 ? total / rows : (total / rows + 1));
		int currentPage = pageBean.getPage();

		String url = "product.action?";
		if (s_product != null) {
			if (s_product.getBigType() != null)
				url = url + "s_product.bigType.id=" + s_product.getBigType().getId();
			else if (s_product.getSmallType() != null)
				url = url + "s_product.smallType.id=" + s_product.getSmallType().getId();
			else if (StringUtil.isNotEmpty(s_product.getName()))
				url = url + "s_product.name=" + s_product.getName();
			else if (s_product.isHot())
				url = url + "s_product.hot=true";
			else if (s_product.isOnSale())
				url = url + "s_product.onSale=true";
		}
		if (url.charAt(url.length() - 1) == '?')
			url += "pageBean.page=";
		else
			url += "&pageBean.page=";

		pagination = new Pagination().setPagination(totalPage, currentPage, url);
	}

	/** 下面是后台控制 **/

	/**
	 * 获取所有查询到的商品
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<Product> productList = productService.getProducts(s_product, pageBean);
		Long total = productService.getProductCount(s_product);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(java.util.Calendar.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jc.registerJsonValueProcessor(BigType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, BigType.class));
		jc.registerJsonValueProcessor(SmallType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, SmallType.class));
		jc.setExcludes(new String[] { "comments", "orders", "carts" });
		JSONArray rows = JSONArray.fromObject(productList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存商品
	 * 
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (picFile != null) {
			picFileFileName = UUID.randomUUID().toString()
					+ picFileFileName.substring(picFileFileName.lastIndexOf("."));
			File destFile = new File("D:/Project-Data/market-data/product/" + picFileFileName);
			FileUtils.copyFile(picFile, destFile);
			product.setImageName(picFileFileName);
		}

		if (product != null && product.getId() != 0) {
			s_product = productService.getProductById(product.getId());
			if (StringUtil.isNotEmpty(product.getName()))
				s_product.setName(product.getName());
			if (product.getPrice() >= 0)
				s_product.setPrice(product.getPrice());
			if (product.getStock() >= 0)
				s_product.setStock(product.getStock());
			if (StringUtil.isNotEmpty(product.getImageName())) {
				if (StringUtil.isNotEmpty(s_product.getImageName()))
					FileUtils.deleteQuietly(new File(
							"D:/Project-Data/market-data/product/" + s_product.getImageName()));
				s_product.setImageName(product.getImageName());
			}
			if (StringUtil.isNotEmpty(product.getDescription()))
				s_product.setDescription(product.getDescription());
			if (product.getBigType() != null && product.getBigType().getId() != 0)
				s_product.setBigType(product.getBigType());
			if (product.getSmallType() != null && product.getSmallType().getId() != 0)
				s_product.setSmallType(product.getSmallType());
			product = s_product;
		}

		productService.saveProduct(product);
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 删除商品
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids)
			productService.deleteProduct(productService.getProductById(Integer.parseInt(id)));
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 设置热卖，用product接受设置热卖与否、起止时间
	 * 
	 * @throws Exception
	 */
	public void setHot() throws Exception {
		if (product != null) {
			String[] arr_ids = strIds.split(",");
			for (String id : arr_ids) {
				s_product = productService.getProductById(Integer.parseInt(id));
				s_product.setHot(product.isHot());
				s_product.setHotStartTime(new GregorianCalendar());
				s_product.getHotStartTime().setTime(DateUtil.formatToDate(hotStartTime, "yyyy-MM-dd HH:mm:ss"));
				s_product.setHotEndTime(new GregorianCalendar());
				s_product.getHotEndTime().setTime(DateUtil.formatToDate(hotEndTime, "yyyy-MM-dd HH:mm:ss"));
				productService.saveProduct(s_product);
			}

			JSONObject result = new JSONObject();
			result.put("success", true);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	/**
	 * 设置特价，用product接受设置特价与否、特价、起止时间
	 * 
	 * @throws Exception
	 */
	public void setOnSale() throws Exception {
		if (product != null && product.getId() != 0) {
			s_product = productService.getProductById(product.getId());
			s_product.setOnSale(product.isOnSale());
			s_product.setOnSalePrice(product.getOnSalePrice());
			s_product.setOnSaleStartTime(new GregorianCalendar());
			s_product.getOnSaleStartTime().setTime(DateUtil.formatToDate(onSaleStartTime, "yyyy-MM-dd HH:mm:ss"));
			s_product.setOnSaleEndTime(new GregorianCalendar());
			s_product.getOnSaleEndTime().setTime(DateUtil.formatToDate(onSaleEndTime, "yyyy-MM-dd HH:mm:ss"));
			productService.saveProduct(s_product);

			JSONObject result = new JSONObject();
			result.put("success", true);
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public Product getS_product() {
		return s_product;
	}

	public void setS_product(Product s_product) {
		this.s_product = s_product;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public Pagination getPagination() {
		return pagination;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getMainContentJsp() {
		return mainContentJsp;
	}

	public void setMainContentJsp(String mainContentJsp) {
		this.mainContentJsp = mainContentJsp;
	}

	public Product getResultProduct() {
		return resultProduct;
	}

	public void setResultProduct(Product resultProduct) {
		this.resultProduct = resultProduct;
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

	public File getPicFile() {
		return picFile;
	}

	public void setPicFile(File picFile) {
		this.picFile = picFile;
	}

	public String getPicFileFileName() {
		return picFileFileName;
	}

	public void setPicFileFileName(String picFileFileName) {
		this.picFileFileName = picFileFileName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getStrIds() {
		return strIds;
	}

	public void setStrIds(String strIds) {
		this.strIds = strIds;
	}

	public String getHotStartTime() {
		return hotStartTime;
	}

	public void setHotStartTime(String hotStartTime) {
		this.hotStartTime = hotStartTime;
	}

	public String getHotEndTime() {
		return hotEndTime;
	}

	public void setHotEndTime(String hotEndTime) {
		this.hotEndTime = hotEndTime;
	}

	public String getOnSaleStartTime() {
		return onSaleStartTime;
	}

	public void setOnSaleStartTime(String onSaleStartTime) {
		this.onSaleStartTime = onSaleStartTime;
	}

	public String getOnSaleEndTime() {
		return onSaleEndTime;
	}

	public void setOnSaleEndTime(String onSaleEndTime) {
		this.onSaleEndTime = onSaleEndTime;
	}
}
