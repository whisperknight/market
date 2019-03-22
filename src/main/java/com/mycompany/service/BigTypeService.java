package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.BigType;
import com.mycompany.entity.PageBean;

public interface BigTypeService {
	
	/**
	 * 查询所有大类
	 * @return
	 */
	public List<BigType> getAllBigTypes();
	
	public BigType getBigType(int id);
	
	/** 下面是后台服务 **/
	
	public List<BigType> getBigTypeList(BigType s_bigType, PageBean pageBean);
	
	public Long getBigTypeCount(BigType s_bigType);
	
	public void saveBigType(BigType bigType);
	
	public void deleteBigType(BigType bigType);
}
