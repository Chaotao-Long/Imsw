package com.im.service;

import java.util.Date;

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
	
	//根据帐号获取用户一条记录
	public User getFromUserByUsername(String username);
	
	// 根据用户新信息更新用户信息
	public int updateUserInfo(User user);

	// 根据username更新密码
	public void updatePasswordByUsername(String username, String newPassword);
	
//	//根据用户id有选择性地更新用户信息
//	public int updateByPrimaryKeySelective(User user);
	
	//根据用户username来更新用户登录时间
  	public void updateLogintimeByUsername(String username, Date logintime);
  	
  	//改变用户状态
  	public void updateStatusByUsername(String username, Integer status);

}
