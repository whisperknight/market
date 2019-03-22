package com.mycompany.action;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.CartProduct;
import com.mycompany.entity.Order;
import com.mycompany.entity.OrderProduct;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;
import com.mycompany.service.CartService;
import com.mycompany.service.OrderService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.DateUtil;
import com.mycompany.util.JsonUtil;
import com.mycompany.util.ObjectJsonValueProcessor;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class OrderAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	OrderService orderService;

	@Resource
	CartService cartService;

	Order s_order;// 获取需要操作的订单

	String mainContentJsp;// 返回用户中心页面
	boolean toOrder;// 指定跳转订单中心菜单的标识
	List<Order> orders;// 返回所有订单

	/** 下面是后台域 **/
	private int page;
	private int rows;
	String strIds;

	/**
	 * 由购物车生成订单
	 * 
	 * @return
	 */
	public String save() {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0 || user.getCart().getProducts().size() == 0)
			return ERROR;

		var order = new Order();
		order.setCreateTime(new GregorianCalendar());
		order.setOrderCode(DateUtil.getCurrentDateToStrCode());
		order.setStatus(Order.TO_SEND);// 目前没有接入支付宝就先跳过待付款步骤
		order.setUser(user);

		float totalCost = 0;
		var orderProducts = new ArrayList<OrderProduct>();
		List<CartProduct> cartProducts = cartService.getCartProducts(user);
		for (CartProduct cp : cartProducts) {
			OrderProduct op = new OrderProduct();
			op.setOrder(order);
			op.setProduct(cp.getProduct());
			op.setNum(cp.getNum());
			op.setPrice((cp.getProduct().isOnSale() ? cp.getProduct().getOnSalePrice()
					: cp.getProduct().getPrice()));
			orderProducts.add(op);
			totalCost += cp.getNum()
					* (cp.getProduct().isOnSale() ? cp.getProduct().getOnSalePrice()
							: cp.getProduct().getPrice());
		}
		order.setProducts(orderProducts);
		order.setCost(totalCost);

		orderService.saveOrder(order);
		cartService.clearCart(user.getCart());
		return "toOrders";// 需要重定向一下
	}

	/**
	 * userCenter获取订单数据然后显示用户中心页面
	 * 
	 * @return
	 */
	public String obtainOrdersData() {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0)
			return ERROR;

		orders = orderService.getOrders(user);
		return SUCCESS;
	}

	/**
	 * 跳转到用户中心并显示订单中心菜单
	 * 
	 * @return
	 */
	public String toOrders() {
		toOrder = true;
		return "toUserCenter";
	}

	/**
	 * 确认收货变交易完成
	 * 
	 * @throws Exception
	 */
	public void confirm() throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0) {
			JSONObject jo = new JSONObject();
			jo.put("success", false);
			JsonUtil.write(jo, ServletActionContext.getResponse());
		}

		if (s_order.getId() != 0) {
			orderService.updateStatus(s_order.getId(), user.getId(), 4);
			JSONObject jo = new JSONObject();
			jo.put("success", true);
			JsonUtil.write(jo, ServletActionContext.getResponse());
		}
	}

	/** 下面是后台控制 **/

	/**
	 * 获取所有订单
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<Order> orderList = orderService.getOrderList(s_order, pageBean);
		Long total = orderService.getOrderCount(s_order);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(java.util.Calendar.class,
				new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss"));
		jc.registerJsonValueProcessor(User.class,
				new ObjectJsonValueProcessor(new String[] { "id", "userName" }, User.class));
		jc.setExcludes(new String[] { "products" });
		JSONArray rows = JSONArray.fromObject(orderList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 获取单个订单下的商品列表详情
	 * 
	 * @throws Exception
	 */
	public void orderDetail() throws Exception {
		if (s_order != null && s_order.getId() != 0) {
			s_order = orderService.getOrderById(s_order.getId());
			List<OrderProduct> orderProducts = s_order.getProducts();
			JSONArray rows = new JSONArray();
			for (OrderProduct op : orderProducts) {
				JSONObject ja = new JSONObject();
				ja.put("name", op.getProduct().getName());
				ja.put("imageName", op.getProduct().getImageName());
				ja.put("price", op.getPrice());
				ja.put("num", op.getNum());
				ja.put("total", op.getPrice() * op.getNum());
				rows.add(ja);
			}
			JSONObject result = new JSONObject();
			result.put("rows", rows);
			result.put("total", rows.size());
			JsonUtil.write(result, ServletActionContext.getResponse());
		}
	}

	/**
	 * 发货
	 * 
	 * @throws Exception
	 */
	public void send() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids) {
			orderService.updateStatus(Integer.parseInt(id), Order.TO_RECEIVE);
		}
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

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public boolean isToOrder() {
		return toOrder;
	}

	public void setToOrder(boolean toOrder) {
		this.toOrder = toOrder;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public Order getS_order() {
		return s_order;
	}

	public void setS_order(Order s_order) {
		this.s_order = s_order;
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
