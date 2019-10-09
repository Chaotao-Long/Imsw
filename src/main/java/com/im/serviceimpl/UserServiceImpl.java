package com.im.serviceimpl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.im.dao.UserMapper;
import com.im.dbmodel.User;
import com.im.service.UserService;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES) 
@Transactional
public class UserServiceImpl implements UserService {

	//依赖注入Dao层的bean
	@Autowired
	private UserMapper userMapper;
	
	//登录验证
	@Override
	public User getUserByUsernameAndPassword(String username, String password) {

		return userMapper.getUserByUsernameAndPassword(username, password);
	}

	//用户注册
	@Override
	public void addEntitytoUser(User user) {

		userMapper.InsertIntoUser(user);
		
	}
	
	//用户注册方法1
	public int addEntitytoUser1(User user) {

		return userMapper.insertSelective(user);		
	}
	
	//用户查询
	public User getFromUserById(Integer id) {
		 return userMapper.selectByPrimaryKey(id);
	}
	

	//修改用户信息
	@Override
	public void updateUserInfo(User user) {

		userMapper.updateUserInfo(user);
	}

	//修改密码
	@Override
	public void updatePasswordById(int id, String newPassword) {

		userMapper.updateUserPassword(id, newPassword);
	}

}
