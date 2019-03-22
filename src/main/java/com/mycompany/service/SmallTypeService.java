package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.BigType;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.SmallType;

public interface SmallTypeService {
	public SmallType getSmallType(int id);
	
	public boolean existSmallTypeWithBigType(BigType bigType);
	
	/** 下面是后台服务 **/
	
	public List<SmallType> getSmallTypeList(SmallType s_smallType, PageBean pageBean);
	
	public Long getSmallTypeCount(SmallType s_smallType);
	
	public void saveSmallType(SmallType smallType);
	
	public void deleteSmallType(SmallType smallType);
}
