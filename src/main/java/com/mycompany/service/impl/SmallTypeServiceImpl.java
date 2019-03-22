package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.BigType;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.SmallType;
import com.mycompany.service.SmallTypeService;
import com.mycompany.util.StringUtil;

@Service("smallTypeService")
@Transactional
public class SmallTypeServiceImpl implements SmallTypeService {

	@Resource
	BaseDao<SmallType> baseDao;

	@Override
	public SmallType getSmallType(int id) {
		return baseDao.find(SmallType.class, id);
	}

	@Override
	public boolean existSmallTypeWithBigType(BigType bigType) {
		List<SmallType> list = baseDao.query("from SmallType where bigType.id = ?1",
				SmallType.class, bigType.getId());
		if (list.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public List<SmallType> getSmallTypeList(SmallType s_smallType, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from SmallType");
		
		if(s_smallType != null) {
			if(StringUtil.isNotEmpty(s_smallType.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_smallType.getName() + "%");
			}
		}
		
		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), SmallType.class, pageBean, params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), SmallType.class, params.toArray());
	}

	@Override
	public Long getSmallTypeCount(SmallType s_smallType) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from SmallType");
		
		if(s_smallType != null) {
			if(StringUtil.isNotEmpty(s_smallType.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_smallType.getName() + "%");
			}
		}
		
		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void saveSmallType(SmallType smallType) {
		if(smallType != null) {
			if(smallType.getId() != 0)
				baseDao.merge(smallType);
			else
				baseDao.persist(smallType);
		}
	}

	@Override
	public void deleteSmallType(SmallType smallType) {
		if(smallType != null && smallType.getId() != 0)
			baseDao.remove(smallType);
	}

}
