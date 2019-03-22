package com.mycompany.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.PageBean;

@Repository("baseDao")
public class BaseDaoImpl<T> implements BaseDao<T> {

	@PersistenceContext
	private EntityManager entityManager;

	public void persist(T o) {
		this.entityManager.persist(o);
	}

	public void remove(T o) {
		this.entityManager.remove(this.merge(o));// 若之前访问该资源的session中调用过此实体则会变成游离态，所以需要先变为持久化态再删除
	}

	public T merge(T o) {
		return this.entityManager.merge(o);
	}

	public T find(Class<T> entity, Object key) {
		return this.entityManager.find(entity, key);
	};

	public List<T> query(String jpql, Class<T> entity) {
		return this.entityManager.createQuery(jpql, entity).getResultList();
	}

	public List<T> query(String jpql, Class<T> entity, Object... params) {
		TypedQuery<T> query = this.entityManager.createQuery(jpql, entity);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.getResultList();
	};

	public T queryOne(String jpql, Class<T> entity) {
		List<T> list = this.query(jpql, entity);
		return list.isEmpty() ? null : list.get(0);
	};

	public T queryOne(String jpql, Class<T> entity, Object... params) {
		List<T> list = this.query(jpql, entity, params);
		return list.isEmpty() ? null : list.get(0);
	};

	public List<T> query(String jpql, Class<T> entity, PageBean pageBean) {
		return this.entityManager.createQuery(jpql, entity).setFirstResult(pageBean.getStart())
				.setMaxResults(pageBean.getRows()).getResultList();
	}

	public List<T> query(String jpql, Class<T> entity, PageBean pageBean, Object... params) {
		TypedQuery<T> query = this.entityManager.createQuery(jpql, entity);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return query.setFirstResult(pageBean.getStart()).setMaxResults(pageBean.getRows())
				.getResultList();
	}

	public Long count(String jpql) {
		return (Long) this.entityManager.createQuery(jpql).getSingleResult();
	}

	public Long count(String jpql, Object... params) {
		Query query = this.entityManager.createQuery(jpql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		return (Long) query.getSingleResult();
	}
}
