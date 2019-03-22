package com.mycompany.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.Breadcrumb;
import com.mycompany.entity.CartProduct;
import com.mycompany.entity.Product;
import com.mycompany.entity.User;
import com.mycompany.service.CartService;
import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

@Controller
public class CartAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	CartService cartService;

	private Product product;// 获取加入购物车的产品ID
	private int num;// 获取单个商品的数量

	private String mainContentJsp;// 返回主页
	private List<Breadcrumb> breadcrumbs;// 返回路径导航
	private List<CartProduct> cartProducts;// 返回列表数据

	/**
	 * ajax添加商品到购物车
	 * 
	 * @throws Exception
	 */
	public void addToCart() throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0) {
			JSONObject jo = new JSONObject();
			jo.put("success", false);
			JsonUtil.write(jo, ServletActionContext.getResponse());
			return;
		}

		cartService.addProductToCart(user.getCart(), product);
		JSONObject jo = new JSONObject();
		jo.put("success", true);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/**
	 * ajax更新商品数量
	 * 
	 * @throws Exception
	 */
	public void updateCartProductNum() throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0 || num <= 0)
			return;

		cartService.updateCartProductNum(user.getCart(), product, num);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/**
	 * ajax删除商品
	 * 
	 * @throws Exception
	 */
	public void deleteCartProduct() throws Exception {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0)
			return;

		cartService.deleteCartProduct(user.getCart(), product);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/**
	 * 获取购物车商品列表
	 * 
	 * @return
	 */
	public String toList() {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0)
			return ERROR;
		
		mainContentJsp = "list.jsp";

		User currentUser = (User) ActionContext.getContext().getSession().get("currentUser");
		cartProducts = cartService.getCartProducts(currentUser);
		setBreadcrumbs();
		return SUCCESS;
	}

	/**
	 * 点击立即购买
	 * 
	 * @return
	 */
	public String buy() {
		User user = (User) ActionContext.getContext().getSession().get("currentUser");
		if (user == null || user.getId() == 0)
			return ERROR;

		cartService.addProductToCart(user.getCart(), product);
		return "toList";
	}

	/**
	 * 设置bootstrap的路径导航
	 */
	private void setBreadcrumbs() {
		breadcrumbs = new ArrayList<>();
		breadcrumbs.add(new Breadcrumb("首页", ServletActionContext.getRequest().getContextPath()));
		breadcrumbs.add(new Breadcrumb("购物车", "#"));
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getMainContentJsp() {
		return mainContentJsp;
	}

	public void setMainContentJsp(String mainContentJsp) {
		this.mainContentJsp = mainContentJsp;
	}

	public List<Breadcrumb> getBreadcrumbs() {
		return breadcrumbs;
	}

	public List<CartProduct> getCartProducts() {
		return cartProducts;
	}

	public void setCartProducts(List<CartProduct> cartProducts) {
		this.cartProducts = cartProducts;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
