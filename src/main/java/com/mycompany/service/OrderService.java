package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.Order;
import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;

public interface OrderService {
	public void saveOrder(Order order);
	
	public List<Order> getOrders(User user);
	
	public void updateStatus(int orderId, int userId, int status);
	
	/** 下面是后台服务 **/
	
	public List<Order> getOrderList(Order s_order, PageBean pageBean);
	
	public Long getOrderCount(Order s_order);
	
	public Order getOrderById(int id);
	
	public void updateStatus(int orderId, int status);
}
