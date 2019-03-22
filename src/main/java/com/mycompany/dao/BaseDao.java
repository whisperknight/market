package com.mycompany.dao;

import java.util.List;

import com.mycompany.entity.PageBean;

/**
 * 基础数据库操作
 * @author WhisperKnight
 *
 * @param <T> 泛型实体
 */
public interface BaseDao<T> {

	/**
	 * 添加
	 * 
	 * @param o
	 * @return
	 */
	public void persist(T o);

	/**
	 * 移出
	 * 
	 * @param o
	 */
	public void remove(T o);

	/**
	 * 更新
	 * 
	 * @param o
	 */
	public T merge(T o);
	
	/**
	 * 主键查询
	 * @param entity
	 * @param key
	 * @return
	 */
	public T find(Class<T> entity, Object key);

	/**
	 * jpql查询
	 * 
	 * @param jpql 
	 * @return
	 */
	public List<T> query(String jpql, Class<T> entity);

	/**
	 * jpql查询，带参数
	 * @param jpql
	 * @param entity
	 * @param params
	 * @return
	 */
	public List<T> query(String jpql, Class<T> entity, Object... params);

	/**
	 * 查询第一行
	 * 
	 * @param jpql 
	 * @return
	 */
	public T queryOne(String jpql, Class<T> entity);
	
	/**
	 * 查询第一行，带参数
	 * @param jpql
	 * @param entity
	 * @param params
	 * @return
	 */
	public T queryOne(String jpql, Class<T> entity, Object... params);

	/**
	 *  查询(带分页)
	 * 
	 * @param jpql
	 * @param entity
	 * @param page 第几页
	 * @param rows 每页显示几条记录
	 * @return
	 */
	public List<T> query(String jpql, Class<T> entity, PageBean pageBean);
	
	/**
	 *  查询(带分页)，带参数
	 * 
	 * @param jpql
	 * @param entity
	 * @param page 第几页
	 * @param rows 每页显示几条记录
	 * @param params
	 * @return
	 */
	public List<T> query(String jpql, Class<T> entity, PageBean pageBean, Object... params);

	/**
	 * select count(*) from
	 * 
	 * @param jpql
	 * @return
	 */
	public Long count(String jpql);

	/**
	 * select count(*) from，带参数
	 * 
	 * @param jpql
	 * @param param
	 * @return
	 */
	public Long count(String jpql, Object... params);
}
