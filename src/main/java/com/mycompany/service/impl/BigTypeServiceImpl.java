package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.BigType;
import com.mycompany.entity.PageBean;
import com.mycompany.service.BigTypeService;
import com.mycompany.util.StringUtil;

@Service("bigTypeService")
@Transactional
public class BigTypeServiceImpl implements BigTypeService{

	@Resource
	private BaseDao<BigType> baseDao;
	
	@Override
	public List<BigType> getAllBigTypes() {
		return baseDao.query("from BigType", BigType.class);
	}

	@Override
	public BigType getBigType(int id) {
		return baseDao.find(BigType.class, id);
	}

	@Override
	public List<BigType> getBigTypeList(BigType s_bigType, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from BigType");
		
		if(s_bigType != null) {
			if(StringUtil.isNotEmpty(s_bigType.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_bigType.getName() + "%");
			}
		}
		
		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), BigType.class, pageBean, params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), BigType.class, params.toArray());
	}

	@Override
	public Long getBigTypeCount(BigType s_bigType) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from BigType");
		
		if(s_bigType != null) {
			if(StringUtil.isNotEmpty(s_bigType.getName())) {
				sb.append(" and name like ?1");
				params.add("%" + s_bigType.getName() + "%");
			}
		}
		
		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void saveBigType(BigType bigType) {
		if(bigType != null) {
			if(bigType.getId() != 0)
				baseDao.merge(bigType);
			else
				baseDao.persist(bigType);
		}
	}

	@Override
	public void deleteBigType(BigType bigType) {
		if(bigType != null && bigType.getId() != 0)
			baseDao.remove(bigType);
	}

	
}
