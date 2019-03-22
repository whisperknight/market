package com.mycompany.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * 解决hibernate对象级联问题
 * 
 * @author Administrator
 *
 */
public class ObjectJsonValueProcessor implements JsonValueProcessor {

	/**
	 * 保留的字段
	 */
	private String[] properties;

	/**
	 * 处理类型
	 */
	private Class<?> clazz;

	/**
	 * 构造方法
	 * 
	 * @param properties 保留的字段
	 * @param clazz      处理类型
	 */
	public ObjectJsonValueProcessor(String[] properties, Class<?> clazz) {
		this.properties = properties;
		this.clazz = clazz;
	}

	public Object processArrayValue(Object arg0, JsonConfig arg1) {
		return null;
	}

	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		PropertyDescriptor pd = null;
		Method method = null;
		StringBuffer json = new StringBuffer("{");
		try {
			for (int i = 0; i < properties.length; i++) {
				pd = new PropertyDescriptor(properties[i], clazz);
				method = pd.getReadMethod();
				String v = value == null ? "" : String.valueOf(method.invoke(value));
				json.append("'" + properties[i] + "':'" + v + "'");
				json.append(i != properties.length - 1 ? "," : "");
			}
			json.append("}");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSONObject.fromObject(json.toString());
	}

}
