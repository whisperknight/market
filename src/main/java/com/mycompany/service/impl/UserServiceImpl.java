package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.Cart;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;
import com.mycompany.service.UserService;
import com.mycompany.util.StringUtil;

@Service("userSerive")
@Transactional
public class UserServiceImpl implements UserService {

	@Resource
	BaseDao<User> baseDao;

	@Override
	public void saveUser(User user) {
		if (user.getId() != 0) {
			if(user.getCart() == null) {
				user.setCart(baseDao.find(User.class, user.getId()).getCart());
			}
			baseDao.merge(user);
		}
		else {
			Cart cart = new Cart();
			cart.setUser(user);
			user.setCart(cart);
			if (StringUtil.isEmpty(user.getUserImage()))
				user.setUserImage("default.jpg");
			baseDao.persist(user);
		}
	}

	@Override
	public boolean existUserByUserName(String userName) {
		String jpql = "select count(*) from User where userName = ?1";
		Long count = baseDao.count(jpql, userName);
		if (count > 0)
			return true;
		return false;
	}

	@Override
	public User login(User user) {
		String jpql = "from User where userName = ?1 and password = ?2";
		return baseDao.queryOne(jpql, User.class, user.getUserName(), user.getPassword());
	}

	@Override
	public boolean ifPasswordchanged(String userName, String password) {
		String jpql = "select count(*) from User where userName = ?1 and password = ?2";
		Long count = baseDao.count(jpql, userName, password);
		if (count > 0)
			return false;
		return true;
	}

	/** 下面是后台服务 **/
	
	@Override
	public List<User> getUserList(User s_user, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from User");
		
		if(s_user != null) {
			if(StringUtil.isNotEmpty(s_user.getUserName())) {
				sb.append(" and userName like ?1");
				params.add("%" + s_user.getUserName() + "%");
			}
		}
		
		sb.append(" and status <> -1");// 非管理员
		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), User.class, pageBean, params.toArray());
		else
			return null;
	}

	@Override
	public Long getUserCount(User s_user) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from User");
		
		if(s_user != null) {
			if(StringUtil.isNotEmpty(s_user.getUserName())) {
				sb.append(" and userName like ?1");
				params.add("%" + s_user.getUserName() + "%");
			}
		}
		
		sb.append(" and status <> -1");// 非管理员
		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public void deleteUser(User user) {
		if(user != null && user.getId() != 0)
			baseDao.remove(user);
	}

	@Override
	public User getUserById(int userId) {
		return baseDao.find(User.class, userId);
	}
}
