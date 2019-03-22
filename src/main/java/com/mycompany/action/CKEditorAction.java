package com.mycompany.action;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.mycompany.util.JsonUtil;
import com.opensymphony.xwork2.ActionSupport;

import net.sf.json.JSONObject;

@Controller
public class CKEditorAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private File upload;
	private String uploadFileName;
	
	/**
	 * 保存上传的文件
	 * 
	 * @throws Exception
	 */
	public void saveFile() throws Exception {
		if (upload != null) {
			uploadFileName = UUID.randomUUID().toString()
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
			// 定义一个目标文件
			File destFile = new File("D:/Project-Data/market-data/upload/" + uploadFileName);

			// 将临时文件复制到目标文件
			FileUtils.copyFile(upload, destFile);

			JSONObject jo = new JSONObject();
			jo.put("uploaded", 1);
			jo.put("fileName", uploadFileName);
			jo.put("url", "/market-data/upload/" + uploadFileName);
			JsonUtil.write(jo, ServletActionContext.getResponse());
		}
	}
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	
}
