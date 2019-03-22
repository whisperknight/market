package com.mycompany.action;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;
import com.mycompany.service.UserService;
import com.mycompany.util.DateJsonValueProcessor;
import com.mycompany.util.JsonUtil;
import com.mycompany.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Controller
public class UserAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource
	private UserService userService;

	private User user;// 获取user进行登录、注册、添加、修改
	private String imageCode;// 前端传过来的验证码

	private File imageFile;// 获取文件，由拦截器获取
	private String imageFileFileName;// 获取文件名（包括后缀），必须是name+FileName
	private String imageFileContentType;// 获取文件类型，必须是name+ContentType

	private String mainContentJsp;// 返回设定主页
	private String error;// 返回错误信息

	/** 下面后台域 **/

	private User s_user;// 获取查询条件
	private int page;// 获取datagrid的page
	private int rows;// 获取datagrid的rows
	private String strIds;// 获取批量删除的id串

	/**
	 * 跳转注册页面
	 * 
	 * @return
	 */
	public String toRegister() {
		mainContentJsp = "register.jsp";
		return SUCCESS;
	}

	/**
	 * 注册
	 * 
	 * @return
	 */
	public String register() {
		userService.saveUser(user);
		User currentUser = userService.login(user);
		ActionContext.getContext().getSession().put("currentUser", currentUser);
		mainContentJsp = "reg-success.jsp";
		return SUCCESS;
	}

	/**
	 * AJAX判断用户名是否存在
	 * 
	 * @throws Exception
	 */
	public void existUserWithUserName() throws Exception {
		boolean exist = userService.existUserByUserName(user.getUserName());
		JSONObject jo = new JSONObject();
		jo.put("exist", exist);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/**
	 * AJAX判断密码是否与原密码相同
	 * 
	 * @throws Exception
	 */
	public void ifPasswordchanged() throws Exception {
		boolean changed = userService.ifPasswordchanged(user.getUserName(), user.getPassword());
		JSONObject jo = new JSONObject();
		jo.put("changed", changed);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/**
	 * 跳转登录页面
	 * 
	 * @return
	 */
	public String toLogin() {
		mainContentJsp = "login.jsp";
		return SUCCESS;
	}

	/**
	 * 跳转管理员登录页面
	 * 
	 * @return
	 */
	public String toAdminLogin() {
		mainContentJsp = "login.jsp";
		return "toAdmin";
	}

	/**
	 * 跳转管理员主页
	 * 
	 * @return
	 */
	public String adminMain() {
		User currentUser = (User) ActionContext.getContext().getSession().get("currentUser");
		if (currentUser == null || currentUser.getId() == 0
				|| currentUser.getStatus() != User.ADMINISTRATOR)
			return ERROR;

		mainContentJsp = "main.jsp";
		return "toAdmin";
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	public String login() {
		if (!String.valueOf(ActionContext.getContext().getSession().get("sRand")).toUpperCase()
				.equals(imageCode.toUpperCase())) {
			error = "*验证码错误！";
			return ERROR;
		}
		User currentUser = userService.login(user);
		if (currentUser == null) {
			ServletActionContext.getRequest().setAttribute("user", user);
			error = "*用户名或密码错误！";
			return ERROR;
		} else {
			ActionContext.getContext().getSession().put("currentUser", currentUser);
			return "toIndex";
		}
	}

	/**
	 * 管理员登录
	 * 
	 * @return
	 */
	public String adminLogin() {
		if (!String.valueOf(ActionContext.getContext().getSession().get("sRand")).toUpperCase()
				.equals(imageCode.toUpperCase())) {
			error = "*验证码错误！";
			return "adminError";
		}
		User currentUser = userService.login(user);
		if (currentUser == null || currentUser.getStatus() != User.ADMINISTRATOR) {
			ServletActionContext.getRequest().setAttribute("user", user);
			error = "*用户名或密码错误！";
			return "adminError";
		} else {
			ActionContext.getContext().getSession().put("currentUser", currentUser);
			mainContentJsp = "main.jsp";
			return "toAdminMain";
		}
	}

	/**
	 * 注销
	 * 
	 * @return
	 */
	public String logout() {
//		ActionContext.getContext().getSession().clear();
		ServletActionContext.getRequest().getSession().invalidate();
		return "toIndex";
	}

	/**
	 * 跳转订单中心取数据再跳转用户中心
	 * 
	 * @return
	 */
	public String userCenter() {
		User currentUser = (User) ActionContext.getContext().getSession().get("currentUser");
		if (currentUser == null || currentUser.getId() == 0)
			return ERROR;

		mainContentJsp = "userCenter.jsp";
		return "toOrderCenter";
	}

	/**
	 * 保存用户头像
	 * 
	 * @throws Exception
	 */
	public void saveImageFile() throws Exception {
		if (imageFile == null) {
			JSONObject jo = new JSONObject();
			jo.put("imageFileFileName", "");
			JsonUtil.write(jo, ServletActionContext.getResponse());
		} else {
			imageFileFileName = UUID.randomUUID().toString()
					+ imageFileFileName.substring(imageFileFileName.lastIndexOf("."));
			// 定义一个目标文件
			File destFile = new File("D:/Project-Data/market-data/user/" + imageFileFileName);

			// 将临时文件复制到目标文件
			FileUtils.copyFile(imageFile, destFile);

			JSONObject jo = new JSONObject();
			jo.put("imageFileFileName", imageFileFileName);
			JsonUtil.write(jo, ServletActionContext.getResponse());
		}
	}

	/**
	 * 修改用户信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public void modify() throws Exception {
		User currentUser = (User) ActionContext.getContext().getSession().get("currentUser");
		if (currentUser == null || currentUser.getId() == 0) {
			JSONObject jo = new JSONObject();
			jo.put("success", false);
			JsonUtil.write(jo, ServletActionContext.getResponse());
			return;
		}

		if (StringUtil.isNotEmpty(user.getPassword()))
			currentUser.setPassword(user.getPassword());
		if (StringUtil.isNotEmpty(user.getNickName()))
			currentUser.setNickName(user.getNickName());
		if (StringUtil.isNotEmpty(user.getRealName()))
			currentUser.setRealName(user.getRealName());
		currentUser.setSex(user.getSex());
		if (user.getBirthday() != null)
			currentUser.setBirthday(user.getBirthday());
		if (StringUtil.isNotEmpty(user.getIdentityCode()))
			currentUser.setIdentityCode(user.getIdentityCode());
		if (StringUtil.isNotEmpty(user.getEmail()))
			currentUser.setEmail(user.getEmail());
		if (StringUtil.isNotEmpty(user.getMobile()))
			currentUser.setMobile(user.getMobile());
		if (StringUtil.isNotEmpty(user.getAddress()))
			currentUser.setAddress(user.getAddress());
		if (StringUtil.isNotEmpty(user.getUserImage())) {
			if (!currentUser.getUserImage().equals("default.jpg"))
				FileUtils.deleteQuietly(
						new File("D:/Project-Data/market-data/user/" + currentUser.getUserImage()));
			currentUser.setUserImage(user.getUserImage());
		}

		userService.saveUser(currentUser);
		ActionContext.getContext().getSession().put("currentUser", currentUser);

		JSONObject jo = new JSONObject();
		jo.put("success", true);
		JsonUtil.write(jo, ServletActionContext.getResponse());
	}

	/** 下面是后台控制 **/

	/**
	 * 获取所有查询到的用户
	 * 
	 * @throws Exception
	 */
	public void list() throws Exception {
		PageBean pageBean = new PageBean(page, rows);
		List<User> userList = userService.getUserList(s_user, pageBean);
		Long total = userService.getUserCount(s_user);

		JsonConfig jc = new JsonConfig();
		jc.registerJsonValueProcessor(java.util.Calendar.class,
				new DateJsonValueProcessor("yyyy-MM-dd"));
		jc.setExcludes(new String[] { "cart", "comments", "innerComments", "orders" });
		JSONArray rows = JSONArray.fromObject(userList, jc);

		JSONObject result = new JSONObject();
		result.put("rows", rows);
		result.put("total", total);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}

	/**
	 * 删除用户
	 * @throws Exception 
	 */
	public void deleteUser() throws Exception {
		String[] arr_ids = strIds.split(",");
		for (String id : arr_ids)
			userService.deleteUser(userService.getUserById(Integer.parseInt(id)));
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}
	
	/**
	 * 保存用户
	 * @throws Exception 
	 */
	public void saveUser() throws Exception {
		userService.saveUser(user);
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}
	
	/**
	 * easyui的textbox的remote判断用户名是否存在
	 * 
	 * @throws Exception
	 */
	public void notExistUserName() throws Exception {
		boolean pass = !userService.existUserByUserName(user.getUserName());
		JsonUtil.write(pass, ServletActionContext.getResponse());
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMainContentJsp() {
		return mainContentJsp;
	}

	public void setMainContentJsp(String mainContentJsp) {
		this.mainContentJsp = mainContentJsp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public File getImageFile() {
		return imageFile;
	}

	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}

	public String getImageFileFileName() {
		return imageFileFileName;
	}

	public void setImageFileFileName(String imageFileFileName) {
		this.imageFileFileName = imageFileFileName;
	}

	public String getImageFileContentType() {
		return imageFileContentType;
	}

	public void setImageFileContentType(String imageFileContentType) {
		this.imageFileContentType = imageFileContentType;
	}

	public User getS_user() {
		return s_user;
	}

	public void setS_user(User s_user) {
		this.s_user = s_user;
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
