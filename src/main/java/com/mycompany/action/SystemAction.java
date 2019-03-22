package com.mycompany.action;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

@Controller
public class SystemAction extends ActionSupport {

	@Resource
	InitAction initAction;
	
	private static final long serialVersionUID = 1L;

	/**
	 * 刷新系统缓存
	 * @throws Exception
	 */
	public void refresh() throws Exception {
		initAction.init();
		JSONObject result = new JSONObject();
		result.put("success", true);
		JsonUtil.write(result, ServletActionContext.getResponse());
	}
}
