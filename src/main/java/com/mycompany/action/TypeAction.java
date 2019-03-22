package com.mycompany.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.BigType;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.Product;
import com.mycompany.entity.SmallType;
import com.mycompany.service.BigTypeService;
import com.mycompany.service.ProductService;
import com.mycompany.service.SmallTypeService;
import com.mycompany.util.JsonUtil;
import com.mycompany.util.ObjectJsonValueProcessor;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class TypeAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	BigTypeService bigTypeService;

	@Resource
	SmallTypeService smallTypeService;

	@Resource
	ProductService productService;

	/** 下面后台域 **/
	private BigType bigType;
	private SmallType smallType;

	private BigType s_bigType;// 获取查询条件
	private SmallType s_smallType;// 获取查询条件
	private int page;// 获取datagrid的page
	private int rows;// 获取datagrid的rows
	private String strIds;// 获取批量删除的id串

	/** 下面是后台控制 **/

	//用于combobox
	public void getAllBigType() throws Exception {
		List<BigType> bigTypelist = bigTypeService.getAllBigTypes();

		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[] { "smallTypes", "products" });
		JSONArray rows = JSONArray.fromObject(bigTypelist, jc);

		JsonUtil.write(rows, ServletActionContext.getResponse());
	}

	//用于combobox
	public void getAllSmallType() throws Exception {
		if (bigType != null && bigType.getId() != 0) {
			BigType resultBigType = bigTypeService.getBigType(bigType.getId());
			JsonConfig jc = new JsonConfig();
			jc.setExcludes(new String[] { "smallTypes", "products" });
			JSONArray rows = JSONArray.fromObject(resultBigType.getSmallTypes(), jc);

			JsonUtil.write(rows, ServletActionContext.getResponse());
		}
	}

	/**
	 * 获取查询到的大类
	 * 
	 * @throws Exception
	 */
	public void listBigType() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<BigType> bigTypeList = bigTypeService.getBigTypeList(s_bigType, pageBean);
		Long total = bigTypeService.getBigTypeCount(s_bigType);

		JsonConfig jc = new JsonConfig();
		jc.setExcludes(new String[] { "smallTypes", "products" });
		JSONArray rows = JSONArray.fromObject(bigTypeList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 删除大类
	 * 
	 * @throws Exception
	 */
	public void deleteBigType() throws Exception {
		String[] arr_ids = strIds.split(",");
		List<String> notDeleteNames = new ArrayList<>();// 记录未删除的大类名称返回给前端
		for (String id : arr_ids) {
			BigType temp_bigType = bigTypeService.getBigType(Integer.parseInt(id));
			// 删除时外键关联验证
			if (!smallTypeService.existSmallTypeWithBigType(temp_bigType))
				bigTypeService.deleteBigType(temp_bigType);
			else
				notDeleteNames.add(temp_bigType.getName());
		}
		JSONObject result = new JSONObject();
		result.put("success", notDeleteNames.size() > 0 ? false : true);
		result.put("errorMsg", Arrays.toString(notDeleteNames.toArray()) + " 包含小类，无法删除！");
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存大类
	 * 
	 * @throws Exception
	 */
	public void saveBigType() throws Exception {
		bigTypeService.saveBigType(bigType);
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 获取查询到的小类
	 * 
	 * @throws Exception
	 */
	public void listSmallType() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<SmallType> smallTypeList = smallTypeService.getSmallTypeList(s_smallType, pageBean);
		Long total = smallTypeService.getSmallTypeCount(s_smallType);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(BigType.class,
				new ObjectJsonValueProcessor(new String[] { "id", "name" }, BigType.class));
		jc.setExcludes(new String[] { "products" });
		JSONArray rows = JSONArray.fromObject(smallTypeList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 删除小类
	 * 
	 * @throws Exception
	 */
	public void deleteSmallType() throws Exception {
		String[] arr_ids = strIds.split(",");
		List<String> notDeleteNames = new ArrayList<>();// 记录未删除的小类名称返回给前端
		for (String id : arr_ids) {
			SmallType temp_smallType = smallTypeService.getSmallType(Integer.parseInt(id));
			// 删除时外键关联验证
			if (!productService.existProductWithSmallType(temp_smallType))
				smallTypeService.deleteSmallType(temp_smallType);
			else
				notDeleteNames.add(temp_smallType.getName());
		}
		JSONObject result = new JSONObject();
		result.put("success", notDeleteNames.size() > 0 ? false : true);
		result.put("errorMsg", Arrays.toString(notDeleteNames.toArray()) + " 包含商品，无法删除！");
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 保存小类
	 * 
	 * @throws Exception
	 */
	public void saveSmallType() throws Exception {
		//由于数据库建表失误这里需要对商品所属大类进行更新
		s_smallType = smallTypeService.getSmallType(smallType.getId());
		if(s_smallType.getBigType().getId() != smallType.getBigType().getId()) {
			Product s_product = new Product();
			s_product.setSmallType(s_smallType);
			List<Product> products = productService.getProducts(s_product);
			for(Product p: products) {
				p.setBigType(smallType.getBigType());
				productService.saveProduct(p);
			}
		}
		
		smallTypeService.saveSmallType(smallType);
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	public BigType getBigType() {
		return bigType;
	}

	public void setBigType(BigType bigType) {
		this.bigType = bigType;
	}

	public BigType getS_bigType() {
		return s_bigType;
	}

	public void setS_bigType(BigType s_bigType) {
		this.s_bigType = s_bigType;
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

	public SmallType getS_smallType() {
		return s_smallType;
	}

	public void setS_smallType(SmallType s_smallType) {
		this.s_smallType = s_smallType;
	}

	public SmallType getSmallType() {
		return smallType;
	}

	public void setSmallType(SmallType smallType) {
		this.smallType = smallType;
	}
}
