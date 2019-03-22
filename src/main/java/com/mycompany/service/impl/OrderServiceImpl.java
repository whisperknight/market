package com.mycompany.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mycompany.dao.BaseDao;
import com.mycompany.entity.Order;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;
import com.mycompany.service.OrderService;
import com.mycompany.util.StringUtil;

@Service("orderService")
@Transactional
public class OrderServiceImpl implements OrderService {

	@Resource
	BaseDao<Order> baseDao;

	@Override
	public void saveOrder(Order order) {
		if (order.getId() == 0)
			baseDao.persist(order);
		else
			baseDao.merge(order);
	}

	@Override
	public List<Order> getOrders(User user) {
		if (user.getId() != 0)
			return baseDao.query("from Order where user.id=?1 order by createTime desc",
					Order.class, user.getId());
		return null;
	}

	@Override
	public void updateStatus(int orderId, int userId, int status) {
		Order q_order = baseDao.queryOne("from Order where id=?1 and user.id=?2", Order.class,
				orderId, userId);
		if (q_order != null) {
			q_order.setStatus(status);
			this.saveOrder(q_order);
		}
	}

	@Override
	public List<Order> getOrderList(Order s_order, PageBean pageBean) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("from Order");

		if (s_order != null) {
			if (s_order.getUser() != null && StringUtil.isNotEmpty(s_order.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_order.getOrderCode())) {
				sb.append(" and user.userName like ?1  and orderCode like ?2");
				params.add("%" + s_order.getUser().getUserName() + "%");
				params.add("%" + s_order.getOrderCode() + "%");
			} else if (s_order.getUser() != null
					&& StringUtil.isNotEmpty(s_order.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_order.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_order.getOrderCode())) {
				sb.append(" and orderCode like ?1");
				params.add("%" + s_order.getOrderCode() + "%");
			}
		}
		sb.append(" order by createTime desc");

		if (pageBean != null)
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Order.class, pageBean,
					params.toArray());
		else
			return baseDao.query(sb.toString().replaceFirst("and", "where"), Order.class,
					params.toArray());
	}

	@Override
	public Long getOrderCount(Order s_order) {
		List<Object> params = new LinkedList<>();
		StringBuffer sb = new StringBuffer("select count(*) from Order");

		if (s_order != null) {
			if (s_order.getUser() != null && StringUtil.isNotEmpty(s_order.getUser().getUserName())
					&& StringUtil.isNotEmpty(s_order.getOrderCode())) {
				sb.append(" and user.userName like ?1  and orderCode like ?2");
				params.add("%" + s_order.getUser().getUserName() + "%");
				params.add("%" + s_order.getOrderCode() + "%");
			} else if (s_order.getUser() != null
					&& StringUtil.isNotEmpty(s_order.getUser().getUserName())) {
				sb.append(" and user.userName like ?1");
				params.add("%" + s_order.getUser().getUserName() + "%");
			} else if (StringUtil.isNotEmpty(s_order.getOrderCode())) {
				sb.append(" and orderCode like ?1");
				params.add("%" + s_order.getOrderCode() + "%");
			}
		}

		return baseDao.count(sb.toString().replaceFirst("and", "where"), params.toArray());
	}

	@Override
	public Order getOrderById(int orderId) {
		return baseDao.find(Order.class, orderId);
	}

	@Override
	public void updateStatus(int orderId, int status) {
		Order q_order = getOrderById(orderId);
		if (q_order != null) {
			q_order.setStatus(status);
			this.saveOrder(q_order);
		}
	}
}
