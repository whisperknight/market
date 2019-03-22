package com.mycompany.service;

import java.util.List;

import com.mycompany.entity.PageBean;
import com.mycompany.entity.User;

public interface UserService {
	public void saveUser(User user);
	
	public boolean existUserByUserName(String userName);

	public boolean ifPasswordchanged(String userName, String password);
	
	public User login(User user);
	
	/** 下面是后台服务 **/
	
	public List<User> getUserList(User s_user, PageBean pageBean);
	
	public Long getUserCount(User s_user);
	
	public void deleteUser(User user);
	
	public User getUserById(int userId);
}
