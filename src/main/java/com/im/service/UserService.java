package com.im.service;

import com.im.dbmodel.User;

public interface UserService {
	
	// 根据账号密码获取用户记录
	public User getUserByUsernameAndPassword(String username, String password);

	// 插入新用户
	public void addEntitytoUser(User user);
	
	//插入新用户方法1
	public int addEntitytoUser1(User user);

	// 根据id获取用户一条记录
	public User getFromUserById(Integer id);
	
	// 根据用户新信息更新用户信息
	public void updateUserInfo(User user);

	// 根据id更新密码
	public void updatePasswordById(int id, String newPassword);
}
