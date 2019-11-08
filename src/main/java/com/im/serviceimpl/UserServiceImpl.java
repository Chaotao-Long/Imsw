package com.im.serviceimpl;

import java.util.Date;

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

	//用户注册，用自定义的方法
	@Override
	public void addEntitytoUser(User user) {

		userMapper.InsertIntoUser(user);
		
	}
	
	//用户注册方法1，用mybatis的逆向工程生成器生成的默认方法
	public int addEntitytoUser1(User user) {

		return userMapper.insertSelective(user);		
	}
	
	//用户查询
	public User getFromUserById(Integer id) {
		 return userMapper.selectByPrimaryKey(id);
	}
	

	//修改用户信息
	@Override
	public int updateUserInfo(User user) {

		 return userMapper.updateByPrimaryKeySelective(user);
	}

	//修改密码
	@Override
	public void updatePasswordByUsername(String username, String newPassword) {

		userMapper.updateUserPassword(username, newPassword);
	}

	//根据username查询用户
	@Override
	public User getFromUserByUsername(String username) {
		
		return userMapper.getFromUserByUsername(username);
	}

	@Override
	public void updateLogintimeByUsername(String username, Date logintime) {

		userMapper.updateLogintimeByUsername(username, logintime);
	}

	@Override
	public void updateStatusByUsername(String username, Integer status) {

		userMapper.updateStatusByUsername(username, status);
	}

}
